package com.ai.piss;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
/**
 * HttpSession 抽取公共方法
 * @author wu
 *
 */
public abstract class PissHttpSession implements HttpSession{
	public static final String SESSION_TOKEN="token";
	private HttpServletRequest request;

	public PissHttpSession(HttpServletRequest request, String id, Integer timeout){
		super();
		this.request=request;
		this.id=id;
		setMaxInactiveInterval(timeout);
	}
	
	private long creationTime=0L;
	@Override
	public long getCreationTime() {
		return this.creationTime;
	}

	private String id;
	@Override
	public String getId() {
		return this.id;
	}

	private long lastAccessedTime=0L;
	@Override
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	@Override
	public ServletContext getServletContext() {
		return this.request.getSession().getServletContext();
	}
	
	/**
	 * 最大空闲时间
	 */
	private int maxInactiveInterval;
	@Override
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval=interval;
	}
	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return this.request.getSession().getSessionContext();
	}
	/**
	 * 初始化session信息
	 */
	public abstract void init();
	/**
	 * 设置session的过期时间
	 * @param timeout
	 */
	public abstract void setTTL(int timeout);
	/**
	 * 检查session是否存在
	 * @return
	 */
	public abstract boolean checkExists();
}
