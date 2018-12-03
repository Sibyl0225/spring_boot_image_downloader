package com.neo.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.neo.domain.Music;
import com.neo.domain.MusicServiceImp;
import com.neo.domain.User;
import com.neo.domain.UserRepository;

import net.sf.json.JSONObject;

@RestController
public class MusicController {
	
	@Autowired
	private MusicServiceImp musicService;
	
	private HashMap<String,Music> musicMap = null;
	
    @RequestMapping("/getMusic")
    public void getUser(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	Map<String, String[]> parameterMap = request.getParameterMap();
    	JSONObject paramsJson = JSONObject.fromObject(parameterMap);
        if(musicMap ==null) {
        	List<Music> musics = musicService.findMusic();
        	musicMap = new HashMap<String,Music>();
        	for (Music music : musics) {
        		String id = music.getId();
        		musicMap.put(id, music);
			}
        }
        String paramId = request.getParameter("id");
    	Music music = musicMap.get(paramId);
    	response.setContentType("text/html;charset=utf-8");
    	JsonObject resultObj = new JsonObject();
    	resultObj.addProperty("params", paramsJson.toString());
     	resultObj.addProperty("code", 0);
     	resultObj.addProperty("data", music.toString());
     	response.getWriter().write(resultObj.toString());
    	response.getWriter().close();
    }
    
    @RequestMapping("/getMusics")
    public ModelAndView getUsers(Locale locale,Model model) {    	
    	List<Music> musics = musicService.findMusic();
        return new ModelAndView("netease/musicList", "musics", musics);
    }
    
}