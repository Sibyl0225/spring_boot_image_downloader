package com.neo.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class MusicServiceImp implements MusicService {

    public List<Music> findMusic(){
    	List<Music> musicList = new ArrayList<Music>();
    	String musicFileString = null;
    	try {
    		musicFileString = FileUtils.readFileToString(new File("D://歌列表.txt"), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();			
			//TODO
		}
    	if(StringUtils.isEmpty(musicFileString)) {
    		return musicList;
    	}
		musicList = JSON.parseArray(musicFileString,Music.class);
    	
		return musicList;   	
    };
    
    public List<Music> findMusicByKeyword(String keyWord) {
		return null;
	}
    
    public static void main(String[] args) {
		MusicServiceImp musicServiceImp = new MusicServiceImp();
		List<Music> findMusic = musicServiceImp.findMusic();
		System.out.println("findMusicSize:"+findMusic.size());
		findMusic.forEach(music->System.out.println(music.toString()));
	}
   
}