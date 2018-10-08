package com.neo.yande.entity;

public class RedisClient {

	public ShardedJedis sheredJedis;

	public void setSheredJedis(ShardedJedis sheredJedis) {
		this.sheredJedis = sheredJedis;
	}
	public ShardedJedis getSheredJedis() {
		return sheredJedis;
	}
	

}
