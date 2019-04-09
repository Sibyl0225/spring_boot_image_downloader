package com.neo.yande.entity;

import java.util.concurrent.ArrayBlockingQueue;

public abstract class Downloader {
	
	private int threadId;
	private ArrayBlockingQueue<Yande>  queue;
	private String  savePath = null;

	public abstract void endlessDownloader() ;
	
	public abstract void success(Yande yande) ;
	
	public abstract void fail(Yande yande) ;

	
	public void commenDownloader(int threadId,String savePath, ArrayBlockingQueue<Yande>  queue) {
		initDownLoad(threadId,queue);
		this.setSavePath(savePath);
		System.out.println();
	}

	private void initDownLoad(int threadId,ArrayBlockingQueue<Yande>  queue) {
		this.threadId = threadId;
		this.queue = queue;
	}
	
	public int getThreadId() {
		return threadId;
	}

	public ArrayBlockingQueue<Yande> getQueue() {
		return queue;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public void setQueue(ArrayBlockingQueue<Yande> queue) {
		this.queue = queue;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

}
