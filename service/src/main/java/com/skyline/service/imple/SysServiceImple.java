package com.skyline.service.imple;


import java.io.IOException;
import java.lang.management.ManagementFactory;
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
	public String  getPerformance(String pathFileName,int arrLength) throws IOException, JSONException, ParseException{
		Date now = new Date();
		long date=now.getTime()-24*3600*1000;
		SimpleDateFormat sdf= DateFormatUtil.getSdf("HH:mm:SS");
		
		String json_str = sysDao.readFileToString(pathFileName);
		JSONObject json = new JSONObject(json_str);
		JSONArray jArrs = json.getJSONArray("threadNums");
		

		
//		System.out.println("before"+jArrs.toString());
		int i=0;
		if(arrLength<jArrs.length())
			i=jArrs.length()-arrLength;
		for(;i<jArrs.length();i++){
				JSONArray jArr =  jArrs.getJSONArray(i);
				jArr.put(0, date+(sdf.parse((String) jArr.get(0))).getTime() );
				
		}
		
		if(arrLength>jArrs.length()){
			JSONArray nArrs = new JSONArray();
			for(int j=arrLength;j>jArrs.length();j--){
				JSONArray jArr =  new JSONArray();
				jArr.put(0, now.getTime()-j*(3600/arrLength)*1000);
				jArr.put(1, 0);
				nArrs.put(jArr);
			}
			
			for(Object obj:jArrs){
				nArrs.put(obj);
			}
			return nArrs.toString();
		}

		
		return jArrs.toString();
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
