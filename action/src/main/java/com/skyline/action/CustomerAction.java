package com.skyline.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.predisw.annotation.Description;
import com.predisw.exception.UniException;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Employee;
import com.skyline.pojo.Partner;
import com.skyline.pojo.Rate;
import com.skyline.service.BaseService;
import com.skyline.service.CustomerService;
import com.skyline.service.RateService;
import com.skyline.util.HttpUpAndDownload;
import com.skyline.util.PoiExcel;
import com.skyline.util.SingletonProps;

@Controller
@RequestMapping("/cus")
public class CustomerAction{
	private Logger logger = LoggerFactory.getLogger(CustomerAction.class);	
	
	@Autowired 
	private BaseService baseService;
	@Autowired
	private CustomerService cusService;

	
	
	//跳到客户管理页面
	@RequestMapping("toAddCus.do")
	public void toAddCust(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		List<Employee> empList=baseService.getByClass(Employee.class);
		req.setAttribute("empList", empList);
		
		req.getRequestDispatcher("/WEB-INF/jsp/customer/addCustomer.jsp").forward(req, res);

	}
	//添加客户
	@RequestMapping("addCus.do")
	public void addCus(Customer cus,HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		
		cus.setEmail(cus.getEmail().replace(" ", "")); //去掉所有的空格
		cus.setCcEmail(cus.getCcEmail().replace(" ", "")); //去掉所有的空格
		cus.setBccEmail(cus.getBccEmail().replace(" ", "")); //去掉所有的空格
		
		try{
			baseService.addUniCheck(cus, "vosId", cus.getVosId());
			baseService.save(cus);
			}
		catch(UniException e){
			req.setAttribute("Message", e.getMessage());
			e.printStackTrace();
			req.getRequestDispatcher("toAddCus.do").forward(req, res);
			return;
		}catch(Exception e){
			req.setAttribute("Message","添加失败");
			e.printStackTrace();
			req.getRequestDispatcher("toAddCus.do").forward(req, res);
			return;
		}
		req.setAttribute("Message", "添加成功");
		req.getRequestDispatcher("toAddCus.do").forward(req, res);
	}
	

	
	@RequestMapping("importCus.do")
	public void importCus(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		//---------------获取System.properties 中的属性--------
		//加载properties 文件的属性
		SingletonProps sProps=SingletonProps.getInstance();
		Properties props=sProps.getProperties();
		

		Map<String, String> uploadInput;

		String fileName="";
		try {
			uploadInput = HttpUpAndDownload.getUploadInput(req);
			fileName=uploadInput.get("upload");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			req.setAttribute("Message", "上传失败 "+e.getMessage()+"cause: "+e.getCause());
			req.getRequestDispatcher("toAddCus.do").forward(req, res);
			return;
		}
		String[] excel_sheetHead_order=props.getProperty("cus_import_excel").split(",");
		String[] cusAttributeOrder=props.getProperty("cus_dbtable_fields").split(",");
		
		try {
			cusService.saveExcelCusToDb(fileName, excel_sheetHead_order, cusAttributeOrder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			req.setAttribute("Message", "导入失败");
			req.getRequestDispatcher("toAddCus.do").forward(req, res);
			return;
		}
		

		req.setAttribute("Message", "Finished!");
		req.getRequestDispatcher("toAddCus.do").forward(req, res);

		
	}
	
	//-------------获取一个客户对象
	@RequestMapping("getCus.do")
	public void getCus(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		String cus_vosid=req.getParameter("cus_vosid");
		Customer cus=null;
		if(cus_vosid!=null ){
			List cusList=baseService.getByField(Customer.class, "vosId", cus_vosid);
			if(cusList.size()>0){ 
				cus=(Customer)cusList.get(0);
			}
		}
		req.setAttribute("vosId", cus_vosid);
		req.setAttribute("cus", cus);
		req.getRequestDispatcher("toAddCus.do").forward(req, res);
	}
	
//更新客户
	@RequestMapping("update.do")
	public void update(Customer cus,HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		
		cus.setEmail(cus.getEmail().replace(" ", "")); //去掉所有的空格
		cus.setCcEmail(cus.getCcEmail().replace(" ", "")); //去掉所有的空格
		cus.setBccEmail(cus.getBccEmail().replace(" ", "")); //去掉所有的空格
		
		try{
			baseService.updateUniCheck(cus,"vosId", cus.getVosId(),"CId");
			baseService.update(cus);
		}catch(UniException e){  //vosId 已存在
			e.printStackTrace();
			req.setAttribute("Message", e.getMessage());
			req.getRequestDispatcher("toAddCus.do").forward(req, res);
			return;
		}catch(Exception e){
			e.printStackTrace();
			req.setAttribute("Message", "更新失败: "+e.getLocalizedMessage());
			req.getRequestDispatcher("toAddCus.do").forward(req, res);
			return;
		}

		req.setAttribute("Message", "更新成功");
		req.getRequestDispatcher("toAddCus.do").forward(req, res);

	}
	
	
	//------------------------通过xmlrequest 获取选择的业务员所有客户的vosid---------
	@RequestMapping("getCusByAjax.do")
	public String getCusByAjax(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String eNum = req.getParameter("eNum"); //获取前台通过xmlRequest 对象传过来的
		List<Customer> cusListOfEmp=baseService.getByField(Customer.class, "ENum", eNum);
		req.setAttribute("cusList", cusListOfEmp);
		return "forward:responseCusListJson.do";

	}
	
	//获取某个客户某个格式的客户列表
	@RequestMapping("getTypeCustomersViaAjax.do")
	public String getTypeCustomersViaAjax(HttpServletRequest req,HttpServletResponse res){
		String eNum=req.getParameter("eNum");
		String type=req.getParameter("type");
		List<Customer> cusListOfEmp=cusService.getCustomersByType(eNum,Enum.valueOf(Partner.class, type));
		req.setAttribute("cusList", cusListOfEmp);
		return "forward:responseCusListJson.do";
	}
	
	//将客户list 集合转为json格式发送到前台
	@RequestMapping("responseCusListJson.do")
	public void responseCusListJson(HttpServletRequest req,HttpServletResponse res) throws IOException{
		List<Customer> cusList=(List<Customer>)req.getAttribute("cusList");
		JSONArray cusArr=new JSONArray(cusList.toArray());
		res.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
//		System.out.println(cusArr);
		out.print(cusArr);

	}
	
}
