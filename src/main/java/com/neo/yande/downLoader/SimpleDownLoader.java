package com.neo.yande.downLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.io.FileUtils;

import com.neo.yande.entity.Const;
import com.neo.yande.entity.Downloader;
import com.neo.yande.entity.Yande;

public class SimpleDownLoader extends Downloader {

	@Override
	public void endlessDownloader() {
		ArrayBlockingQueue<Yande> queue = super.getQueue();
		String savePath = super.getSavePath();
		int threadId = super.getThreadId();
		if (queue == null)
			System.out.println("queue is null！");
		Yande yande = null;
		try {
			while (true) {
				yande = queue.take();
				if (yande != null && yande.isOverFlag() == false) {
					System.out
							.println("threadId: " + threadId + " 获取数据yande, 创建时间为：" + yande.getCreateDate() + "，开始下载！");
					System.out.println("threadId: " + threadId + " 即将休眠1500ms...");
					Thread.sleep(1500);
					try {
						FileUtils.copyURLToFile(new URL(yande.getPreviewImage()), 
								                new File(savePath, yande.getImageName()),
								                Const.ConnectionTimeout, 
								                Const.ReadTimeout);
						success(yande);
					} catch (IOException e) {
						fail(yande);
						e.printStackTrace();
					}
				} else {
					queue.put(yande);
					System.out.println("threadId: " + threadId + " 运行结束！即将退出");
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void success(Yande yande) {
		System.out.println("下载成功！");
	}

	@Override
	public void fail(Yande yande) {
		System.out.println("下载失败！");
	}

}
