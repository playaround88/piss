package com.ai.piss.redis;

import java.awt.List;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.ai.piss.PissHttpSession;

public class RedisHttpSession extends PissHttpSession{
	private static final Logger log=LoggerFactory.getLogger(RedisHttpSession.class);
	private Jedis jedis;

	public RedisHttpSession(HttpServletRequest request, String id,Integer timeout) {
		super(request, id,timeout);
		
		log.trace("init jedis connection");
		jedis=ConfigManager.getJedis();
		if(ConfigManager.auth!=null && !"".equals(ConfigManager.auth)){
			jedis.auth(ConfigManager.auth);
		}
	}
	
	@Override
	public void init() {
		log.trace("do redis session init...");
		log.trace("justify is session has create in redis?");
		if(!checkExists()){
			log.trace("session is new create, create in redis...");
			this.isNew=true;
			Map sessionMap=new HashMap();
			jedis.hset(getId(), "piss", "piss");
		}
		//重新设置
		setTTL(getMaxInactiveInterval());
	}


	@Override
	public void setTTL(int timeout) {
		jedis.expire(getId(), timeout);
	}

	@Override
	public boolean checkExists() {
		return jedis.exists(getId());
	}

	@Override
	public Object getAttribute(String name) {
		byte[] value=jedis.hget(getId().getBytes(), name.getBytes());
		return SerializeUtil.deserialize(value);
	}

	@Override
	public Object getValue(String name) {
		byte[] value=jedis.hget(getId().getBytes(), name.getBytes());
		return SerializeUtil.deserialize(value);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		Set<String> keys=jedis.hkeys(getId());
		return new CollectionEnumeration<String>(keys);
	}

	@Override
	public String[] getValueNames() {
		Set<String> keys=jedis.hkeys(getId());
		return (String[])keys.toArray();
	}

	@Override
	public void setAttribute(String name, Object value) {
		jedis.hset(getId().getBytes(), name.getBytes(), SerializeUtil.serialize(value));
	}

	@Override
	public void putValue(String name, Object value) {
		jedis.hset(getId().getBytes(), name.getBytes(), SerializeUtil.serialize(value));
	}

	@Override
	public void removeAttribute(String name) {
		jedis.hdel(getId(), name);
	}

	@Override
	public void removeValue(String name) {
		jedis.hdel(getId(), name);
	}

	@Override
	public void invalidate() {
		jedis.del(getId());
	}

	private boolean isNew=false;
	@Override
	public boolean isNew() {
		return this.isNew;
	}

}
