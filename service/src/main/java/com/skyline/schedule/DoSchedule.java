package com.skyline.schedule;

import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import lombok.Setter;

import com.skyline.util.SingletonProps;

@Component
public class DoSchedule  implements InitializingBean ,DisposableBean{
	Logger logger = LoggerFactory.getLogger(this.getClass());

	ScheduledExecutorService scheduleService = Executors.newScheduledThreadPool(1);

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
//		Thread.setDefaultUncaughtExceptionHandler(new DefaultUnCatchException());
		Properties props=SingletonProps.getInstance().getProperties();
		String pathFileName=props.getProperty("performanceLogDir")+props.getProperty("performanceLogName");

		
	//初始化
		PerformanceInit performanceInit = new PerformanceInit();
		performanceInit.setPathFileName(pathFileName);
	
		scheduleService.schedule(performanceInit, 0, TimeUnit.MILLISECONDS);
//		new Thread(performanceInit).start();

		
		//常规任务之记录性能信息performance
		LogPerformance  logPerformance = new LogPerformance();
		logPerformance.setPathFileName(pathFileName);
		
		scheduleService.scheduleAtFixedRate(logPerformance, 0, 3000, TimeUnit.MILLISECONDS);

		//常规任务之每天rotate 日志文件
		logger.info("doschedule has started ,recording  performance has started!");

	//	throw new RuntimeException("哈哈,挂吧");
		
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		scheduleService.shutdown();
		logger.info("doschedule has stop ,recording  performance has stop!");
	}


}
