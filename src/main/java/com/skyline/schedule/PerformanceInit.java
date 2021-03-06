package com.skyline.schedule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.predisw.util.DateFormatUtil;
import com.skyline.dao.SysDao;
import com.skyline.util.SpringContextUtil;




public class PerformanceInit implements  Runnable {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	private SysDao sysDao=(SysDao) SpringContextUtil.getBean("sysDao"); 这里会获取到空指针异常

	private  String pathFileName="";

	
	public String getPathFileName() {
		return pathFileName;
	}


	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		SysDao sysDao=(SysDao) SpringContextUtil.getBean("sysDao");
		 
		logger.info("performance.log is initing");

		
		Date now = new Date();
		SimpleDateFormat sdf = DateFormatUtil.getSdf("yyyy-MM-dd");
		
		Path path =Paths.get(pathFileName);
		String performInfo="";

		
		if(Files.exists(path)){ //存在则检测date 的数据是否正确
			try {  
				performInfo= sysDao.readFileToString(path);
			} catch (IOException e) {
				e.printStackTrace();  //即使异常,也跳过,下面将会重新创建一个文件
			}
			
			JSONObject jsonObj=null;
			String date="";
			String pathName=path.toString();
			String targetName=pathName.substring(0,pathName.indexOf("."))+"_"+DateFormatUtil.format(now, "yyyy-MM-dd_HH-mm-ss")+".log";
			
			try{
				jsonObj= new JSONObject(performInfo);
				date=jsonObj.get("date").toString();
			}catch(JSONException e){

	//			e.printStackTrace();
				throw new RuntimeException("aa");
/*			try {
					sysDao.moveAndinit(path, Paths.get(targetName));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					

					
//					System.exit(-1); //如果最后创建失败,则退出虚拟机
				}
				
				return; //创建成功,则跳过下面的执行

*/
			}

			if(!sdf.format(now).equals(date)){ //假如时间不一致,则backup 旧的文件,同时创建新的.
				try {
					sysDao.moveAndinit(path, Paths.get(targetName));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-1); //如果最后创建失败,则退出虚拟机
				}
			}
			
			
		}else{  //如果不存在就创建
			
			try {
				sysDao.initPerformance(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1); //如果最后创建失败,则退出虚拟机
			}
		
		}
		logger.info("performance.log is done!!!!");
		
	}
		

	
	
}
