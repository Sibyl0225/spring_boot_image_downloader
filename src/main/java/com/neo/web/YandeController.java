package com.neo.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.neo.yande.entity.RedisClient;
import com.neo.yande.entity.Yande;

import redis.clients.jedis.Jedis;

@RestController
public class YandeController {
	
	private static RedisClient redisClient = new RedisClient();
  
    @RequestMapping("/getYandes")
    public ModelAndView getUsers(Locale locale,Model model) {    	
		Jedis jedis = redisClient.jedisPool.getResource();
		Set<String> keySets = jedis.keys("*");
		Iterator<String> iterator = keySets.iterator();
		List<Yande> yandes = new ArrayList<Yande>();
		int count = 0;
		do {
			if(!iterator.hasNext())  break;
			String key = (String) iterator.next();
			Map<String, String> yandeMap = jedis.hgetAll(key);
			yandes.add(com.neo.yande.entity.YandeMap.mapToYande(yandeMap));
			count ++;
		} while (count < 10) ;
        return new ModelAndView("yande/yandePickList", "yandes", yandes);
    }
    
}