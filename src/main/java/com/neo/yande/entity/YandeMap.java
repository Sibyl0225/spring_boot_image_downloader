package com.neo.yande.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class YandeMap extends HashMap<String, Object> {

	private static final long serialVersionUID = -3103572308064455531L;

	public static Yande mapToYande(Map<String,String> properties)  {
		Class<?> yandeClazz = null;
		Yande yande = new Yande();
		try {
			yandeClazz = Class.forName("com.neo.yande.entity.Yande");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		// 获取类中的全部定义字段
		//Field[] fields = yandeClazz.getDeclaredFields();
		Field[] fields = yandeClazz.getFields();

		// 循环遍历字段，获取字段相应的属性值
		for (Field field : fields) {
			// 假设不为空。设置可见性，然后返回
			field.setAccessible(true);

			try {
				// 设置字段可见，就可以用get方法获取属性值。

				//System.out.println(field.getName()+":"+field.getType().getName());
				//if(field.getType().isPrimitive()) {
					
					if(field.isAccessible()) {
						yande = setFiledValueByType(field,yande,field.getType(),properties.get(field.getName()));
					}else {
						field.setAccessible(true);
						yande = setFiledValueByType(field,yande,field.getType(),properties.get(field.getName()));
						field.setAccessible(false);
					}
				//}
				
			} catch (Exception e) {
				 System.out.println("error--------Reason is:"+e.getMessage());
			}
		}
		return yande;
	}
	
	private static Yande setFiledValueByType(Field field, Yande yande, Class<?> type, String object)  {
		if(object == null) return yande;
		String clazzName = type.getName();
		//System.out.println(clazzName+":"+object);

		try {
			if (clazzName.equals("java.lang.String"))
				field.set(yande, object);
			if (clazzName.equals("long"))
				field.set(yande, Long.valueOf(object));
			if (clazzName.equals("boolean"))
				field.set(yande, Boolean.valueOf(object));
			if (clazzName.equals("int"))
				field.set(yande, Integer.valueOf(object));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		} 
		//System.out.println(yande);
        return yande;
		
	}

	public static void main(String[] args) throws ClassNotFoundException {
		
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxTotal(20);
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(1000l); 
        config.setTestOnBorrow(false); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        
        ResourceBundle resource = ResourceBundle.getBundle("redis");
        
    	String password = resource.getString("redis.password");
    	String host = resource.getString("redis.host");
    	String port = resource.getString("redis.port");
    	String timeout = resource.getString("redis.timeout_connect");
    	
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, Integer.valueOf(port));
        jedisShardInfo.setConnectionTimeout(Integer.valueOf(timeout));
        shards.add(jedisShardInfo); 

        // 构造池 
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, shards);
        Map<String, String> hgetAll = shardedJedisPool.getResource().hgetAll("*");
        System.out.println(hgetAll.size());

	}

}
