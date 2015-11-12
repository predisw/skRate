package com.skyline.action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.predisw.util.SingletonLock;
import com.skyline.comparatorImple.BaseRateCodeComparator;
import com.skyline.comparatorImple.BaseRateCountryComparator;
import com.skyline.comparatorImple.BaseRateEfTimeComparator;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Email;
import com.skyline.pojo.Props;
import com.skyline.pojo.Rate;
import com.skyline.pojo.RateList;
import com.skyline.pojo.SendRecord;
import com.skyline.pojo.User;
import com.skyline.service.BaseRateService;
import com.skyline.service.BaseService;
import com.skyline.service.JavaMailService;
import com.skyline.service.RateService;
import com.skyline.service.SendMailService;
import com.skyline.service.SendRecordService;
import com.skyline.util.PoiExcel;
import com.skyline.util.SingletonProps;
@RequestMapping("/sendMail")
@Controller
public class SendMailAction {
	@Autowired
	private BaseService baseService;
	@Autowired
	private RateService rateService;
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private JavaMailService javaMailService;
	@Autowired
	private SendMailService smService;
	@Autowired
	private SendRecordService srService;
	@Autowired
	private BaseRateService baseRateService;
	
	
	Logger logger =LoggerFactory.getLogger(this.getClass());

	//添加一个锁
	// 防止在发报价的时候修改rate 表
	//在sendRecordAction 中的setRateRecordIncorrect() 会修改rate 表.
	//
	private ReadWriteLock rateLock=SingletonLock.getSingletonReadWriteLock();  
	Lock rLock=rateLock.readLock(); 

	
	//用到session.setAttibute 的方法：getEmail(),getOperators(),getCus(),save()
	
	
	//spring mvc 前台的Form 元素绑定到 后台的JaveBean对象，做的一个映射，但是这个映射的List长度不可以超过256
	  @InitBinder  
	  public void initListBinder(WebDataBinder binder)  
	  {  
	      // 设置需要包裹的元素个数，默认为256  
	      binder.setAutoGrowCollectionLimit(1024);  
	  } 
	
	
	//------------------------------------------------------------------------------
	//保存rate 到数据库中
	@RequestMapping("/setMailInfo.do")
	public String setMailInfo(RateList form_rateList,HttpServletRequest req,HttpServletResponse res,RedirectAttributes red){
		
		User loginUser=(User)req.getSession().getAttribute("user");
		
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");  //用于格式化来自input 标签的date 格式,输入excel 中时要格式化的 effective time 
		
		//------------- 获取邮件附件名称,内容和主题和全局的抄送地址----页面提交的主题和内容对所有客户来说都是一样的---------------
		String attachName=req.getParameter("mail_attach"); //邮件附件的名称 ,不是绝对路径,而是用于设置附件的名称.
		String mail_subject=req.getParameter("mail_subject");
		//设置邮件主题需要的level,在sendMail 中取出
				
		String globalBccAddrList="";
		List<Props> propsOfBccAddr  = baseService.getByField(Props.class, "name", "bccAddr");
		if(propsOfBccAddr!=null &&propsOfBccAddr.size()>0){
			globalBccAddrList=propsOfBccAddr.get(0).getValue();
		}

		String mail_content=req.getParameter("mail_content");
		String cmt_mail=""; //邮件内容是否自定义的标记
		//因为邮件内容所有的客户都是 一样的,所以可以在这里设置,而主题和附件的名称是有vosId 的值的,所以每个客户不一样
		if(mail_content==null || "".equals(mail_content)){
			cmt_mail=null;
			//如果邮件内容为空,则使用默认的邮件内容(在表props 中字段名为default_mail_content 的值)
			List<Props> pList=baseService.getByField(Props.class, "name", "default_mail_content");
			if(pList!=null && pList.size()>0){
				mail_content=pList.get(0).getValue();
			}
		}
		
		//抄送地址
		//在sendMail 和reSendMail 中独立读取
		
		//-------------------------------获取发送的目标客户----------------------
		String[] cusIds=(String[])req.getSession().getAttribute("cusIds"); //客户customer的id 列表
		//通过客户的id 将客户对象遍历出来放到cusList 中
		List<Customer> cusList=new ArrayList<Customer>();
		for(int k=0;k<cusIds.length;k++){
			Customer cus= new Customer();  //不能放到循环外面
			cus=(Customer)baseService.getById(Customer.class,Integer.parseInt(cusIds[k]));
			cusList.add(cus);
		}
		
		//----------------------------获取excel 模板------------
		String excelTp=req.getParameter("excel"); //excel 附件的绝对路径名
		String eTpName=excelTp.substring(excelTp.lastIndexOf(File.separator)+1, excelTp.lastIndexOf("."));
	
		//---------------获取System.properties 中的属性--------

		Properties props;
		try {
			props = SingletonProps.getInstance().getProperties();
		} catch (IOException e) {
			logger.error("", e);
			red.addFlashAttribute("Message", "获取系统属性失败 "+e.getMessage()+" cause:"+e.getCause());
			return "redirect:/getcc.do";
		}
			
		//-------------获取发送时间---------每位客户的发送时间都是不一样的---------
		Date now=new Date();
		Rate rate;

		//------------------rate 记录name 的随机数部分
		//内容相同的rate记录发给不同的客户,这个记录名字不能一样,当这个客户的这份记录发送成功,就根据这个唯一的名字更改isSuccess 字段
		//名字的格式是客户vosId+时间+随机数,这样就不会重复了.
	   	String chars = "0123456789"; 
 	     char[] rands = new char[4]; 
 	     for (int i = 0; i < 4; i++) 
 	     { 
 	         int rand = (int) (Math.random() * 10); 
 	         rands[i] = chars.charAt(rand); 
 	     } 
 	     String randomS=String.valueOf(rands); //把随机生成的四个整形数字变成String型
 	     
 	     String randomName=now.getTime()+randomS;

 	     //-----------------记录用于发送邮件时需要的信息--------------------
		
 	     //一个map,保存客户 和这个客户对应需要发送的excel附件 文件的文件名
		Map<SendRecord,Email>  sr_email =new HashMap<SendRecord, Email>();


		
		//--------------------导出需要发送的rate 报价记录为excel 文件,将 文件名保存到cus_rate map 中---
		
		//需要先导出到excel再导入数据库, 在线程中整合表单中提交过来的rate 记录 和从数据库中取出的这个客户的所有code 最新的记录
		//在这里整合然后发出去,而不是先写到数据库在发送,是为了避免多线程写入数据库互相覆盖的问题
				String[] db_header=props.getProperty("db_header_NPrefix").split(",");
				String[] excel_header=props.getProperty("excel_header_NPrefix").split(",");
				String level_prefix=req.getParameter("a_level_prefix");
				if(!"".equals(level_prefix) && level_prefix!=null){
					db_header=props.getProperty("db_header").split(",");
					excel_header=props.getProperty("excel_header").split(",");
				}

				String sheetIndexStr=props.getProperty("sheetNumber");
				int sheetIndex=0;
				if(sheetIndexStr!=null){
					sheetIndex=Integer.parseInt(sheetIndexStr);
				}		
				String newSheetName=props.getProperty("newSheetName");
				String filePath=req.getServletContext().getRealPath(props.getProperty("excel_export")); //生产环境下的excel 导出绝对路径

			
				List<Rate> rateList1=form_rateList.getRateList();

				//处理进度 10%
				req.getSession(false).setAttribute("sendMailProgress",10);
				
			try{
				rLock.lock();
				
				for(int i=0;i<cusList.size();i++){
					Email email =new Email();
					

					
					
					cusList.get(i).setRateLevel(rateList1.get(0).getLevel()); //所有客户的level 都是一样的.所有使用提交的rate记录第第一条的level,作为所有客户的level
					baseService.update(cusList.get(i)); //写入数据库,为后面发送邮件的主题的一部分
					
					String fileName=cusList.get(i).getVosId()+"_"+randomName;
					String suffix=excelTp.substring(excelTp.lastIndexOf(".")); //后缀名
					String excel_path_name=filePath+File.separator+fileName+suffix;
					logger.debug("the file_path_name is [{}]",excel_path_name);
					
					try {
						Files.copy(Paths.get(excelTp),Files.newOutputStream(Paths.get(excel_path_name),StandardOpenOption.CREATE_NEW));
					} catch (IOException e) {
						logger.error("", e);
						red.addFlashAttribute("Message", "复制模板失败"+e.getMessage()+" cause:"+e.getCause());
						return "redirect:/getcc.do";
					}
					
					List<Rate> rateList2 = new ArrayList<>();  //假如提交的rate 报价的生效时间大于now 则将其保存到这里.

					
					List<Rate> cRateList=rateService.getLastRateByCid(cusList.get(i).getCId(), true, true,true);
					//找到cRateList与rateList1 中code相同的记录,并移除
					for(int j=0;j<rateList1.size();j++){
						
						//转换日期格式,如果不转换,在excel 中的日期是这样子的 :Thu Jun 25 00:00:00 CST 2015
						rateList1.get(j).setEffectiveTime((Date)( new java.sql.Date ( (rateList1.get(j).getEffectiveTime().getTime() )  ) ));
						
						if(cusList.size()>1){
							rateList1.get(j).setIsChange("NEW"); //当客户数量大于1 的时候,将提交过来的rate的change 状态默认设置为NEW
						}
						
						
						for(int k=0;k<cRateList.size();k++){
							if(rateList1.get(j).getCode().equals(cRateList.get(k).getCode())){  //当code 相同....
								//多个客户的时候,独立比较rate 的变化来设置change,再发送
								if(cusList.size()>1){
									baseRateService.setChangeStatus(cRateList.get(k), rateList1.get(j));
								}
								
								Date newEfTime=rateList1.get(j).getEffectiveTime(); //新的生效时间
								
								//假如生效时间比现在大将cRateList 中相同code 的这个报价设置好过期时间,也发给客户
								if(newEfTime.getTime()>now.getTime()  ){
									Date oldEfTime=cRateList.get(k).getEffectiveTime();
									//假如这个生效时间大于现在,而且属于重复发送.
									if(newEfTime.compareTo(oldEfTime)==0){
										
										Rate pRate=cRateList.get(k);  //或者等于null 也可
										if(cRateList.get(k).getPRid()!=null){ //用于查找上个报价
											
											pRate =(Rate)baseService.getById(Rate.class, cRateList.get(k).getPRid());  //可以用泛型改善,或者将base 继承到rate 中.
											rateList1.get(j).setPRid(pRate.getRId()); //用于查找上个报价,例如发送报价成功时,通过rateList 找到对应的cRate,在修改数据库中的过期时间
											baseRateService.setChangeStatus(pRate, rateList1.get(j));
											pRate.setIsChange("Current");
										}
										rateList2.add(pRate);
									}
									else{  //第一发送 生效时间比现在大的处理方法:
										Calendar calendar = Calendar.getInstance();  //得到日历
										calendar.setTime(newEfTime);
										calendar.add(Calendar.DAY_OF_MONTH, -1); // 当天+(-1) 就是得到前天;
										Date expireDate=calendar.getTime();
										
										rateList1.get(j).setPRid(cRateList.get(k).getRId()); //用于发送报价成功时,通过rateList 找到对应的cRate,在修改数据库中的过期时间
										cRateList.get(k).setExpireTime(expireDate);
										cRateList.get(k).setIsChange("Current");
										rateList2.add(cRateList.get(k));

									}
								}

								cRateList.remove(k);
							}
						}
					}
					
					for(int n=0;n<cRateList.size();n++){
						cRateList.get(n).setIsChange("Current");
	//					cRateList.get(n).setEffectiveTime(now);
					}
					
					//将cRateList 和rateList1 合并到一个list,然后写到excel 中,
			//		cRateList.addAll(rateList1);  //如果将rateList1 加到cRateList 中则rateList1 的记录会放到excel 文件的后面,所以....
			//		rateList1.addAll(cRateList);   //这条语句会导致rateList1 被改变,循环第二次(第二个客户)的时候rateList1 已经被第一次循环所改变了.
			//rateList1 在循环第二次(第二个客户)时,rateList1 不再是前台jsp提交的数据了.


					List<Rate> send_rateList= new ArrayList<Rate>(); //必须用新建的List,要不会被覆盖,要不就在循环体外建send_rateList
					if(rateList2.size()>0){
						send_rateList.addAll(rateList2);
					}
					send_rateList.addAll(rateList1);

					send_rateList.addAll(cRateList);
					//A-Z 排序
					Collections.sort(send_rateList,new BaseRateEfTimeComparator());
					Collections.sort(send_rateList, new BaseRateCodeComparator());
					Collections.sort(send_rateList, new BaseRateCountryComparator());

					//生成附件内容--------------------------------------
				try {
					poiExcel.toExisting(send_rateList, excel_path_name, db_header, excel_header, sheetIndex, newSheetName,sdf);
				} catch (Exception e) {
					logger.error("", e);
					red.addFlashAttribute("Message", "写入excel 附件失败 "+e.getMessage()+" cause:"+e.getCause());
					return "redirect:/getcc.do";
				}
					logger.debug("the export excel2007 filename is [{}]",excel_path_name);


		//----------------添加邮件主题,内容,附件名,附件绝对路径---------------------
				email.setSubject(cusList.get(i).getCName()+"-RT");
				email.setContent(mail_content);
				email.setAttacheName(fileName+suffix);
				email.setAttacheFullName(excel_path_name);

		//-----------------添加发送地址----------
				email.setAddrList(cusList.get(i).getEmail());
				email.setCcAdddrList(cusList.get(i).getCcEmail());
				if(globalBccAddrList!=null){ 
					if(cusList.get(i).getBccEmail()!=null){	
						email.setBccAddrList(globalBccAddrList+","+cusList.get(i).getBccEmail());
						}
					else{
						email.setBccAddrList(globalBccAddrList);
					}
				}else{
					email.setBccAddrList(cusList.get(i).getBccEmail());
				}
				
		//------------------邮件其他信息		
				email.setName(fileName); //唯一名,和rate 报价记录名一致
				String uploadPath=req.getServletContext().getRealPath("/upload"); //上传文件的绝对路径
				email.setResourcePath(uploadPath);

					
					
					
				//	-------------将提交的rateList 中的rate 记录保存到数据库-------------------
					//每个客户多需要添加一份新增列表
					for(int j=0;j<rateList1.size();j++){
						//rate 在循环外声明会如何?因为事务总是在save 之后提交,同时关闭session,所以放在外面也可以
						rate=rateList1.get(j); 
						rate.setSendTime(now); //设置发送时间
						rate.setENum(cusList.get(i).getENum());
						rate.setVosId(cusList.get(i).getVosId());
						rate.setBakVosId(cusList.get(i).getVosId());
						rate.setCId(cusList.get(i).getCId());
						rate.setIsAvailable(true); //默认设置是有效的
						rate.setIsCorrect(true);//默认设置是正确的
						rate.setName(fileName);
					}
					
						
				//将这份导出excel 的记录的一些信息写入数据库,方便以后邮件发送失败的重送 
					SendRecord sr=new SendRecord();
					sr.setRateName(fileName);
					sr.setEmailName(email.getName());
					sr.setVosId(cusList.get(i).getVosId());
					sr.setCId(cusList.get(i).getCId());
					sr.setSendTime(now);
					sr.setUName(loginUser.getUName());
					sr.setExcelTp(eTpName);
					//设置是否为自定义的内容,主题,附件名
					if(!"".equals(mail_subject) &&mail_subject!=null){
						sr.setCmtSubject(true);
						email.setSubject(mail_subject);  //覆盖前面的设置
					}
					if(!"".equals(attachName) &&attachName!=null){
						sr.setCmtAttach(true);
						email.setAttacheName(attachName+suffix); //覆盖前面的设置,而且不能改变attachname 的值
						 
					}
					if("".equals(cmt_mail)){
						sr.setCmtContent(true);
					}
						
			//-将提交的rateList 中的rate 记录保存到数据库	------------和将sendRecord 发送记录写入数据库
					try{

						smService.saveForMail(rateList1, sr,email);
					}catch(Exception e){
						logger.error("", e);
						red.addFlashAttribute("Message", "写入数据库失败 "+e.getMessage()+" cause:"+e.getCause());
						return "redirect:/getcc.do";
					}

					
					//这个Map 是要传到下一个请求去,去发送邮件
					sr_email.put(sr,email);

					//处理进度 10+%
					req.getSession(false).setAttribute("sendMailProgress",10+40/cusList.size()-1);
				}
				
			}finally{
				rLock.unlock();
			}


		//


		req.getSession(false).setAttribute("sendMailProgress",50);
		
		req.setAttribute("sr_email", sr_email);



		return "forward:sendEmail.do";
	//	return null;

	}
	
	
	
	

	
	
	
	//----------------------------发送邮件-----
		@RequestMapping("sendEmail")
		public String sendEmail(HttpServletRequest req,HttpServletResponse res,RedirectAttributes red) throws CloneNotSupportedException {
			

			Map<SendRecord,Email> sr_email=(Map<SendRecord,Email>)req.getAttribute("sr_email");
			req.getSession(false).setAttribute("sendMailProgress",55);			//进度 55%
			
			
			for(Map.Entry<SendRecord,Email> entry:sr_email.entrySet()){
				SendRecord sr=entry.getKey();
				Email email=entry.getValue();

				//发送记录部分初始化
				SendRecord successedSR=(SendRecord) sr.clone();
				SendRecord failedSR=(SendRecord) sr.clone();
			
				//成功的发送记录
				successedSR.setSendStatus(1);
				//失败的发送记录
				failedSR.setSendStatus(-1);

				try {
					smService.sendMail(email, successedSR, failedSR);
	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("", e);
					red.addFlashAttribute("Message", e.getMessage()+" cuased Exception:"+e.getCause());
				}
				//进度每处理完一个客户增加45/客户数量的进度
				req.getSession(false).setAttribute("sendMailProgress",55+45/sr_email.size()-1);
			}
			//进度完成100%
			req.getSession(false).setAttribute("sendMailProgress",100);
			
			
			return "redirect:/sendRecord/getSendRecords.do";

			
		}
		
