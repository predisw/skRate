package com.skyline.schedule;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimerTask;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.cfg.CreateKeySecondPass;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.skyline.dao.SysDao;
import com.skyline.dao.imple.SysDaoImple;
import com.skyline.util.SingletonProps;
import com.skyline.util.SpringContextUtil;

public class LogPerformance implements Runnable {

	private SysDao sysDao=(SysDao) SpringContextUtil.getBean("sysDao");
	
	 private String pathFileName="";
		
	 
	public String getPathFileName() {
		return pathFileName;
	}

	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
		Path path =Paths.get(pathFileName);
		sysDao.writeStringToFile(path, sysDao.createPerformance(sysDao.readFileToString(path)));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
