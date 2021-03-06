package com.neo.yande.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.core.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClient {

    public Jedis jedis;//非切片额客户端连接
    public JedisPool jedisPool;//非切片连接池
    public redis.clients.jedis.ShardedJedis shardedJedis;//切片额客户端连接
    public ShardedJedisPool shardedJedisPool;//切片连接池
    public ResourceBundle resource = ResourceBundle.getBundle("redis");
    
    public RedisClient(int idx) 
    { 
        initialPool(idx); 
        //initialShardedPool(); 
        //shardedJedis = shardedJedisPool.getResource(); 
        jedis = jedisPool.getResource(); 
               
    } 
    
    public void getRedisProperties(){
    	
    	//redis.name redisClient
    	//redis.password sion8940
    	//redis.host 127.0.0.1
    	//redis.port 6379,
    	//redis.ssh_port 22,
    	//redis.timeout_connect 60000,
    	//redis.timeout_execute 60000
    	
    	ResourceBundle resource = ResourceBundle.getBundle("redis");
    	resource.getString("redis.name");
//    	String password = resource.getString("redis.password");
//    	String host = resource.getString("redis.host");
//    	String port = resource.getString("redis.port");
//    	String timeout = resource.getString("redis.timeout_connect");
//    	String timeout_execute = resource.getString("redis.timeout_execute");
    	
    }
 
    /**
     * 初始化非切片池
     */
    private void initialPool(int idx) 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxTotal(40); 
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(1000l); 
        config.setTestOnBorrow(false); 
        
    	String password = resource.getString("redis.password");
    	String host = resource.getString("redis.host");
    	int port = Integer.valueOf(resource.getString("redis.port"));
    	int timeout_connect = Integer.valueOf(resource.getString("redis.timeout_connect"));
    	
        jedisPool = new JedisPool(config,host,port,timeout_connect,password,idx);
    }
    
//    /** 
//     * 初始化切片池 
//     */ 
//    private void initialShardedPool() 
//    { 
//        // 池基本配置 
//        JedisPoolConfig config = new JedisPoolConfig(); 
//        config.setMaxTotal(20);
//        config.setMaxIdle(5); 
//        config.setMaxWaitMillis(1000l); 
//        config.setTestOnBorrow(false); 
//        // slave链接 
//        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
//        
//    	String password = resource.getString("redis.password");
//    	String host = resource.getString("redis.host");
//    	String port = resource.getString("redis.port");
//    	String timeout = resource.getString("redis.timeout_connect");
//    	
//        JedisShardInfo jedisShardInfo = new JedisShardInfo(host,Integer.valueOf(port));
//        jedisShardInfo.setConnectionTimeout(Integer.valueOf(timeout));
//        jedisShardInfo.setPassword(password);
//        shards.add(jedisShardInfo); 
//
//        // 构造池 
//        shardedJedisPool = new ShardedJedisPool(config, shards); 
//    } 

}
