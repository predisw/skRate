package com.skyline.schedule;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import lombok.Setter;

import com.skyline.util.SingletonProps;

@Component
public class DoSchedule  implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("init.... start");
		String pathFileName=SingletonProps.getInstance().getProperties().getProperty("performanceLog");
		
		ScheduledExecutorService scheduleService = Executors.newScheduledThreadPool(1);
	//初始化
		PerformanceInit performanceInit = new PerformanceInit();
		performanceInit.setPathFileName(pathFileName);
		scheduleService.schedule(performanceInit, 0, TimeUnit.MILLISECONDS);

		
		//常规任务之记录性能信息performance
		LogPerformance  logPerformance = new LogPerformance();
		logPerformance.setPathFileName(pathFileName);
		
		scheduleService.scheduleAtFixedRate(logPerformance, 0, 3000, TimeUnit.MILLISECONDS);

		//常规任务之每天rotate 日志文件

		System.out.println("init.... done");

	}
	

}
