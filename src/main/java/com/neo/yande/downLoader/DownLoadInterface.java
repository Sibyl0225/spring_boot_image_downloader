package com.neo.yande.downLoader;

import com.neo.yande.entity.Yande;

public interface DownLoadInterface {

	public void endlessDownloader() ;
	
	public void success(Yande yande) ;
	
	public void fail() ;
	
}
