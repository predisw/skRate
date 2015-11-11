package com.skyline.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class PowersCheck implements Filter {
	
	private List<String> excludeUrls; 
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,	FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req =(HttpServletRequest) request;
		HttpServletResponse  res =(HttpServletResponse) response;
	
		String url =req.getRequestURI();   // example :"/SkylineRate/powers/toUserRole.do"
		String contextPath=req.getContextPath();   //example :"/SkylineRate"
		String pUrl=url.substring(contextPath.length(), url.lastIndexOf("."));
		
		if(excludeUrls!=null && excludeUrls.size()>0){
			if(excludeUrls.contains(pUrl)){
				chain.doFilter(req, res);
				return;  //必须,否则在推出这个filter 时会执行下面的代码
			}
		}

		List<String> noPUrls=(List<String>)req.getSession().getAttribute("noPUrls");
	
		if(noPUrls.contains(pUrl)){
			return;
		}
		
		chain.doFilter(req, res);
		
	}

	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
		String excludeUrl=filterConfig.getInitParameter("excludeUrl");
		if(StringUtils.isNotEmpty(excludeUrl)){
			excludeUrls=new ArrayList<>();
			for(String url:excludeUrl.split(",")){
				excludeUrls.add(url.substring(0,url.lastIndexOf(".")));
			}
			
		}
		
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
