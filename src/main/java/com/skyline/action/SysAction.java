package com.skyline.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.predisw.annotation.Description;
import com.predisw.util.DateFormatUtil;
import com.skyline.pojo.Log;
import com.skyline.pojo.User;
import com.skyline.service.BaseService;
import com.skyline.service.SysService;
import com.skyline.util.SingletonProps;

@Controller
@RequestMapping("/sys")
public class SysAction {
	@Autowired
	private BaseService baseService;
	@Autowired
	private SysService sysService;
	
	Logger logger=LoggerFactory.getLogger(this.getClass());
	
	
	@RequestMapping("getVerInfo.do")
	public void getVerInfo(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		req.getRequestDispatcher("/WEB-INF/jsp/sys/version.jsp").forward(req, res);
	}

	@RequestMapping("toSysOperation.do")
	public void toSysOperation(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		
		req.getRequestDispatcher("/WEB-INF/jsp/sys/sysOperation.jsp").forward(req, res);
	}
	
	
	@com.predisw.annotation.Log
	@Description(name="重启应用服务")
	@RequestMapping("reload.do")
	public String reload(HttpServletRequest req,HttpServletResponse res) throws IOException{
		
		String cmd="curl --user predisw:admin http://localhost:8080/manager/text/reload?path=/SkylineRate";
		Process process=null;
		try{
			process=Runtime.getRuntime().exec(cmd);
		}catch(Exception e){
			logger.error("", e);
			req.setAttribute("Message", "失败"+e.getMessage());
			return "forward:toSysOperation.do";
		}

		
		java.util.Scanner in= new java.util.Scanner(process.getInputStream());
		
		while(in.hasNextLine()){
			String Message=in.nextLine();
			logger.warn("reload response is [{}]",Message);
			if(Message.contains("OK")){
				req.setAttribute("Message", "Reload Successfully");

			}
			
			if(Message.contains("FAIL")){
				req.setAttribute("Message", "Reload Failed !!");

			}
		}
		

		return "forward:toSysOperation.do";
	}
	
	//---------------------跳到performance.jsp页面
	@RequestMapping("toPerformance.do")
	public String toPerformance(HttpServletRequest req,HttpServletResponse res){
		
		
		
		String threadNums="";
		try{
			Properties props=SingletonProps.getInstance().getProperties();
			String pathFileName=props.getProperty("performanceLogDir")+props.getProperty("performanceLogName");
			
			threadNums=sysService.getPerformance(Paths.get(pathFileName), 5);
		}catch(Exception e){
			logger.error("", e);
			req.setAttribute("Message", "失败 "+e.getMessage());
			return "forward:/WEB-INF/jsp/sys/performance.jsp";
		}
		
		System.out.println(threadNums);
		req.setAttribute("threadNums", threadNums);
		return "forward:/WEB-INF/jsp/sys/performance.jsp";
	}
	
	
	//--------------------------------动态更新
	@RequestMapping("getPerformance.do")
	public void getPerformance(HttpServletRequest req,HttpServletResponse res) throws IOException{
		
	//	String fileName="performance.log";
		
		String json=sysService.getCurrentPerformance();
		
		PrintWriter out = res.getWriter();
//		System.out.println(json);
		out.print(json);
				
		
		
	
		
	}
	

	
}
