package com.neo.yande;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.neo.yande.downLoader.SimpleDownLoader;
import com.neo.yande.downLoader.YandeParse;
import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.Yande;

public class test {
	
	static Logger logger = Logger.getLogger(test.class);

	public static void main(String[] args) throws InterruptedException {
		
		
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

		int totalPage = 1;
		int total = 0;
		
		for (int j = 0; j < 1; j++) {			
			Downloader downLoader = new SimpleDownLoader();
			downLoader.commenDownloader(j, savePath, queues);
			downLoader.start();
		}
		YandeParse yandeParse = new YandeParse();
		for (int page = 1; page <= totalPage; page++) {
			List<Yande> yandes = null;
			try {				
				yandes = yandeParse.getListFromYande(page);
			} catch (Exception e) {
				logger.info("some error in parse "+page+" page!");
				continue;
			}
			for (Yande yande : yandes) {
				yande.setOverFlag(true);
				
				queues.put(yande);
				total++;
				System.out.println("put "+total+" into queue!");
			}
		}

		Yande enfYande = new Yande();
		enfYande.setOverFlag(true);
		queues.put(enfYande);

	}

}