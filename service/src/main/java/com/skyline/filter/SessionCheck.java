package com.skyline.filter;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyline.pojo.User;

public class SessionCheck implements Filter{
	Logger logger = LoggerFactory.getLogger(SessionCheck.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		logger.debug("************************important!!,we are getting shutdown****************************************");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		long start_time=System.currentTimeMillis();
		HttpServletRequest request =(HttpServletRequest) req;
		HttpServletResponse response =(HttpServletResponse) res;
		User login_user=(User)request.getSession().getAttribute("user");
		
/*		if(!request.getRequestURI().contains("login"))  //除了登录页面或者action 不拦截其他都拦截,这个也可以在filter 的init-param 中配置
			if(login_user==null){
				request.getSession().setMaxInactiveInterval(1200); //单位为秒,设置为20分钟后,会话超时
				response.sendRedirect(request.getContextPath()+"/login.jsp");
//				 chain.doFilter(request, response);
				 return; //重定向之后,用return中断之后的代码执行;然后当浏览器重新申请/login.jsp 的时候,就会跳过这里,不会进入这里执行return,所以可以通过这个拦截器
			}*/
		
		String thread_user=null;
		if(login_user!=null){
			thread_user= login_user.getUName();
		}
		String reqUrl=request.getRequestURL().toString();
		String ssId=request.getSession().getId();

		//		打印cookie 的详细信息
	/*	Cookie[] cookies=request.getCookies();
		Cookie cookie=null;
		if(cookies!=null){
			for(int i=0;i<cookies.length;i++){
				cookie=cookies[i];
				if(cookie!=null){
					logger.debug("cookie's name is [{}],value is [{}] and maxAge is [{}]",cookie.getName(),	cookie.getValue(),cookie.getMaxAge());

				}
			}
		}else{
			logger.debug("cookies is null");
		}
*/
		
		logger.info("total threads is [{}],peak thread number [{}]",ManagementFactory.getThreadMXBean().getThreadCount(),ManagementFactory.getThreadMXBean().getPeakThreadCount());
		logger.debug("from user [{}],the session id of requestUrl [{}] is [{}]",thread_user,reqUrl,ssId);

		String threadName=thread_user+"-"+request.getRequestURI()+"-"+Thread.currentThread().getId()+"g:"+Thread.currentThread().getThreadGroup();
		Thread.currentThread().setName(threadName);
		
		chain.doFilter(request, response);
	//	chain.doFilter(req, res);


		logger.debug("耗时:[{}]ms",System.currentTimeMillis()-start_time);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
