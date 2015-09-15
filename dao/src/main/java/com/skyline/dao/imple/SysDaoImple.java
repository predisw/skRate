package com.skyline.dao.imple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.predisw.util.DateFormatUtil;
import com.skyline.dao.SysDao;

@Repository("sysDao")
public class SysDaoImple implements SysDao {
	@Override
	public String readFileToString(String pathFileName) throws IOException {
		// TODO Auto-generated method stub
		
		Path path=Paths.get(pathFileName);
		BufferedReader fileReader=Files.newBufferedReader(path, Charset.forName("utf-8"));
		String performance="";
		int size=1024;
		char[] cbuf=new char[size];
		while(fileReader.read(cbuf, 0, size) != -1){
			performance+=new String(cbuf);
	//		System.out.println(performance);
		}
		fileReader.close();
		return performance;
	}

	@Override
	public void writePerformance(String pathFileName, JSONObject json)
			throws IOException {
		// TODO Auto-generated method stub
		Date now =new Date();
		String date =DateFormatUtil.format(now, "yyyy:MM:dd");
		String time =DateFormatUtil.format(now, "HH:mm:SS");
		int threadNum=ManagementFactory.getThreadMXBean().getThreadCount();
		Path path=Paths.get(pathFileName);
		
		JSONArray jArr = json.getJSONArray("threadNums");
		
		JSONArray newArr=new JSONArray();
		
		newArr.put(time);
		newArr.put(threadNum);
		
		
		jArr.put(newArr);
		
//		System.out.println(json.toString());

		
		try(BufferedWriter fileWriter = Files.newBufferedWriter(path,Charset.forName("utf-8"));){
			
			fileWriter.write(json.toString());
			fileWriter.flush();
		}
	}
}
