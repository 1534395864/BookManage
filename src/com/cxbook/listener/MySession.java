package com.cxbook.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class MySession
 *
 */
@WebListener
public class MySession implements HttpSessionListener {
	private SessionMap map = SessionMap.getinstance();


	/**
	 * Default constructor.
	 */
	public MySession() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) {
		map.addSession(arg0.getSession());
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		map.deleSession(arg0.getSession());
	}


}
