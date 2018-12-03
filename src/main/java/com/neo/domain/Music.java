package com.neo.domain;

import com.neo.util.DateUtil;

public class Music {
//	"id": 32358691,
//    "name": "Brave Shine",
//    "artist": ["Aimer"],
//    "album": "Brave Shine (期間生産限定アニメ盤)",
//    "pic_id": "2940094094533735",
//    "url_id": 32358691,
//    "lyric_id": 32358691,
//    "source": "netease",
//    "br": "128",
//    "size": "3744539",
//    "url": "http://m8c.music.126.net/20181126195151/77ffcbbb2b78eb10159a6a71c1ed138e/ymusic/b3c4/2fa6/ef79/d5cd51cf42231a69bef68168f6dd4404.mp3"
      
	
	  String id;
	  String name;
	  String artist;
	  String album;
	  String pic_id;
	  String url_id;
	  String lyric_id;
	  String source;
	  String br;
	  String size;
	  String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}
	public String getUrl_id() {
		return url_id;
	}
	public void setUrl_id(String url_id) {
		this.url_id = url_id;
	}
	public String getLyric_id() {
		return lyric_id;
	}
	public void setLyric_id(String lyric_id) {
		this.lyric_id = lyric_id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getBr() {
		return br;
	}
	public void setBr(String br) {
		this.br = br;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "Music " + DateUtil.getDate() + " [id=" + id + ", name=" + name + ", artist=" + artist + ", album=" + album + ", pic_id=" + pic_id
				+ ", url_id=" + url_id + ", lyric_id=" + lyric_id + ", source=" + source + ", br=" + br + ", size="
				+ size + ", url=" + url + "]";
	}
	  
	  
}
