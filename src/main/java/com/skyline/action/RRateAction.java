package com.skyline.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.predisw.annotation.Log;
import com.skyline.comparatorImple.CusComparator;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Employee;
import com.skyline.pojo.Partner;
import com.skyline.pojo.Props;
import com.skyline.pojo.RRate;
import com.skyline.service.BaseRateService;
import com.skyline.service.BaseService;
import com.skyline.service.LogService;
import com.skyline.service.RRateService;
import com.skyline.util.HttpUpAndDownload;
import com.skyline.util.PageInfo;

@RequestMapping("/rRate")
@Controller
public class RRateAction {
	@Autowired
	private BaseService baseService;
	@Autowired
	private RRateService rRateService;
	@Autowired
	private BaseRateAction baseRateAction;
	@Autowired
	private BaseRateService baseRateService;
	@Autowired
	private LogService logService;
	
	private Logger logger= LoggerFactory.getLogger(RRateAction.class);
	
	
	//---------------------跳转到界面
	@RequestMapping("toAddRRate.do")
	public String toAddRRate(HttpServletRequest req,HttpServletResponse res){
	
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
		}
		
		List<Customer> cusList=baseService.getByField(Customer.class, "CType", Partner.PROVIDER);

		Collections.sort(cusList, new CusComparator());
		req.setAttribute("cusList", cusList);


		return "forward:/WEB-INF/jsp/rRate/addRRate.jsp";
	}
	

	@Log
	@RequestMapping("importRRate.do")
	public String importRRate(HttpServletRequest req,HttpServletResponse res,RedirectAttributes red){
		
		String vosId="";
		String fileName="";
		try {
			Map<String,String> uploadInfo =HttpUpAndDownload.getUploadInput(req);
			fileName=uploadInfo.get("upload");
			vosId=uploadInfo.get("vosId");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			red.addFlashAttribute("Message", "上传失败 "+e.getMessage()+" "+e.getCause());
			return "redirect:toAddRRate.do";
		}
		
		String[] excelHeaders={"CARRIERNAME","COUNTRYNAME","BREAKNAME","COUNTRYCODE","BREAKCODE","FIRSTUNIT","NEXTUNIT","FEERATE","EFFECTIVE DATE","EXPIRE DATE"};
		

		
		try{
			if(!baseRateService.checkExcel(fileName, excelHeaders, vosId)){
				red.addFlashAttribute("Message", "供应商vosId与文件内的vosId不一致 ");
				return "redirect:toAddRRate.do";
			}
			baseRateService.saveIsrExceltoDb(fileName, excelHeaders,new RRate());
		}catch(Exception e){
			logger.error("", e);
			red.addFlashAttribute("Message", "写入数据失败 "+e.getMessage()+" "+e.getCause());
			return "redirect:toAddRRate.do";
		}
		
		red.addFlashAttribute("Message", "导入成功 ");
		
		StringBuffer content=new StringBuffer();
		content.append("文件名:");
		content.append(fileName.substring(fileName.lastIndexOf(File.separator)+1));
		content.append("-vosId:");
		content.append(vosId);

		try{
			logService.logToDb("导入", "落地报价", content.toString());
		}catch(Exception e){
			logger.error("记录日志失败",e);
		}
		
		return "redirect:toAddRRate.do";
	}
	
	
	@RequestMapping("toRRateRecord.do")
	public String toRRateRecord(HttpServletRequest req,HttpServletResponse res) {
		
		req=baseRateAction.getBaseRatePageInfo(req, res);
		
		List<Customer> cusList=baseService.getByField(Customer.class, "CType", Partner.PROVIDER);
		//获取可选的name 为 billing_unit属性的对象,对象类似一个map
		
		Collections.sort(cusList, new CusComparator());
		List<Props> bList=baseService.getByField(Props.class, "name", "billing_unit");
		
		req.setAttribute("cusList", cusList);
		req.setAttribute("bList", bList);

		return "forward:/WEB-INF/jsp/rRate/RRateRecord.jsp";
	}
	
	
	@RequestMapping("getRRateRecord.do")
	public String getSuccessRate(PageInfo page,HttpServletRequest req,HttpServletResponse res) throws ParseException, ServletException, IOException{
		
		req=baseRateAction.getSearchResult(page, req, res,RRate.class);
		
		return "forward:toRRateRecord.do";
	}
	
	
	
	
	@RequestMapping("modifyrRate.do")
	public void modifyrRate(HttpServletRequest req,HttpServletResponse res) throws IOException{
		
		String rRates_str=req.getParameter("rRates");
//		System.out.println(rRates_str);
		JSONArray jsonArr=new JSONArray(rRates_str);
		
		res.setCharacterEncoding("UTF-8");
		PrintWriter out =res.getWriter();
		
		JSONObject result=new JSONObject();

		try{
			rRateService.saveJsonToDb(jsonArr); 
		}catch(Exception e){
			logger.error("", e);
			result.put("Message", "修改失败 "+e.getMessage()+" cause: "+e.getCause());
			out.print(result);
			return;
		}

		result.put("Message", "修改成功");
		out.print(result);
		

	}
	
	@RequestMapping("delrRates.do")
	public void delrRates(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String ids=req.getParameter("ids");
		String[] idArr = ids.split(",");

		int[] intIdArr=new int[idArr.length];
	
		for(int i=0;i<idArr.length;i++){
			intIdArr[i]=Integer.valueOf(idArr[i]);
		}
		
		String Message="";
		try{
			baseService.delBulkByids(RRate.class, intIdArr);
			Message="删除成功";

		}catch(Exception e){
			logger.error("", e);;
			Message="失败:"+e.getLocalizedMessage()+" cause:"+e.getCause();
		}

		res.setCharacterEncoding("UTF-8");
		PrintWriter out =res.getWriter();
		
		out.write(Message);
	}
	
	
	
}
