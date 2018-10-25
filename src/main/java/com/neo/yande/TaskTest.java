package com.neo.yande;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.neo.util.DateUtil;
import com.neo.yande.downLoader.DownloaderTask;
import com.neo.yande.downLoader.SimpleDownLoader;
import com.neo.yande.downLoader.YandeParse;
import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.Yande;

public class TaskTest extends DownloaderTask {
	
	private static Logger logger = LogManager.getLogger(test.class.getName());
		
	final String  savePath = "C:/yande";
	
	int totalCount = 0;
	
	ArrayBlockingQueue<Yande> queues = new ArrayBlockingQueue<Yande>(200);

	public DownloaderTask initeDownloders(int total) {
		
		for (int j = 0; j < total; j++) {
//			Downloader downLoader = new MultipartDownloader();
			Downloader downLoader = new SimpleDownLoader();
			downLoader.commenDownloader(j, savePath, queues);
			downLoader.start();
		}
		return this;
	}

	public DownloaderTask initeQueues(int startPage, int endPage) {


		YandeParse yandeParse = new YandeParse();
		
		for (int page = startPage; page < endPage; page++) {
			List<Yande> yandes = null;
			try {				
				yandes = yandeParse.getListFromYande(page);
			} catch (Exception e) {
				logger.info("some error in parse "+page+" page!");
				continue;
			}
			for (Yande yande : yandes) {
				yande.setOverFlag(false);
				yande.setCreateDate(DateUtil.format(new Date()));
				try {
					queues.put(yande);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				totalCount++;
				logger.info("put   " +totalCount+ "   " + yande.getImageId() +" into queue!");
			}
		}

		Yande enfYande = new Yande();
		enfYande.setOverFlag(true);
		try {
			queues.put(enfYande);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}

}
