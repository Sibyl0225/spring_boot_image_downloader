package com.neo.netease.tool;

import com.neo.yande.downLoader.DownloaderTask;

public class test {
	

	public static void main(String[] args) throws InterruptedException {
		int page  = 3;
				
		DownloaderTask initeQueues = new TaskTest().initeQueues(3, page);
		
		//while(initeQueues.getQueuesSize() < 20) {			
//        	//暂停30秒
//			Thread.sleep(10000);
//			System.out.println("列表还未下载完成！暂停10秒！");
		//}
		//开始下载
		initeQueues.initeDownloders(5);
   
	}

}
