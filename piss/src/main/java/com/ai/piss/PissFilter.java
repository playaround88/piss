package com.ai.piss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最前置filter， 替换HttpServletRequest
 * 
 * @author wu
 *
 */
public class PissFilter implements Filter {
	private Logger log = LoggerFactory.getLogger(PissFilter.class);
	// 超时时间单位秒s
	private int timeout = 120;
	private String pmClazz = "";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.trace("init PissFilter...");
		this.timeout = Integer.parseInt(filterConfig
				.getInitParameter("timeout"));
		this.pmClazz = filterConfig.getInitParameter("pmClazz");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		log.trace("wrap HttpRequest");
		PissHttpServletRequest rq = new PissHttpServletRequest(
				req, timeout, pmClazz);
		
		HttpSession session=rq.getSession(true);
		log.trace("add session cookie");
		Cookie ck=new Cookie(PissHttpSession.SESSION_TOKEN, session.getId());
		ck.setMaxAge(this.timeout);
		resp.addCookie(ck);
		
		log.trace("Replace HttpServletRequest with PissHttpServletRequest");
		chain.doFilter(rq, response);
	}

	@Override
	public void destroy() {
		log.trace("destory the PissFilter, nothing to do");
	}
}
