package com.skyline.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.predisw.annotation.Description;
import com.predisw.annotation.Log;
import com.skyline.comparatorImple.CusComparator;
import com.skyline.comparatorImple.EmpComparator;
import com.skyline.pojo.CountryCode;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Employee;
import com.skyline.pojo.Partner;
import com.skyline.pojo.Props;
import com.skyline.pojo.RRate;
import com.skyline.pojo.Rate;
import com.skyline.service.BaseRateService;
import com.skyline.service.BaseService;
import com.skyline.service.CountryCodeService;
import com.skyline.service.LogService;
import com.skyline.service.RateService;
import com.skyline.util.HttpUpAndDownload;
import com.skyline.util.PageInfo;
import com.skyline.util.PoiExcel;
@RequestMapping("/cRate")
@Controller
public class CRateAction {
	@Autowired
	private BaseService baseService;
	@Autowired
	private RateService rateService;
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private BaseRateAction baseRateAction;
	@Autowired
	private BaseRateService baseRateService;
	@Autowired
	private LogService logService;
	
	//用于显示业务员,rate 的level 属性值
	@RequestMapping("getRateList.do")
	public void getRateList(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		List<Employee> empList=baseService.getByClass(Employee.class);
		List<Props> propList=baseService.getByField(Props.class, "name", "level");
		List<String> levelList=new ArrayList<>();
		for(int i=0;i<propList.size();i++){
			levelList.add(propList.get(i).getValue());
		}
		
		Collections.sort(empList, new EmpComparator());
		req.setAttribute("empList", empList);
		
		req.setAttribute("lList", levelList);
		
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
		}
		
