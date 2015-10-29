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

import lombok.Getter;
import lombok.Setter;

import com.predisw.util.DateFormatUtil;
import com.skyline.util.SingletonProps;

public class PerformLogRotate implements Runnable {
	

	public String getPathFileName() {
		return pathFileName;
	}

	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}

	public String pathFileName="";
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	String performInfo="";
	Path path=Paths.get(pathFileName);
	
	
	
		
	
	}

}
