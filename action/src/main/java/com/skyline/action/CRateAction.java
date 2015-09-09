package com.skyline.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skyline.pojo.CountryCode;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Employee;
import com.skyline.pojo.Partner;
import com.skyline.pojo.Props;
import com.skyline.pojo.Rate;
import com.skyline.service.BaseService;
import com.skyline.service.CountryCodeService;
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
	
	
	//用于显示业务员,rate 的level 属性值
	@RequestMapping("getRateList.do")
	public void getRateList(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		List<Employee> empList=baseService.getByClass(Employee.class);
		List<Props> propList=baseService.getByField(Props.class, "name", "level");
		List<String> levelList=new ArrayList<>();
		for(int i=0;i<propList.size();i++){
			levelList.add(propList.get(i).getValue());
		}
		req.setAttribute("empList", empList);
		req.setAttribute("lList", levelList);
		
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
		if(ids!=null){
			for(int i=0;i<ids.length;i++){
				Rate rate=(Rate)baseService.getById(Rate.class,Integer.parseInt(ids[i]));
				rateService.setIsAvailable(rate.getVosId(), rate.getCode(), false);
			}
		}
	
		 //传递到下一个action ,用于删除后回到相同的界面和数据
		req.getRequestDispatcher("getRateListOfEmp.do?cus_vosid="+vosId).forward(req, res);
		
	}
	
	
	//------------------导入rate记录----------
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
			req.setAttribute("uploadError", "上传出错 "+e.getMessage()+" caused:"+e.getCause());
			req.getRequestDispatcher("getRateList.do").forward(req, res);
			return;
		}


		if("".equals(eNum) || "".equals(vosid)){
			req.setAttribute("uploadError", "请选择客户vosId 或者上传文件");
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
		
		String[] tbIndex={"Destination","Name","Destination Code","Code","Rate","Billing Unit","Effective Date"};
		String[] hlist;
		Rate rate;
		List<Rate> rateList= new ArrayList<>();
		List list=poiExcel.readByPoi(fileName, 0, tbIndex);
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
			req.setAttribute("uploadError", "导入失败 "+e.getMessage());
			req.getRequestDispatcher("getRateList.do").forward(req, res);
			return;
			
		}

		
		res.sendRedirect(req.getContextPath()+"/cRate/getRateList.do");
		
		
	}
	
	
	//-----------下面的action 对应 rateRecord.jsp
	
	
	
	//--------------------------获取报价记录-----------------------
	@RequestMapping("getRateRecord.do")
	public void getRateRecord(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		
		req=baseRateAction.getBaseRatePageInfo(req, res);
		List<Customer> cusList=baseService.getByField(Customer.class,"CType",Partner.CUSTOMER);
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
