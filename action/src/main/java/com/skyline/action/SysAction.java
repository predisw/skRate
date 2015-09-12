package com.skyline.action;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		/*
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
		}*/
		
/*		//记录操作日志
		User user=(User)req.getSession(false).getAttribute("user");
		Log log = new Log();
		log.setENum(user.getUName());
		log.setLType("重启");
		log.setLTime(new Date());
		baseService.save(log);
		*/
		return "forward:toSysOperation.do";
	}
}
