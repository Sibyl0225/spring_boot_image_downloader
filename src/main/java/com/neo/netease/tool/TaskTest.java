package com.neo.netease.tool;

import com.neo.util.DateUtil;
import com.neo.yande.downLoader.DownloaderTask;
import com.neo.yande.downLoader.SimpleDownLoader;
import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.Yande;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class TaskTest extends DownloaderTask {
	
	private static Logger logger = LogManager.getLogger(TaskTest.class.getName());
	
	String todayDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
	
	
	final String keyword = "aimer";
		
	final String  savePath = "C:/"+todayDate+"/netease/"+keyword+"/";
	
	int totalCount = 0;
	
	ArrayBlockingQueue<Yande> queues = new ArrayBlockingQueue<Yande>(1000);

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
			downLoader.endlessDownloader();
		}
		return this;
	}

	public DownloaderTask initeQueues(int startPage, int endPage) {
		

		Yande enfYande = new Yande();
		enfYande.setOverFlag(1);
		try {
			queues.put(enfYande);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int page = startPage; page <= endPage; page++) {
			List<Yande> yandes = null;
			try {				
				yandes = Poster.getQueryListByKeyword(keyword,startPage);;
				logger.info("get data of "+page+" page!暂停10秒!");
			} catch (Exception e) {
				logger.info("some error in parse "+page+" page!");
				continue;
			}
			for (Yande yande : yandes) {
				yande.setOverFlag(0);
				yande.setCreateDate(DateUtil.format(new Date()));
				try {
					queues.put(yande);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				totalCount++;
				logger.info("put   " +totalCount+ "   " + yande.getImageName() +" into queue!");
			}
		}

		return this;
	}
	
	
	public int getQueuesSize() {
		return queues.size();
	}


    @Override
	public DownloaderTask initeRedisQueues() {
		// TODO Auto-generated method stub
		return null;
	};

}
