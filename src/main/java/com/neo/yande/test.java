package com.neo.yande;

import com.neo.yande.downLoader.DownloaderTask;

import java.util.Timer;
import java.util.TimerTask;

public class test {
	
//	private static Logger logger = LogManager.getLogger(test.class.getName());

	public static void main(String[] args) throws InterruptedException {
		
//		PropertyConfigurator.configure("bin/log4j.propertier");
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
		
//		final String  savePath = "C:/yande";
//		
//		ArrayBlockingQueue<Yande> queues = new ArrayBlockingQueue<Yande>(200);
//
//		int totalPage = 20;
//		int total = 0;
//		
//		for (int j = 0; j < 10; j++) {
////			Downloader downLoader = new MultipartDownloader();
//			Downloader downLoader = new SimpleDownLoader();
//			downLoader.commenDownloader(j, savePath, queues);
//			downLoader.start();
//		}
		
//		new TaskTest().initeDownloders(3).initeQueues(10, 10);

        DownloaderTask downloaderTask = new TaskTest().initeQueues(0, 100);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                downloaderTask.initeRedisQueues().initeDownloders(8);
            }
        },3000l);



//
//		String url = "animal_ears gochuumon_wa_usagi_desu_ka? hoto_cocoa hoto_mocha kafuu_chino koi nekomimi pantyhose stockings thighhighs.jpg" ;
//		url = url.replaceAll("\\?", "");
//		System.out.println(url);

   
	}

}
