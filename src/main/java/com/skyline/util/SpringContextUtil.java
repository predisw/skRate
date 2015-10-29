package com.skyline.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

@Repository("springContextUtil")
public class SpringContextUtil implements ApplicationContextAware{

	private static ApplicationContext context;
  
	@Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }

    public static Object getBean(String beanName) {
    	
        return context.getBean(beanName);
    }


}
