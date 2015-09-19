package com.skyline.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.predisw.util.DateFormatUtil;
import com.skyline.dao.SysDao;
import com.skyline.service.SysService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class PerformanceTest {
	@Autowired
	private SysDao sysDao;
	@Autowired
	private SysService sysService;
	@Test
	public void test() throws IOException, JSONException, ParseException{
		String pathFileName="/var/log/skyline/performance.log";
		Path path =Paths.get(pathFileName);
//	String jsonstr=sysDao.readFileToString(pathFileName);
//		sysDao.writePerformance(pathFileName, new JSONObject(jsonstr));
		
//	System.out.println(sysService.getPerformance(pathFileName, 30));
//	/.out.println(sysService.getCurrentPerformance());
		
//		sysDao.CreatePerformance(path);
	}
}
