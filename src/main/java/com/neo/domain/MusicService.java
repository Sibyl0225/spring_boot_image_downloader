package com.neo.domain;

import java.util.List;

public interface MusicService {

    List<Music> findMusic();
    
    List<Music> findMusicByKeyword(String keyWord);
   
}