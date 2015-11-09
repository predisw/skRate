package com.skyline.action;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorAction {
	private Logger logger = LoggerFactory.getLogger(ErrorAction.class);
	
	@RequestMapping("to404.do")
	public String to404(HttpServletRequest req,HttpServletResponse res){
		
		return "forward:/WEB-INF/jsp/error/404.html";
		
	}
	
	@RequestMapping("logJspExceptionToFile.do")
	public String  logJspExceptionToFile(HttpServletRequest req,HttpServletResponse res){
		Exception e =(Exception) req.getAttribute("javax.servlet.error.exception");
		logger.error("",e);

		
		return "forward:/WEB-INF/jsp/error/jspException.jsp";
	}
	
}
