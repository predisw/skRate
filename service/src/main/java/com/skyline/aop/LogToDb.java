package com.skyline.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.predisw.annotation.Description;
import com.skyline.dao.BaseDao;
import com.skyline.pojo.Log;
import com.skyline.pojo.User;

@Aspect
@Controller
public class LogToDb {
	@Autowired
	private BaseDao baseDao;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Pointcut("execution(* com.skyline..*(..) )")
	public void pointCut(){}
	
	
	
	

	@AfterReturning(pointcut="pointCut()")
	public void logAfterReturn(JoinPoint jp) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		//先看方法上有没有描述注解,如果有则保存到数据库中,如果没有则不再执行
		Class[] parameterTypes = ((MethodSignature)jp.getSignature()).getMethod().getParameterTypes();
		Method method=jp.getTarget().getClass().getMethod(jp.getSignature().getName(), parameterTypes);
		String methodName="";

		logger.debug("abccccccccccccccccccccccccccccccc");
		
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
		baseDao.save(log);


	}
}