		req.getRequestDispatcher("/WEB-INF/jsp/cRate/rateCode.jsp").forward(req, res);
	}
	

	//----------------------------------------客户发送报价的code 有哪些----
	//--------------------------通过vosid 获取属于这个vosid 的rate 有效(is_available)和发送成功(is_success)的rate 记录,
	@RequestMapping("getRateListOfEmp.do")
	public void getRateListOfEmp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		String vosId=req.getParameter("cus_vosid");
		List<Customer> cusList=baseService.getByField(Customer.class, "vosId",vosId);
		
		List<Rate> rateList=null;
		if(cusList.size()>0){
			Customer cus =cusList.get(0);
			rateList=rateService.getLastRateByCid(cus.getCId(), true, true,true);
		}

		req.setAttribute("vosId", vosId); //在html上显示选择的vosId
		req.setAttribute("rateList", rateList);
		req.getRequestDispatcher("getRateList.do").forward(req, res);
	}
	
	//---------------删除某个客户的code 记录.这个code 的记录不再作为报价发送.------
	//不是从数据库移除,而是 把is_available 设置为 false
	@RequestMapping("delCusRate.do")
	public void delCusRate(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		String[] ids=req.getParameterValues("rateId");
		String vosId=req.getParameter("cus_vosid");
		rateService.delCusRates(ids);
	
		 //传递到下一个action ,用于删除后回到相同的界面和数据
		req.getRequestDispatcher("getRateListOfEmp.do?cus_vosid="+vosId).forward(req, res);
		
	}
	
	
	//------------------导入rate记录----------
	@Log
	@RequestMapping("importRate.do")
	public void importRate(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, ParseException{

		Map<String,String> uploadInput;
		String eNum="";
		String vosid="";
		String fileName="";
		String level="";

		try {
			uploadInput=HttpUpAndDownload.getUploadInput(req);
			eNum=uploadInput.get("im_eNum");
			 vosid=uploadInput.get("im_vosid");
			fileName=uploadInput.get("upload");
			level=uploadInput.get("level");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			req.setAttribute("Message", "上传出错 "+e.getMessage()+" caused:"+e.getCause());
			req.getRequestDispatcher("getRateList.do").forward(req, res);
			return;
		}


		if("".equals(eNum) || "".equals(vosid)){
			req.setAttribute("Message", "请选择客户vosId 或者上传文件");
			req.getRequestDispatcher("getRateList.do").forward(req, res);
			return;
		}
		//通过vosId 获取客户 id
		int c_id=0;
		List cusList =baseService.getByField(Customer.class, "vosId", vosid);
		if(cusList!=null && !cusList.isEmpty()){
			Customer cus=(Customer)cusList.get(0);
			c_id=cus.getCId();
		}
		
		String[] tbIndex={"Destination","Name","Destination Code","Code","Rate","Billing Unit","Effective Date","Expire Date"};

		
		String[] hlist;
		Rate rate;
		List<Rate> rateList= new ArrayList<>();
		List list=null;
		try{
		list=poiExcel.readByPoi(fileName, 0, tbIndex);
		}catch(Exception e){
			e.printStackTrace();
			req.setAttribute("Message", "读取上传文件失败 :"+e.getMessage());
			req.getRequestDispatcher("getRateList.do").forward(req, res);
			return;
		}

		
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy"); 
		for(int i=0;i<list.size();i++){
			hlist=(String[])list.get(i);
			rate= new Rate();
			rate.setLevel(level);
			rate.setVosId(vosid);
			rate.setBakVosId(vosid);
			rate.setENum(eNum);
			rate.setCountry(hlist[0]);
			rate.setOperator(hlist[1]);
			if(hlist[3]==null){
				hlist[3]="";
			}
			rate.setCode(hlist[2]+hlist[3]);
			rate.setRate(Double.parseDouble(hlist[4]));
			rate.setBillingType(hlist[5]);
			rate.setSendTime(new Date());

			rate.setEffectiveTime(sdf.parse(hlist[6]));
			if(hlist[7]!=null && !"".equals(hlist[7])){
				rate.setEffectiveTime(sdf.parse(hlist[7],new ParsePosition(0)));
			}
			rate.setIsAvailable(true);
			rate.setIsSuccess(true);
			rate.setIsCorrect(true);
			rate.setCId(c_id);
			rate.setLevelPrefix("");  //设置prefix 为空字符串,如果不设置,则导出的leve 会是null 字符串,而不是留空
			
			rateList.add(rate);
		}
		
		try{
			baseService.saveBulk(rateList);
		}catch(Exception e){
			e.printStackTrace();
			req.setAttribute("Message", "导入失败 "+e.getMessage());
			req.getRequestDispatcher("getRateList.do").forward(req, res);
			return;
			
		}

		req.setAttribute("Message", "导入成功 ");
		
		StringBuffer content=new StringBuffer();
		content.append("工号:");
		content.append(eNum);
		content.append("-vosId:");
		content.append(vosid);
		content.append("-level:");
		content.append(level);
		
		logService.logToDb("导入", "客户报价", content.toString());
		
		res.sendRedirect(req.getContextPath()+"/cRate/getRateList.do");
		
		
	}
	
	//--------------从ISR 导入客户的历史报价记录
	@RequestMapping("importRateFromISR.do")
	public String importRateFromISR(HttpServletRequest req,HttpServletResponse res,RedirectAttributes red){
		
		String vosId="";
		String fileName="";
		String level="";
		String eNum="";
		try {
			Map<String,String> uploadInfo =HttpUpAndDownload.getUploadInput(req);
			fileName=uploadInfo.get("upload");
			vosId=uploadInfo.get("im_vosid");
			eNum=uploadInfo.get("im_eNum");
			level=uploadInfo.get("level");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			red.addFlashAttribute("Message", "上传失败 "+e.getMessage()+" "+e.getCause());
			return "redirect:getRateList.do";
		}
		
		
		int c_id=0;
		List cusList =baseService.getByField(Customer.class, "vosId", vosId);
		if(cusList!=null && !cusList.isEmpty()){
			Customer cus=(Customer)cusList.get(0);
			c_id=cus.getCId();
		}
		
		String[] excelHeaders={"CARRIERNAME","COUNTRYNAME","BREAKNAME","COUNTRYCODE","BREAKCODE","FIRSTUNIT","NEXTUNIT","FEERATE","EFFECTIVE DATE","EXPIRE DATE"};
		

		
		try{
			if(!baseRateService.checkExcel(fileName, excelHeaders, vosId)){
				red.addFlashAttribute("Message", "供应商vosId与文件内的vosId不一致 ");
				return "redirect:getRateList.do";
			}
			Rate rate= new Rate();
			rate.setLevel(level);
			rate.setENum(eNum);
			rate.setCId(c_id);
			rate.setLevelPrefix("");
			baseRateService.saveIsrExceltoDb(fileName, excelHeaders,rate);
		}catch(Exception e){
			e.printStackTrace();
			red.addFlashAttribute("Message", "写入数据失败 "+e.getMessage()+" "+e.getCause());
			return "redirect:getRateList.do";
		}
		
		red.addFlashAttribute("Message", "导入成功 ");
		

		return "redirect:getRateList.do";
	}
	
	
	
	//-----------下面的action 对应 rateRecord.jsp
	
	
	
	//--------------------------获取报价记录-----------------------
	@RequestMapping("getRateRecord.do")
	public void getRateRecord(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		
		req=baseRateAction.getBaseRatePageInfo(req, res);
		List<Customer> cusList=baseService.getByField(Customer.class,"CType",Partner.CUSTOMER);
		
		Collections.sort(cusList, new CusComparator());
		req.setAttribute("cusList", cusList);
		
		req.getRequestDispatcher("/WEB-INF/jsp/cRate/rateRecord.jsp").forward(req, res);
	}
	
	
	//---------------------获取某个客户的所有发送成功(is_success)的报价列表----------
	@RequestMapping("getSuccessRate.do")
	public void getSuccessRate(PageInfo page,HttpServletRequest req,HttpServletResponse res) throws ParseException, ServletException, IOException{
		req=baseRateAction.getSearchResult(page, req, res,Rate.class);
		
		req.getRequestDispatcher("getRateRecord.do").forward(req, res);
	}
	

	
}
