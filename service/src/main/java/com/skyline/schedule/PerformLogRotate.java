package com.skyline.schedule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import com.predisw.util.DateFormatUtil;
import com.skyline.util.SingletonProps;

public class PerformLogRotate extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long yestoday=(new Date()).getTime()-24*3600*1000; 
		String pathFileName="";
		String newFile = pathFileName.substring(0,pathFileName.indexOf("."))+DateFormatUtil.getSdf("yyyy-mm-DD").format(new Date(yestoday))+".log";
		try {
			pathFileName=SingletonProps.getInstance().getProperties().getProperty("performanceLog");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Path path =Paths.get(pathFileName);
		if(Files.exists(path)){
			try {
				Files.move(path, Paths.get(newFile),StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);
				Files.createFile(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
