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
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.predisw.util.DateFormatUtil;
import com.skyline.dao.SysDao;

@Repository("sysDao")
public class SysDaoImple implements SysDao {
	
	ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
	Lock rLock=lock.readLock();
	Lock wLock=lock.writeLock();
	
	//加读锁
	@Override
	public String readFileToString(Path path) throws IOException {
		// TODO Auto-generated method stub
		String performance="";
		try{
			rLock.lock();

			BufferedReader fileReader=Files.newBufferedReader(path, Charset.forName("utf-8"));
			int size=1024;
			char[] cbuf=new char[size];
			while(fileReader.read(cbuf, 0, size) != -1){
				performance+=new String(cbuf);
			}
			fileReader.close();
		}finally{
			rLock.unlock();
		}
		
		return performance;
	}

	//加读锁
	@Override
	public void writeStringToFile(Path path, String  content)
			throws IOException {
		// TODO Auto-generated method stub

			try{
				rLock.lock();
				try(BufferedWriter fileWriter = Files.newBufferedWriter(path,Charset.forName("utf-8"));){
				
				fileWriter.write(content);
				fileWriter.flush();
			}
		}finally{
			rLock.unlock();
		}

	}
	
	//加写锁
	@Override
	public void moveAndinit(Path source,Path target) throws IOException{
		try{
			wLock.lock();
			Files.move(source, target,StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);
			initPerformance(source); //重入读锁
		}finally{
			wLock.unlock();
		}
	}
	
	@Override
	public void initPerformance(Path path) throws IOException{
		Date now = new Date();
		SimpleDateFormat sdf = DateFormatUtil.getSdf("yyyy-MM-dd");
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("date", sdf.format(now));
		JSONArray jsonArr=new JSONArray();
		jsonObj.put("threadNums", jsonArr);
		jsonObj.put("cpu", jsonArr);
		jsonObj.put("memory", jsonArr);


		Files.createFile(path);
		writeStringToFile(path, jsonObj.toString());
		
		
	}

	@Override
	public String  createPerformance(String content) throws IOException{
		
		JSONObject jsonObj=new JSONObject(content);
		
	
		Date now =new Date();
		String time =DateFormatUtil.format(now, "HH:mm:ss");
		int threadNum=ManagementFactory.getThreadMXBean().getThreadCount();
		JSONArray threadNumArrS = jsonObj.getJSONArray("threadNums");
		JSONArray threadNumArr=new JSONArray();
		threadNumArr.put(time);
		threadNumArr.put(threadNum);

		threadNumArrS.put(threadNumArr);
		

		return jsonObj.toString();
	}
	
	
	
}
