package com.skyline.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skyline.pojo.ExcelTp;
import com.skyline.service.BaseService;
import com.skyline.util.HttpUpAndDownload;


@Controller
@RequestMapping("/excelTp")
public class ExcelTpAction {
	private Logger logger = LoggerFactory.getLogger(ExcelTpAction.class);	
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("getExcelTp.do")
	public void getExcelTp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		List<ExcelTp> tpList=baseService.getByClass(ExcelTp.class);
		req.setAttribute("tpList", tpList);
		
		req.getRequestDispatcher("/WEB-INF/jsp/rate/excelTp.jsp").forward(req, res);
	}
	
	@RequestMapping("importTp.do")
	public void importTp(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{

		Map<String, String> upload=null;
		try {
			upload = HttpUpAndDownload.getUploadInput(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			req.setAttribute("Message", "上传失败");
			req.getRequestDispatcher("getExcelTp.do").forward(req, res);
			return;
		}
		String filePathName=upload.get("upload");
		logger.debug("upload filepath is [{}]",filePathName);
		String fileName=filePathName.substring(filePathName.lastIndexOf(File.separator)+1, filePathName.lastIndexOf("."));
		System.out.println(filePathName);
		System.out.println(fileName);
		String download_path=filePathName.substring(filePathName.indexOf(req.getContextPath()));

		ExcelTp tp=new ExcelTp();
		tp.setName(fileName);
		tp.setFilePathName(filePathName);
		tp.setDownloadPath(download_path);
		try{
			baseService.saveOrReplaceIfDup(tp, "name"); //只要名字相同就覆盖
		}catch(Exception e){
			logger.error("", e);
			req.setAttribute("Message", "写入数据库失败");
			req.getRequestDispatcher("getExcelTp.do").forward(req, res);
			return;
		}
		req.setAttribute("Message", "done!");
		req.getRequestDispatcher("getExcelTp.do").forward(req, res);
	}
	
	@RequestMapping("downloadTp.do")
	public void downloadTp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		
		String id =req.getParameter("id");
		
		if(id!=null &&!"".equals(id)){
			ExcelTp tp=(ExcelTp)baseService.getById(ExcelTp.class, Integer.parseInt(id));
			try{
				HttpUpAndDownload.downLoadFile(tp.getFilePathName(), res);
			}catch(Exception e){
				logger.error("", e);
				req.setAttribute("Message", "下载失败");
				req.getRequestDispatcher("getExcelTp.do").forward(req, res);
			}
			
		}else{
			req.setAttribute("Message", "下载id正确");
			req.getRequestDispatcher("getExcelTp.do").forward(req, res);
		}
		
		
		
	}
	
}
