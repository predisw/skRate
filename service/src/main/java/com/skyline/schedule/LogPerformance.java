package com.skyline.schedule;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.TimerTask;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.skyline.dao.SysDao;
import com.skyline.util.SingletonProps;

public class LogPerformance extends TimerTask {
	@Autowired
	private SysDao sysDao;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		try {
		String pathFileName=SingletonProps.getInstance().getProperties().getProperty("performanceLog");
		sysDao.writePerformance(pathFileName, new JSONObject(sysDao.readFileToString(pathFileName)));	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
