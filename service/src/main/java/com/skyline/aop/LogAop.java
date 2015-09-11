package com.skyline.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.predisw.annotation.Description;
import com.skyline.dao.BaseDao;
import com.skyline.pojo.Log;
import com.skyline.pojo.User;
import com.skyline.service.BaseService;

@Controller
@Aspect
public class LogAop {
	Logger logger =LoggerFactory.getLogger(LogAop.class);
	
	@Autowired
	private BaseDao baseDao;
	
	@Pointcut("execution(* com.skyline.dao.*+.*(..))" )
	public void pointCut(){}
	
	
	@Before("pointCut()")
	public void doBefore(JoinPoint jp)  { //不能在参数中直接添加 HttpServletRequest req

		Object[] params=jp.getArgs();  //在方法的参数中params[i] 有可能是request 对象.可以通过这里的getClass 获取HttpServletRequest 对象,再获取session
		StringBuffer paramsStr=new StringBuffer();
		for(Object par:params){
			paramsStr.append(par);
			if(params.length>1){
				paramsStr.append(",");
			}
		}
		String class_method=jp.getTarget().getClass().getName()+"."+jp.getSignature().getName();
		logger.debug(class_method+"开始执行");
		logger.debug("params:"+paramsStr.toString());


		
		
/*		Signature sig=jp.getSignature();
		String classinfo=sig.getDeclaringType()+" 类型标识符"+Modifier.toString(sig.getModifiers())+" getClass:"+sig.getClass();
		logger.debug(classinfo);*/
	}
	
	//传入切入点和 切入点处方法的返回结果对象returnObj
	@AfterReturning(pointcut="pointCut()",returning="returnObj")
	public void doAfterReturn(JoinPoint jp,Object returnObj ){
		if(returnObj!=null){
			if(returnObj.getClass().getName().contains("List")){
				logger.debug("方法[{}],返回类型是 List,长度是{}",jp.getTarget().getClass().getSimpleName()+"."+jp.getSignature().getName(),((List)(returnObj)).size());
			}else{
				logger.debug("方法[{}],返回[{}]",jp.getTarget().getClass().getSimpleName()+"."+jp.getSignature().getName(),returnObj.toString());
			}
		}else{
			logger.debug("方法[{}],返回[{}]",jp.getTarget().getClass().getSimpleName()+"."+jp.getSignature().getName(),returnObj);
		}
		
	}
	
	@After("pointCut()")
	public void doAfter(JoinPoint jp){
		String class_method=jp.getTarget().getClass().getName()+"."+jp.getSignature().getName();
		logger.debug(class_method+"结束");
/*		Object[] params=jp.getArgs();
		StringBuffer paramsStr=new StringBuffer();
		for(Object par:params){
			paramsStr.append(par+",");
		}
		logger.debug("参数: "+paramsStr.toString());
		*/
	}
	

	
	
	
	
	
	

}
