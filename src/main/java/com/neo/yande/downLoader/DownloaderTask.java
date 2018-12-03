package com.neo.yande.downLoader;

public abstract class DownloaderTask {
	
	public abstract DownloaderTask initeDownloders(int totol);
	
	public abstract DownloaderTask initeQueues(int startPage, int endPage);
	
	public abstract DownloaderTask initeRedisQueues();
	
	public abstract int getQueuesSize();

}
