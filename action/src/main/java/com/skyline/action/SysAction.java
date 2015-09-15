package com.skyline.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

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
import com.skyline.pojo.Log;
import com.skyline.pojo.User;
import com.skyline.service.BaseService;

@Controller
@RequestMapping("/sys")
public class SysAction {
	@Autowired
	private BaseService baseService;
	
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
		Process process=Runtime.getRuntime().exec(cmd);
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
		
		
		return "forward:/WEB-INF/jsp/sys/performance.jsp";
	}
	
	
	//--------------------------------动态更新
	@RequestMapping("getPerformance.do")
	public void getPerformance(HttpServletRequest req,HttpServletResponse res) throws IOException{
		
		int threadNum=Thread.activeCount();
		
		String threadNums=req.getParameter("threadNums");
		threadNums.replace("[", "");
		threadNums.replace("]", "");
		String[] threadNumArr=threadNums.split(",");
		JSONArray threadNumArrj=new JSONArray(threadNums);

		
/*		for(int i=1;i<threadNumArr.length;i++){

			threadNumArrj.put(threadNumArr[i]);
			
			
		}*/
		if(threadNumArrj.length()>120){
			threadNumArrj.remove(0);
		}
		

		threadNumArrj.put(threadNum);
		

		logger.info("the current threadNum is [{}],线程组is [{}]",threadNum,Thread.currentThread().getThreadGroup());
		
		PrintWriter out =res.getWriter();
		
		logger.info("the threadNumArr is [{}]",threadNumArrj);
		out.print(threadNumArrj);
		
		
		
		
	}
	
	
	
	
	
	
	
	
}
