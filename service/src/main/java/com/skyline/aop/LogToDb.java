package com.skyline.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.predisw.annotation.Description;
import com.skyline.dao.BaseDao;
import com.skyline.pojo.Log;
import com.skyline.pojo.User;


@Service
public class LogToDb {
	@Autowired
	private BaseDao baseDao;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Pointcut("within( com.skyline.action.*.*) &&  @annotation(com.predisw.annotation.Description)")
//	public void pointCut(){}
	
	
		

//	@AfterReturning(pointcut="pointCut()")
	
}
