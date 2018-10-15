package com.neo.yande.downLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.Yande;

public class SimpleDownLoader extends Downloader {

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
							.println(yande);
					System.out
							.println("threadId: " + threadId + " 获取数据yande, 创建时间为：" + yande.getCreateDate() + "，开始 "+ yande.getImageName() +" 下载！");
					System.out.println("threadId: " + threadId + " 即将休眠1500ms...");
					Thread.sleep(1500);
					try {
						URL url = new URL(yande.getPreviewImage());
						// 加载下载位置的文件
						File image = new File(savePath, yande.getImageName());

						RandomAccessFile downThreadStream = new RandomAccessFile(image, "rwd");
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.setConnectTimeout(12 * 1000);
						int responseCode = 200;
						if (connection.getResponseCode() == responseCode) {// 200：请求全部资源成功，
							// 206代表部分资源请求成功
							InputStream inputStream = connection.getInputStream();// 获取流
							downThreadStream.seek(0);// 文件写入的开始位置.
							byte[] buffer = new byte[1024];
							int length = -1;
							int total = 0;// 记录本次线程下载的总大小
							while ((length = inputStream.read(buffer)) > 0) {
								downThreadStream.write(buffer, 0, length);
								total = total + length;
							}
							downThreadStream.close();
							inputStream.close();
							System.out.println("线程" + threadId + "最终下载量为： " + total + ",下载完毕!");
							System.out.println("完成时间：   " + new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()));

							success(yande);
						}

					} catch (IOException e) {
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

	@Override
	public void success(Yande yande) {
		System.out.println("下载成功！");
	}

	@Override
	public void fail(Yande yande) {
		System.out.println("下载失败！");
	}

}
