package com.skyline.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.skyline.pojo.Onliner;
import com.skyline.pojo.User;

public class SessionListener implements HttpSessionListener {

	private final  Map<String, Onliner> onlinerMap = new HashMap<String, Onliner>();
	


	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		HttpSession ss =se.getSession();
		System.out.println("session is new ?"+ss.isNew());
		ServletContext application = ss.getServletContext();
		Onliner onliner = new Onliner();
		onliner.setLoginTime(new Date(ss.getCreationTime()));
		onlinerMap.put(ss.getId()	, onliner);
		
		application.setAttribute("onlinerMap", onlinerMap);
		
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		HttpSession ss =se.getSession();
		ServletContext application = ss.getServletContext();
		onlinerMap.remove(ss.getId());
		application.setAttribute("onlinerMap", onlinerMap);
	}

}
