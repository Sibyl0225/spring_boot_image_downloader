package com.neo.yande;

import com.neo.util.DateUtil;
import com.neo.yande.downLoader.DownloaderTask;
import com.neo.yande.downLoader.SimpleDownLoader;
import com.neo.yande.downLoader.YandeParse;
import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.RedisClient;
import com.neo.yande.entity.Yande;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class TaskTest extends DownloaderTask {
	
	private static Logger logger = LogManager.getLogger(test.class.getName());
	
	private static RedisClient redisClient = new RedisClient();
	
	String todayDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
	final String  savePath = "C:/"+todayDate+"/yande";
	
	int totalCount = 0;
	
	ArrayBlockingQueue<Yande> queues = new ArrayBlockingQueue<Yande>(10000);

	public DownloaderTask initeDownloders(int total) {
		
        File file = new File(savePath);
        if(!file.exists()) {
        	  file.mkdirs();
        	  logger.info("mkdirs "+ savePath);
        }
		
		for(int j = 0; j < total; j++) {
//			Downloader downLoader = new MultipartDownloader();
			Downloader downLoader = new SimpleDownLoader();
			downLoader.commenDownloader(j, savePath, queues);
			downLoader.start();
		}
		return this;
	}

	public DownloaderTask initeQueues(int startPage, int endPage) {


		YandeParse yandeParse = new YandeParse();
        Jedis jedis = redisClient.jedisPool.getResource();
		for (int page = startPage; page <= endPage; page++) {
			List<Yande> yandes = null;
			try {				
				yandes = yandeParse.getListFromYande(page);
				logger.info("get data of "+page+" page!");
			} catch (Exception e) {
				logger.info("some error in parse "+page+" page!");
				continue;
			}

			for (Yande yande : yandes) {
				yande.setOverFlag(0);
				yande.setCreateDate(DateUtil.format(new Date()));
				try {
									
					HashMap<String, String> yandeToMap = yande.yandeToMap();
					//插入
					if(!jedis.exists(yande.getImageId())) {						
						jedis.hmset(yande.getImageId(), yandeToMap);
						logger.info(yande);
						queues.put(yande);	
					}else {
						Map<String, String> yandeMap = jedis.hgetAll(yande.getImageId());
						if(yandeMap.get("hadDownload").equals("true")) {							
							logger.error(yande.getImageId() + "had been download...");
                            System.exit(0);  //如果出现已下载就不再循环
						}else {
							logger.info(yande);
							queues.put(yande);	
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				totalCount++;
				logger.info("put   " +totalCount+ "   " + yande.getImageId() +" into queue!");
			}
		}

		Yande enfYande = new Yande();
		enfYande.setOverFlag(1);
		try {
			queues.put(enfYande);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public int getQueuesSize() {
		return queues.size();
	}

	@Override
	public DownloaderTask initeRedisQueues() {
		Jedis jedis = redisClient.jedisPool.getResource();
		Set<String> keySets = jedis.keys("*");
		Iterator<String> iterator = keySets.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Map<String, String> yandeMap = jedis.hgetAll(key);
			String pId = yandeMap.get("imageId");
			   if(!yandeMap.get("hadDownload").equals("true")) {
					try {
						queues.put(com.neo.yande.entity.YandeMap.mapToYande(yandeMap));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			   }
			
		}
		if(queues.size()> 0) {
			
			logger.info("将下载"+queues.size()+"张图片！");
		}else {
			logger.info("queues为空！");
		}
		jedis.close();
		return this;
	}

}
