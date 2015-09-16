package com.skyline.schedule;



import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class DoSchedule extends ContextLoaderListener implements ServletContextListener{ 
	
	ReadWriteLock  lock = new ReentrantReadWriteLock();
	Lock rLock = lock.readLock();
	Lock wLock=lock.writeLock();

	

	
}
