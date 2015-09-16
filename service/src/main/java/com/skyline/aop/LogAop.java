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
import javax.servlet.http.HttpServletResponse;

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
import com.skyline.dao.LogDao;
import com.skyline.pojo.Log;
import com.skyline.pojo.User;
import com.skyline.service.BaseService;

@Controller
public class LogAop {
	Logger logger =LoggerFactory.getLogger(LogAop.class);
	
	@Autowired
	private LogDao logDao;
	

	

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
	


	public void logAfterReturn(JoinPoint jp) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		//先看方法上有没有描述注解,如果有则保存到数据库中,如果没有则不再执行

		Class[] parameterTypes = ((MethodSignature)jp.getSignature()).getMethod().getParameterTypes();
		Method method=jp.getTarget().getClass().getMethod(jp.getSignature().getName(), parameterTypes);
		String methodName="";

	
		if(method.isAnnotationPresent(Description.class)){
			methodName=method.getAnnotation(Description.class).name();
		}

		//不再往下执行
		if("".equals(methodName)){
			return;
		}
		
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		User user =(User)req.getSession().getAttribute("user");
		Date time=new Date();
		Object[] params=jp.getArgs();
		String entry="";
		StringBuffer content=new StringBuffer();

		//将所有参数放到一个字段中
		for(Object par:params){
			//----------
			if(par instanceof Collection<?>){ //参数为pojo 的实体类的集合
				Iterator<?> ite =((Collection) par).iterator();
				while(ite.hasNext()){
					Object obj = ite.next();
					if(obj.getClass().isAnnotationPresent(Description.class)){
						if("".equals(entry)){ //适用于只有一个实体类为参数的方法.
							entry=obj.getClass().getAnnotation(Description.class).name(); 
						}
						content.append(obj.getClass().getMethod("forLog").invoke(obj));
					}
					if(ite.hasNext()){
						content.append(",");
					}
				}
			}
			//--------------------
			else if(par.getClass().isAnnotationPresent(Description.class) ){  //参数为pojo 实体类
				if("".equals(entry)){ //适用于只有一个实体类为参数的方法.
						entry=par.getClass().getAnnotation(Description.class).name(); 
				}
					content.append(par.getClass().getMethod("forLog").invoke(par));
			}else if(par instanceof HttpServletRequest || par instanceof HttpServletResponse){
				//如果参数是request 或response ,则什么都不做
				continue; //并跳过最后添加分割符号部分
			}
			else{  //---------参数为普通类型
					content.append(String.valueOf(par));

			}

			if(params.length>1){
				content.append(",");
			}
		}

		Log log = new Log();
		log.setWho(user.getUName());
		log.setWhat(entry);
		log.setTime(time);
		log.setContent(content.toString());
		log.setHow(methodName);
		
		logDao.saveLog(log);


	}
	
	

}
