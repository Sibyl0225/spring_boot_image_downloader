package com.neo.yande;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.neo.yande.downLoader.MultipartDownloader;
import com.neo.yande.downLoader.SimpleDownLoader;
import com.neo.yande.downLoader.YandeParse;
import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.Yande;

public class test {
	
	private static Logger logger = LogManager.getLogger(test.class.getName());

	public static void main(String[] args) throws InterruptedException {
		
//		PropertyConfigurator.configure("bin/log4j.propertier");
		
		final String  savePath = "C:/yande";
		
		ArrayBlockingQueue<Yande> queues = new ArrayBlockingQueue<Yande>(200);
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		try {
//			Enumeration<URL> resources = classLoader.getResources("");
//			while(resources.hasMoreElements()) {
//				logger.info("resource:"+resources.nextElement());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		URL resource = ClassLoader.getSystemResource("");
//		logger.info("resource:"+resource);
//
//		String rootPath = String.valueOf(classLoader.getResource(""));	
//		logger.info("rootPath:"+rootPath);
//		System.exit(0);

		int totalPage = 20;
		int total = 0;
		
		for (int j = 0; j < 10; j++) {
//			Downloader downLoader = new MultipartDownloader();
			Downloader downLoader = new SimpleDownLoader();
			downLoader.commenDownloader(j, savePath, queues);
			downLoader.start();
		}
		YandeParse yandeParse = new YandeParse();
		for (int page = 3; page < totalPage; page++) {
			List<Yande> yandes = null;
			try {				
				yandes = yandeParse.getListFromYande(page);
			} catch (Exception e) {
				logger.info("some error in parse "+page+" page!");
				continue;
			}
			for (Yande yande : yandes) {
				yande.setOverFlag(false);
				yande.setCreateDate(new SimpleDateFormat().format(new Date()));
				queues.put(yande);
				total++;
				logger.info("put "+ "   " + total + yande.getImageId() +" into queue!");
			}
		}

		Yande enfYande = new Yande();
		enfYande.setOverFlag(true);
		queues.put(enfYande);

	}

}
