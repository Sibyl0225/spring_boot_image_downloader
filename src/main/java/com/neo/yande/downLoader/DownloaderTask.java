package com.neo.yande.downLoader;

public abstract class DownloaderTask {
	
	public int total = 0;
	
	public abstract DownloaderTask initeDownloders(int totol);
	
	public abstract DownloaderTask initeQueues(int startPage	,int endPage);

}
