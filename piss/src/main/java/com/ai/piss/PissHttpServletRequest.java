package com.ai.piss;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author wu
 *
 */
public class PissHttpServletRequest  extends HttpServletRequestWrapper{
	private static Logger log=LoggerFactory.getLogger(PissHttpServletRequest.class);
	private int timeout;
	private String pmClazz;
	private HttpSession session=null;

	public PissHttpServletRequest(HttpServletRequest request, int timeout, String pmClazz){
		super(request);
		this.timeout=timeout;
		this.pmClazz=pmClazz;
	}

	@Override
	public HttpSession getSession(boolean create) {
		if(this.session!=null){
			log.trace("reset session timeout");
			PissHttpSession ps=(PissHttpSession)session;
			ps.setTTL(timeout);
			return session;
		}
		HttpServletRequest req=(HttpServletRequest)getRequest();
		
		log.trace("justify request parameter contains SESSION_TOKEN?");
		String token=req.getParameter(PissHttpSession.SESSION_TOKEN);
		if(token==null || "".equals(token)){
			log.trace("request not contains SESSION_TOKEN");
			log.trace("justify cookies contains SESSION_TOKEN?");
			Cookie[] cookies=req.getCookies();
			for(int i=0;i<cookies.length;i++){
				Cookie ck=cookies[i];
				if( PissHttpSession.SESSION_TOKEN.equals(ck.getName()) ){
					token=ck.getValue();
					break;
				}
			}
		}
		
		if(token!=null && !"".equals(token)){
			log.debug("session maybe exists, request token:{}",token);
			this.session=createSession(token);
			PissHttpSession ps=(PissHttpSession)this.session;
			if(ps.checkExists()){
				//初始化
				ps.init();
				return this.session;
			}else{
				throw new RuntimeException("token 无效");
			}
		}
		
		log.trace("session is not exists!");
		if(create){
			token=UUID.randomUUID().toString().replace("-", "");
			this.session=createSession(token);
			//初始化
			PissHttpSession ps=(PissHttpSession)this.session;
			ps.init();
			return this.session;
		}else{
			return null;
		}
	}
	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	private PissHttpSession createSession(String token) {
		log.trace("use reflect to create session class");
		try {
			Class c=Class.forName(pmClazz);
			Constructor cst=c.getConstructor(HttpServletRequest.class,String.class,Integer.class);
			PissHttpSession session=(PissHttpSession)cst.newInstance(getRequest(), token,this.timeout);
			
			return session;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
