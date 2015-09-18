package com.skyline.service.imple;


import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.util.DateFormatUtil;
import com.skyline.dao.SysDao;
import com.skyline.service.SysService;
@Service
public class SysServiceImple implements SysService {
	@Autowired
	private  SysDao sysDao;
	
	@Override
	public String  getPerformance(Path path,int arrLength) throws IOException, JSONException, ParseException{
		Date now = new Date();

		SimpleDateFormat sdf= DateFormatUtil.getSdf("yyyy-MM-dd HH:mm:SS");
		
		String json_str = sysDao.readFileToString(path);
		if("".equals(json_str))json_str="{}";
		
		JSONObject json = new JSONObject(json_str);	
		String  date= json.getString("date");
		JSONArray jArrs = json.getJSONArray("threadNums");

		JSONArray nArrs = new JSONArray();
		
//		System.out.println("before"+jArrs.toString());
		int i=0;
		if(arrLength<jArrs.length())	i=jArrs.length()-arrLength;
		
		for(;i<jArrs.length();i++){
				JSONArray jArr =  jArrs.getJSONArray(i);
				jArr.put(0, sdf.parse(date+" "+jArr.get(0)).getTime() );
				nArrs.put(jArr);
		}
		
		if(arrLength>jArrs.length()){

			for(int j=arrLength;j>jArrs.length();j--){
				JSONArray jArr =  new JSONArray();
				jArr.put(0, now.getTime()-j*(3600/arrLength)*1000);  //j 是越来越小
				jArr.put(1, 0);
				nArrs.put(jArr);
			}
			
			for(Object obj:jArrs){
				nArrs.put(obj);
			}

		}
		
		return nArrs.toString();
	}

	@Override
	public 	String getCurrentPerformance(){
		
		Date now =new Date();
		int threadNum= ManagementFactory.getThreadMXBean().getThreadCount();
		int peakThreadNum=ManagementFactory.getThreadMXBean().getPeakThreadCount();
		JSONArray arr = new JSONArray();
		arr.put(now.getTime());
		arr.put(threadNum);
		
		JSONObject obj=new JSONObject();
		obj.put("peakThreadNum", peakThreadNum);
		obj.put("currentThreadNum", arr);

		return obj.toString();
	}


	
	
	
}
