package com.neo.yande.downLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.FileUtils;

import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.FileLengthTools;
import com.neo.yande.entity.Yande;

public class MultipartDownloader extends Downloader{

	@Override
	public void endlessDownloader() {
		ArrayBlockingQueue<Yande> queue = super.getQueue();
		String savePath = super.getSavePath();
		int threadId = super.getThreadId();
		if (queue == null)
			System.out.println("queue is null！");
		Yande yande = null;
		try {
			while (true) {
				yande = queue.take();
				if (yande != null && yande.isOverFlag() == false) {
					System.out
							.println("threadId: " + threadId + " 获取数据yande, 创建时间为：" + yande.getCreateDate() + "，开始下载！");
					System.out.println("threadId: " + threadId + " 即将休眠1500ms...");
					Thread.sleep(1500);
					try {
						int fileLength = FileLengthTools.getRemoteFileLenght(yande.getLargeSizeImage());
						int threadCount = 5;
						CountDownLatch latch = new CountDownLatch(threadCount);
						DownImage(savePath, yande, fileLength, threadCount, latch);			
						latch.await();
						System.out.println("所有线程任务结束！下载完成！");			
						success(yande);
					} catch (Exception e) {
						fail(yande);
						e.printStackTrace();
					}
				} else {
					queue.put(yande);
					System.out.println("threadId: " + threadId + " 运行结束！即将退出");
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void DownImage(String savePath, Yande yande, int fileLength, int threadCount, CountDownLatch latch) {
		int blockSize = fileLength/threadCount;//计算每个线程理论上下载的数量.
		HttpURLConnection connection = null;

		try {
				for (int i = 0; i < threadCount; i++) {
					int startPostion = i * blockSize;
					int endPostion = (i + 1) * blockSize - 1;
					if (i == (threadCount - 1))
						endPostion = fileLength-1  ;
					new ThreadImageDownload(i, yande, savePath,startPostion,endPostion,latch).start();

				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(connection!=null)  connection.disconnect();	
		}
		
	}

	@Override
	public void success(Yande yande) {
		System.out.println("下载成功！");
	}

	@Override
	public void fail(Yande yande) {
		System.out.println("下载失败！");
	}
	
	public static class ThreadImageDownload extends Thread {
		
		private int threadId;
		private String savePath;
		private Yande yande;
		private int endPostion;
		private int startPostion;
		private CountDownLatch latch;
		
		public ThreadImageDownload(int threadId, Yande yande,String savePath,int startPostion,int endPostion,CountDownLatch latch) {
			this.threadId = threadId;
			this.savePath = savePath;
			this.yande = yande;
			this.startPostion = startPostion;
			this.endPostion = endPostion;
			this.latch = latch;

		}

		public void run() {
			System.out.println("线程"+ threadId + "开始下载");
	        String imageId = yande.getImageId() ;
	        
	        try {
	            //分段请求网络连接,分段将文件保存到本地.
	            URL url = new URL(yande.getLargeSizeImage());           
                String imageName = yande.getImageName();
	            //加载下载位置的文件
	            File image = new File(savePath,imageName);
	            //进度记录文件
				File cashTxt = new File(savePath,imageId +"_"+threadId+ ".tmp");
	            
	            RandomAccessFile downThreadStream = new RandomAccessFile(image,"rwd");
	            if(cashTxt.exists()){//如果文件存在
	                downThreadStream = new RandomAccessFile(image,"rwd");	                
					String lastPostion_str = FileUtils.readFileToString(cashTxt, "UTF-8");
					startPostion = Integer.parseInt(lastPostion_str) - 1;
	            }
	            System.err.println(imageId +"_"+threadId+ ".tmp开始位置：   "+startPostion);
	            System.err.println(imageId +"_"+threadId+ ".tmp结束位置：   "+endPostion);
	            
	            if(startPostion < endPostion ){

	            	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            	connection.setRequestMethod("GET");
	            	connection.setConnectTimeout(6 * 1000);
	            	//设置分段下载的头信息。  Range:做分段数据请求用的。格式: Range bytes=0-1024  或者 bytes:0-1024
	            	connection.setRequestProperty("Range", "bytes="+ startPostion + "-" + endPostion );
	            	
	            	if(connection.getResponseCode() == 206){//200：请求全部资源成功， 206代表部分资源请求成功
	            		System.err.println("开始下载分段文件到本地……");
	            		InputStream inputStream = connection.getInputStream();//获取流
	            		downThreadStream.seek(startPostion);//文件写入的开始位置.
	            		
	            		byte[] buffer = new byte[1024];
	            		int length = -1;
	            		int total = 0;// 记录本次线程下载的总大小
	            		
            			RandomAccessFile accessfile = new RandomAccessFile(cashTxt, "rwd");
            			
	            		while((length = inputStream.read(buffer)) > 0){
	            			downThreadStream.write(buffer, 0, length);	            			
	            			total = total + length;
	            			// 去保存当前线程下载的位置，保存到文件中
	            			int currentThreadPostion = startPostion + total;// 计算出当前线程本次下载的
	            			accessfile.write(String.valueOf(currentThreadPostion).getBytes());
	            		}
	            			            		
	            		downThreadStream.close();
	            		inputStream.close(); 
            			accessfile.close();
	            		System.out.println("线程"+ threadId + "下载完毕");
	            		latch.countDown();
	            		
	            		// 当所有线程下载结束，删除存放下载位置的文件。
	            		synchronized (ThreadImageDownload.class) {
	            			if (latch.getCount() == 0) {
	            				System.out.println("所有线程下载完成");
	            				for (int i = 0; i < 20; i++) {
	            					File file = new File(savePath,imageId +"_"+i+ ".tmp");
	            					if(file.exists()){
	            						cleanTemp(file);
	            					} 	            					
	            				}
	            			}
	            		}
	            		
	            	}else{
	            		System.out.println("响应码是" +connection.getResponseCode() + ". 服务器不支持多线程下载              "+threadId+"线程");
	            		if(connection!=null)  connection.disconnect();  
	            		latch.countDown();
	            	}
	            	
	            }else {
	            	System.err.println(imageId+"  文件已下载不需要重复下载！");
	            	latch.countDown();
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

			super.run();	
		}

		// 删除线程产生的临时文件
		private synchronized void cleanTemp(File file) throws IOException {
			String canonicalPath = file.getCanonicalPath();
			if (file.delete()) {
				System.out.println(canonicalPath + "删除成功");
			} else {
				System.out.println(canonicalPath + "删除失败");
			}
			;
		}

	}

}
