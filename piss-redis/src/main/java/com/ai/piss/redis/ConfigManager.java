package com.ai.piss.redis;

import redis.clients.jedis.Jedis;

public class ConfigManager {
	public static String host="localhost";
	public static  int port=6379;
	public static  String auth;
	
	/**
	 * 获取jedis实例
	 * @return
	 */
	public static Jedis getJedis(){
		
		return new Jedis(ConfigManager.host,ConfigManager.port);
	}
}
