package com.skyline.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.predisw.util.DateFormatUtil;
import com.skyline.pojo.Params;
import com.skyline.service.LogService;
import com.skyline.util.HttpUpAndDownload;
import com.skyline.util.PageInfo;
import com.skyline.util.SingletonProps;
import com.skyline.util.SingletonPropsByEnum;

@Controller
@RequestMapping("/log")
public class LogAction {
	@Autowired
	private LogService logService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//----------跳到 系统日志页面
	@RequestMapping("getSysLog.do")
	public void getLog(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		//错误信息
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
		}
		//日志等级
		String fileName=req.getServletContext().getRealPath("/WEB-INF/classes")+File.separator+"logback.xml";
		Map<String,String> paramsMap=null;
		try {
			paramsMap= logService.getParamsMap(fileName);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			req.setAttribute("Message", e.getMessage()+" cause:"+e.getCause());
		}
		//显示下载列表
		SingletonProps sProps=SingletonProps.getInstance();
		Properties props = sProps.getProperties();
		
		List<String> list = logService.getLogFileName(props.getProperty("logDir"));
		Collections.sort(list,Collections.reverseOrder());
		
		
		
		req.setAttribute("logsList", list);
		req.setAttribute("params", paramsMap);
		req.getRequestDispatcher("/WEB-INF/jsp/log/sysLog.jsp").forward(req, res);
		
		
	}
	
	//---修改系统日志的级别
	@RequestMapping("updateLogSetting.do")
	public String updateLogSetting(HttpServletRequest req,Params Params,RedirectAttributes red){
		String fileName=req.getServletContext().getRealPath("/WEB-INF/classes")+File.separator+"logback.xml";
		try{
			logService.modifyXml(Params.getLogParams(), fileName);
			red.addFlashAttribute("Message", "success");
		}catch( Exception e){
			logger.error("", e);
			red.addFlashAttribute("Message", e.getMessage()+" cause:"+e.getCause());
			
		}
		
		return "redirect:getSysLog.do";
	}
	//-----下载系统日志文件
	@RequestMapping("downloadFile.do")
	public void  downloadFile(HttpServletRequest req,HttpServletResponse res,RedirectAttributes red) throws ServletException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{

		SingletonProps sProps=SingletonProps.getInstance();
		Properties props =sProps.getProperties();
		String logDir=props.getProperty("logDir");
		
		String name=req.getParameter("name");
		try {
			HttpUpAndDownload.downLoadFile(logDir+File.separator+name, res);
		} catch (IOException e) {
			logger.error("", e);
			req.setAttribute("Message", e.getMessage()+" cause:"+e.getCause());
			req.getRequestDispatcher("getSysLog.do").forward(req,res);
		}
	}
	
	//---跳到操作日志界面
	@RequestMapping("getOpLog.do")
	public void getOpLog(HttpServletRequest req,HttpServletResponse res	) throws ServletException, IOException{
		
		req.getRequestDispatcher("/WEB-INF/jsp/log/operationLog.jsp").forward(req, res);
		
	}
//----获取详细的操作日志并分页
	@RequestMapping("getPageLog.do")
	public void getPageLog(PageInfo page,HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, ParseException{
		if(page==null){
			page=new PageInfo();
		}
		
		String userName=req.getParameter("user");
		String from=req.getParameter("from");
		Date fDate=null;
		String to=req.getParameter("to");
		Date tDate=new Date();
		if(!"".equals(from) && from!=null){
			fDate=DateFormatUtil.parse(from,"yyyy-MM-dd");
		}
		
		if(!"".equals(to) && to!=null){
			tDate=DateFormatUtil.parse(to, "yyyy-MM-dd");
			tDate.setTime(tDate.getTime()+24*3600*1000);
		}

		Map<String, Object> cons=new HashMap<String, Object>();
		cons.put("fDate", fDate);
		cons.put("tDate",tDate );
		cons.put("userName", userName);
		
		page=logService.getPageOfOpLog(page, cons);
		
		req.setAttribute("page", page);
		//替换map 中的f/tDate 为字符串,因为jsp 中的Date 格式匹配问题
		cons.put("fDate", from);
		cons.put("tDate", to);
		req.setAttribute("cons", cons);
		
		req.getRequestDispatcher("getOpLog.do").forward(req, res);
		
		
	}
}
