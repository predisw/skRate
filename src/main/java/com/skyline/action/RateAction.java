package com.skyline.action;
/**
 * 辅助发邮件,获得供应商,客户,rate 的信息
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.logging.SimpleFormatter;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

























import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.predisw.exception.NotSameException;
import com.predisw.util.DateFormatUtil;
import com.skyline.comparatorImple.BaseRateCodeComparator;
import com.skyline.comparatorImple.BaseRateComparator;
import com.skyline.comparatorImple.BaseRateCountryComparator;
import com.skyline.comparatorImple.BaseRateOperatorComparator;
import com.skyline.comparatorImple.CcComparator;
import com.skyline.comparatorImple.CusComparator;
import com.skyline.pojo.CountryCode;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Email;
import com.skyline.pojo.Employee;
import com.skyline.pojo.ExcelTp;
import com.skyline.pojo.Partner;
import com.skyline.pojo.Props;
import com.skyline.pojo.Rate;
import com.skyline.pojo.RateList;
import com.skyline.pojo.SendRecord;
import com.skyline.pojo.User;
import com.skyline.service.CountryCodeService;
import com.skyline.service.CustomerService;
import com.skyline.service.EmployeeService;
import com.skyline.service.JavaMailService;
import com.skyline.service.PropsService;
import com.skyline.service.BaseService;
import com.skyline.service.RateService;
import com.skyline.service.SendRecordService;
import com.skyline.util.HttpUpAndDownload;
import com.skyline.util.IOhelp;
import com.skyline.util.PageInfo;
import com.skyline.util.PoiExcel;
@Controller   //必须添加，否则requestMapping 不生效
public class RateAction {
	Logger logger = LoggerFactory.getLogger(RateAction.class);
	@Autowired
	private EmployeeService empService;
	@Autowired
	private CustomerService cusService;
	@Autowired
	private CountryCodeService ccService;
	@Autowired
	private BaseService baseService;//将所有pojo类的的通用方法都可以放到这个service 
	@Autowired
	private PropsService propsService;
	@Autowired
	private RateService rateService;
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private JavaMailService javaMailService;
	@Autowired
	private SendRecordService srService;
	
	IOhelp iohelp=new IOhelp();


	
//--------------------------获取发报价时显示业务员----------------------------------------------------
	
	@RequestMapping(value="/getEmps.do")
	public void getEmps(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		List empList=empService.getEmps(true);
		req.setAttribute("empList", empList);
		req.getRequestDispatcher("/WEB-INF/jsp/rate/addCus.jsp").forward(req, res);
	}

	//------------------------------------------------------------------------------
	//将选择工号的业务员的客户保存到request 中,然后在jsp 页面中取出来,但是保存工号到session 中是为了方便移除选中的业务员
	@RequestMapping(value="/getCus.do")
	public void getCus(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		String[] emps; 
		if(req.getParameterValues("checkCus")!=null){
			emps=req.getParameterValues("checkCus");  //请求超时的时候，会出现null 指针！emps 是员工工号
			req.getSession().setAttribute("emps", emps);
		}else{
			emps=(String[])req.getSession().getAttribute("emps");
		}
		
		if(emps==null){
			res.sendRedirect("getEmps.do");
			return;
		}
		//通过员工工号获取这些工号的客户
		List<Customer> cusList=new ArrayList<Customer>();
		List<Customer> tmpList=null;
		for(String eNum:emps){
			tmpList=cusService.getCustomersByType(eNum, Partner.CUSTOMER);
			cusList.addAll(tmpList);
			tmpList.clear();
		}
		Collections.sort(cusList,new CusComparator());
		req.setAttribute("cusList", cusList);
		req.getRequestDispatcher("getEmps.do").forward(req, res);
	}

	//------------------------------------------------------------------------------
	//获取选择的目标客户，方便从其他地方获取被选的客户。例如可以在getRate.do 中获取客户id,写入rate 表中
	@RequestMapping(value="/getTargetCus.do")
	public void getTargetCus(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		String[] cusIds=req.getParameterValues("cusList"); //获取到的客户id 是一个数组

		logger.debug("方法[getTargetCus.do],获取提交的客户有[{}]个",cusIds.length);
		req.getSession().setAttribute("cusIds", cusIds); //将获取到的客户id 保存到回话中
		req.getSession().removeAttribute("cl");
		res.sendRedirect(req.getContextPath()+"/getcc.do");
		
	}
	
	//------------------------------------------------------------------------------
	//将所有国家地区的名字不重复的列出来，还有就是同时显示出经常使用的国家地区表
	@RequestMapping("/getcc.do")
	public void getcc(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
	
		//获取被选的客户
		String[] cusIds=(String[])req.getSession().getAttribute("cusIds"); //客户customer的id 列表
		if(cusIds==null){//假如没有选择客户,则返回提示并返回选择客户的页面
			res.setContentType("text/html;charset=UTF-8"); //不设置,则js 中的中文会乱码 如alert("请选择客户"),
			//important!! 要放到printWriter 之前,要不也不生效
			PrintWriter out = res.getWriter();
			out.println("<script type=\"text/javascript\">");
			out.println("alert(\"请选择客户\");");
			out.println("location.href=\"getEmps.do\""); //相对路径
			out.println("</script>");
			return;
		}
		//读取Message 中的信息
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
		}
		
		//通过客户的id 将客户对象遍历出来放到cusList 中
		List<Customer> cusList=new ArrayList<Customer>();
		for(int k=0;k<cusIds.length;k++){
			Customer cus= new Customer();  //不能放到循环外面
			cus=(Customer) baseService.getById(Customer.class, Integer.parseInt(cusIds[k]));
			cusList.add(cus);
		}
		req.setAttribute("cusList", cusList); //显示被选的客户
		
		//获取所有的country
		List cList=ccService.getCountrys();
		req.setAttribute("cList", cList);
		//获取经常使用的country
		List ocList=ccService.getOfenCountrys(true);
		req.setAttribute("ocList", ocList);
		
		//获取可选的name 为 billing_unit属性的对象,对象类似一个map
		List<Props> pList=baseService.getByField(Props.class, "name", "billing_unit");
		List<String> bList=new ArrayList<>();
		for(int i=0;i<pList.size();i++){
			bList.add(pList.get(i).getValue());
		}
		req.setAttribute("bList", bList);
	
		//获取可选的level 属性
		pList.clear();
		pList=baseService.getByField(Props.class, "name", "level");
		List<String> lList=new ArrayList<>();
		for(int i=0;i<pList.size();i++){
			lList.add(pList.get(i).getValue());
		}
		req.setAttribute("lList", lList);
		
		//-----------获取excle所有的 附件模板-----
		List<ExcelTp> eList =baseService.getByClass(ExcelTp.class);
		req.setAttribute("eList", eList);
		
		//获取客户上次发送的模板,在选择模板处显示
		Customer cus=(Customer) baseService.getById(Customer.class,Integer.parseInt(cusIds[0]));
		SendRecord sr = srService.getLastExcelTp(cus.getVosId());
		if(sr!=null){
			req.setAttribute("dExcelTp", sr.getExcelTp());
		}

		
		req.getRequestDispatcher("/WEB-INF/jsp/rate/addRate.jsp").forward(req, res);;
		
		
	}
	

	
	//------------------------------------------------------------------------------
	//设置哪些国家是常用的
	@RequestMapping("setOften.do")
	public String  setOftenCountry(HttpServletRequest req,RedirectAttributes red) throws IOException{
		String[] ccIds=req.getParameterValues("selectedCC");  
		try{
			ccService.setOftenCountrys(true, ccIds);
		}catch(StaleObjectStateException e){
			e.printStackTrace();
			red.addFlashAttribute("Message", "其他人已设置了");
			return "redirect:getcc.do";
		}catch(Exception e){
			logger.error("", e);
			red.addFlashAttribute("Message", "失败: "+e.getMessage()+"cause: "+e.getCause() );
			return "redirect:getcc.do";
		}
		return "redirect:getcc.do";
	}
	
	
	
	//------------------------------------------------------------------------------
		//移除常用的国家
		@RequestMapping("rmOften.do")
		public String rmOfenCountry(HttpServletRequest req,RedirectAttributes red){
			String ccId=req.getParameter("rmCC"); 
			String[] ccIds = ccId.split(";");
			try{
				ccService.setOftenCountrys(false, ccIds);
			}catch(StaleObjectStateException e){
				logger.error("", e);
				red.addFlashAttribute("Message", "其他人已设置了");
				return "redirect:/getcc.do";
			}catch(Exception e){
				logger.error("", e);
				red.addFlashAttribute("Message", "失败: "+e.getMessage()+"cause: "+e.getCause() );
				return "redirect:/getcc.do";
			}
			logger.debug("there are [{}] countrys to be removed from oftenCountry ",ccIds.length);
			return "redirect:/getcc.do";
		}
		

		//---------------------------
		//下面这个方法中的用到了重写的countryCode 的equals,为了在set 中去重复
		@RequestMapping("getOperators.do")
		public void getOperators(HttpServletRequest req,HttpServletResponse res) throws IOException{
			String countryIds =req.getParameter("countryIds");
			String[]  ccIds = countryIds.split(",");
			if(StringUtils.isEmpty(countryIds)){
				ccIds = new String[0]; 
			}
			
			String operatorIds = req.getParameter("opListIds");
			String[] 	opIds=operatorIds.split(",");
			
			if(StringUtils.isEmpty(operatorIds)){
				opIds=new String[0];
			}

			
			//所有选择的运营商
			Set<CountryCode> ccSet = new HashSet<>();
			
			List<CountryCode> tmpList=new ArrayList<CountryCode>();
			
			String Message="success";
			
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			
			JSONObject data = new JSONObject();

			
			try{
				//根据选择的国家获取这个国家的运营商
				for(String ccid:ccIds){
					CountryCode cc=(CountryCode) baseService.getById(CountryCode.class, Integer.parseInt(ccid));
					tmpList = ccService.getOperators(cc.getCountry());
					ccSet.addAll(tmpList);
					tmpList.clear();
				}

				//已选择的运营商
				for(String opId:opIds){
					CountryCode cc=(CountryCode) baseService.getById(CountryCode.class, Integer.parseInt(opId));
					ccSet.add(cc);
				}

			}catch(Exception e){
				logger.error("",e);
				Message=e.getClass().getSimpleName()+": "+e.getMessage();
				data.put("Message", Message);
				out.write(data.toString());
				return;
			}



			JSONArray ccArr=new JSONArray();
			
			for(CountryCode cc:ccSet){
				JSONObject ccObj= new JSONObject();
				ccObj.put("operator", cc.getOperator());
				ccObj.put("code", cc.getCode());
				ccObj.put("id",cc.getCcId());
				ccArr.put(ccObj);
			}
			data.put("Message", Message);
			data.put("ccArr", ccArr);
			

//			System.out.println(data);
			out.write(data.toString());
			
			
		}
		
		
		
	//-----------------------	显示选择的countrycode 对应的历史rate记录-------------------------------------------------------

	@RequestMapping("/getRate.do")
	public void getRate(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		
		//---------------------------------获得客户-----------------------
		
		String[] cusIds=(String[])req.getSession().getAttribute("cusIds"); //客户customer的id 列表
		//通过客户的id 将客户对象遍历出来放到cusList 中
		List<Customer> cusList=new ArrayList<Customer>();
		for(int k=0;k<cusIds.length;k++){
			Customer cus= new Customer();  //不能放到循环外面
			cus=(Customer) baseService.getById(Customer.class, Integer.parseInt(cusIds[k]));
			cusList.add(cus);
		}
		
		//添加客户之前自定义的邮件内容,主题,和附件名称,假如存在多个客户,则不再返回自定义的邮件内容.
		if(cusList.size()==1){
			SendRecord sr=srService.getLastExcelTp(cusList.get(0).getVosId());
			if(sr!=null){
				Email email=(Email) baseService.getByField(Email.class, "name", sr.getEmailName()).get(0);
				if(sr.getCmtAttach()!=null && sr.getCmtAttach()){
					String mail_attach=email.getAttacheName();
					req.setAttribute("mail_attach", mail_attach.substring(0,mail_attach.lastIndexOf(".")));
				}
				if(sr.getCmtContent()!=null && sr.getCmtContent()){
					String mail_content=email.getContent();
					mail_content=mail_content.replace("</p>", "</p>\\");
					mail_content=mail_content.replace("<br />", "</br />\\");
					mail_content=mail_content.replace("\n\r", "\\"); //注意在linux 服务器中不是"\n" 而是"\n\r"
					req.setAttribute("mail_content", mail_content);
				}
				if(sr.getCmtSubject()!=null && sr.getCmtSubject()){
					req.setAttribute("mail_subject", email.getSubject());
				}
			}
			
		}
		
		
		// -------------------------------获取被选择的运营商-----------------------------
		
		String[] ccids= req.getParameterValues("opList");
		List<CountryCode> ccList=new ArrayList<>();
	
		for(String ccid:ccids){
			CountryCode cc=(CountryCode) baseService.getById(CountryCode.class, Integer.parseInt(ccid));
			ccList.add(cc);
		}
		
		
		//从rate 表中获取选中的客户的某些code 的记录
		List<Rate> rateList=new ArrayList<Rate>();
		List<Rate> tmpList;
		
		//不管是选择了多个客户还是只有一个客户,都只是返回第一个客户的资料来显示
			for(int j=0;j<ccList.size();j++){
				tmpList=rateService.getRateByCid(cusList.get(0).getCId(), ccList.get(j).getCode(),true,true,true); //会返回多个,但是已经根据发送时间排序,desc
				//如果这个客户没有这个code ,就把这个code 的资料放到rateList 中去,方便前台jsp 显示和提交到保存action
				if(tmpList==null || tmpList.size()==0){ 
					Rate rate=new Rate();  //可否放到循环外面??
					//保存国家code 信息
					rate.setCountry(ccList.get(j).getCountry());
					rate.setCode(ccList.get(j).getCode());
					rate.setOperator(ccList.get(j).getOperator());
					rateList.add(rate);

				}else{
					rateList.add(tmpList.get(0));  //如果为空会如何?i mean tmpList
				}

				tmpList.clear();
			}
			
			
			Rate rate=null;
			try {
				rate = rateService.doBeforeGetRate(cusList);
			} catch (Exception e) {

				logger.error("", e);
				req.setAttribute("Message", e.getMessage()+"cause: "+e.getCause());
				req.getRequestDispatcher("/getcc.do").forward(req, res);
				return;
			}
			
			if(rate!=null){
				req.setAttribute("dfLevel", rate.getLevel());
				req.setAttribute("dfBilling", rate.getBillingType());
				req.setAttribute("dfPrefix", rate.getLevelPrefix());
			}else{
				req.setAttribute("Message", "请注意设置 计费方式 和 线路类型!");
			}
		
		logger.debug("[getRate.do]: [{}] customers need to be sent,[{}] rate records need to be sent to every customer ",cusList.size(),rateList.size());

		//排序...
		Collections.sort(rateList, new BaseRateComparator());
//		Collections.sort(rateList, new BaseRateCountryComparator());
//		Collections.sort(rateList, new BaseRateOperatorComparator());
//		Collections.sort(rateList, new BaseRateCodeComparator());
		
		req.setAttribute("cl", ccList);
		req.setAttribute("rateList", rateList);
		req.getRequestDispatcher("/getcc.do").forward(req, res);

	}
	
	
	
	
	
	//---------------邮件图片上传---
	@RequestMapping("/imageUpload.do")
	public void uploadImager(HttpServletRequest req,HttpServletResponse res) throws IOException{

		String fullContentType = "text/html;charset=UTF-8"; //支持中文名称的图片附件的输出
		res.setContentType(fullContentType);

		String fileName=null;
		try {
			fileName = HttpUpAndDownload.getUploadInput(req).get("upload");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);

		}
		String http_name=req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+File.separator+"upload"+File.separator+fileName.substring(fileName.lastIndexOf(File.separator));
//		String name=req.getServletContext().getRealPath("/upload")+fileName.substring(fileName.lastIndexOf(File.separator));
		logger.debug("upload imager name is [{}]",fileName);
		PrintWriter out = res.getWriter();
		String callback=req.getParameter("CKEditorFuncNum");  //CKEditorFuncNum 不能改,是ckeditor 默认的名称
		out.println("<script type=\"text/javascript\">");
		out.println("window.parent.CKEDITOR.tools.callFunction(" + callback+ ",'" +http_name + "','')"); // 相对路径用于显示图片  
		out.println("</script>");
				
	}
	
	
	//--------------通过ajax 提示level 是否不一致,effective_time 是否为空和是否不一样.还有rate 的值是否为空
	@RequestMapping("checkBeforeMail.do")
	public void checkBeforeMail(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String json_str=req.getParameter("params");

		JSONObject jsonObject=new JSONObject(json_str);
		String dflevel=(String) jsonObject.get("dfLevel");
		JSONArray jsonArray=jsonObject.getJSONArray("array");
		StringBuffer Message=new StringBuffer();
		String isTrue="false";
		String rate_m=null;
		String level_m=null;
		String ef_m=null;

		if(jsonArray.length()==0){
			Message.append("请先选择运营商或再次确认运营商");
		}
		
		for(int i=0;i<jsonArray.length();i++){
			JSONObject json=jsonArray.getJSONObject(i);
			if(rate_m==null &&"".equals(json.get("rate"))){
				rate_m="费率不能为空";
				if(Message.length()>0){
					Message.append(",");
				}
				Message.append(rate_m);
			}
			if(ef_m==null && "".equals(json.get("ef_time"))){
				ef_m="生效日期不能为空";
				if(Message.length()>0){
					Message.append(",");
				}
				Message.append(ef_m);
			}
			
			if(ef_m==null && json.get("ef_time")!=null ){
				
				SimpleDateFormat sdf =DateFormatUtil.getSdf("yyyy-MM-dd");
				sdf.setLenient(false);
				try{
					sdf.parse(json.getString("ef_time"));
				}catch(ParseException e){
					
					ef_m="日期值或日期格式不正确";
					if(Message.length()>0){
						Message.append(",");
					}
					Message.append(ef_m);
				}
			}
			
			
			if(!"error".equals(dflevel) && level_m==null && !dflevel.equals(json.get("level"))){
				level_m="客户或多个客户之间的线路类型不一致";
				if(Message.length()>0){
					Message.append(",");
				}
				Message.append(level_m);
			}
		}
		

		if(Message.length()==0){
			isTrue="true";
		}

		res.setCharacterEncoding("UTF8");
		String json="{"+"\"isTrue\":"+"\""+isTrue+"\","+"\"Message\":"+"\""+Message+"\""+"}";
		PrintWriter writer = res.getWriter();
		writer.print(json);
	}




	//-----------------------------进度条-------------------
	@RequestMapping("getProgress.do")
	public void getProgress(HttpServletRequest req,HttpServletResponse res) throws IOException{
		
		Integer  sendMailProgress=(Integer)req.getSession(false).getAttribute("sendMailProgress");

		PrintWriter out = res.getWriter();
		if(sendMailProgress==null){
			sendMailProgress=0;
		}

//		res.setHeader("Access-Control-Allow-Origin","*"); //设置允许跨域名的url
		String smp="{"+"\""+"sendMailProgress"+"\""+":"+"\""+sendMailProgress.toString()+"\""+"}";
		
		out.print(smp);
	}
	

	
	
	
	
}
