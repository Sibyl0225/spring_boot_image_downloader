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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.neo.entity.YandeEntry;
import com.neo.mapper.YandeMapper;
import com.neo.yande.test;
import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.RedisClient;
import com.neo.yande.entity.Yande;

import redis.clients.jedis.Jedis;

public class SimpleDownLoader extends Downloader {
	
	private static Logger logger = LogManager.getLogger(SimpleDownLoader.class.getName());
	public static ArrayBlockingQueue<Yande> queue = null;
	
	private static RedisClient redisClient = new RedisClient();

	@Override
	public void endlessDownloader() {
		queue = super.getQueue();
		String savePath = super.getSavePath();
		int threadId = super.getThreadId();
		if (queue == null)
			System.out.println("queue is null！");
		Yande yande = null;
		try {
			while (true) {
				yande = queue.take();
				logger.info("##############################"+queue.size()+" remain...");
				if (yande != null && yande.isOverFlag() == 0) {
					logger.info(yande);
					logger.info("threadId: " + threadId + " 获取数据yande, 创建时间为：" + yande.getCreateDate() + "，开始 "+ yande.getImageName() +" 下载！");
					logger.info("threadId: " + threadId + " 即将休眠1500ms...");
					Thread.sleep(500);
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
							logger.info("线程" + threadId + "最终下载量为： " + total + ",下载完毕!");
							logger.info("完成时间：   " + new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()));

							success(yande);
						}

					} catch (IOException e) {
						fail(yande);
						e.printStackTrace();
					}
				} else {
					queue.put(yande);
					Thread.sleep(3000l);
					logger.info("threadId: " + threadId + " 休眠！");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void success(Yande yande) {
		yande.setHadDownload(true);
		yande.setOverFlag(1);
		Jedis resource = redisClient.jedisPool.getResource();
		String hmResuly = resource.hmset(yande.getImageId(), yande.yandeToMap());
		resource.close();
		System.out.println("下载成功！hmResuly:"+hmResuly);
	}

	@Override
	public void fail(Yande yande) {
		yande.setHadDownload(false);
		yande.setOverFlag(1);
		
		System.out.println("下载失败！");
	}

}