		//---------------------------------重发邮件--------通过 名字 来关联 rate 表的name 字段和send_record 表的name字段 --------------
		@RequestMapping("resendEmail.do")
		public String reSendEmail(HttpServletRequest req,HttpServletResponse res,RedirectAttributes red) throws CloneNotSupportedException{

			String strId=req.getParameter("id");  //发送记录的id,是前台选中的一个记录的id
			int id=Integer.parseInt(strId);
			SendRecord sr;
			Email email;
		
			try{
				sr = (SendRecord) baseService.getById(SendRecord.class, id);
				email=(Email)baseService.getByField(Email.class, "name", sr.getEmailName()).get(0);
			} catch (Exception e) {
				logger.error("", e);
				red.addFlashAttribute("Message", e.getMessage()+" cuased Exception:"+e.getCause());
				return "redirect:/sendRecord/getSendRecords.do";
			}

			if(!rateService.checkBfResend(sr)){
				red.addFlashAttribute("Message", "不能重发,因为....只能重发 当天的或者发送失败的邮件");
				return "redirect:/sendRecord/getSendRecords.do";
			}

			//发送记录部分初始化
			SendRecord successedSR=(SendRecord) sr.clone();
			SendRecord failedSR=(SendRecord) sr.clone();
			
			//成功的发送记录
			successedSR.setSendStatus(1);
			successedSR.setReSendStatus(true);
			//失败的发送记录
			failedSR.setSendStatus(-1);
			failedSR.setReSendStatus(false);
			
			try {
				smService.sendMail(email, successedSR, failedSR);
			} catch (Exception e) {

				logger.error("", e);
				red.addFlashAttribute("Message", e.getMessage()+" cuased Exception:"+e.getCause());
			}

			return "redirect:/sendRecord/getSendRecords.do";
		}
	
	
	

	
}
