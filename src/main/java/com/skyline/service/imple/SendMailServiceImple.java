package com.skyline.service.imple;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.predisw.util.DateFormatUtil;
import com.skyline.dao.BaseDao;
import com.skyline.pojo.Email;
import com.skyline.pojo.Rate;
import com.skyline.pojo.SendRecord;
import com.skyline.service.JavaMailService;
import com.skyline.service.SendMailService;
@Controller
public class SendMailServiceImple implements SendMailService {
	@Autowired
	private JavaMailService javaMailService;
	@Autowired
	private BaseDao baseDao;
	Logger logger =LoggerFactory.getLogger(this.getClass());	
	
	
	
	
	@Override
	public void saveForMail(List<Rate> rateList, SendRecord sr,Email email) {
		// TODO Auto-generated method stub
		for(Rate rate:rateList){
			baseDao.save(rate);
		}
		baseDao.save(sr);
		baseDao.save(email);
	}
	
	
	
	
	@Override
	public void setETAfterMail(List<Rate> rateList) {
		// TODO Auto-generated method stub
		Rate rate;
		Calendar calendar=Calendar.getInstance();
		for(int i=0;i<rateList.size();i++){
			if(rateList.get(i).getPRid()!=null){
				rate=(Rate)baseDao.getById(Rate.class, rateList.get(i).getPRid());
				calendar.setTime(rateList.get(i).getEffectiveTime());
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				rate.setExpireTime(calendar.getTime());
				baseDao.update(rate);
			}
			
		}
		
	}
	
		
	
	
	
	@Override
	public void sendMail(Email email,SendRecord successedSR,SendRecord failedSR) throws Exception {
		// TODO Auto-generated method stub

		Date send_time =new Date();
		//更新邮件主题
		if(successedSR.getCmtSubject()==null || successedSR.getCmtSubject()==false){
			
			email.setSubject(email.getSubject().substring(0,email.getSubject().lastIndexOf("-RT")+3)+DateFormatUtil.format(send_time, "yyyyMMddhhmmss"));
		}
		
		//发送邮件以及更改状态
		try {
				javaMailService.sendMail(email);
				
				baseDao.update(email); //更新邮件主题
				
				successedSR.setSendTime(send_time);
				baseDao.update(successedSR);
				
				baseDao.setByField(Rate.class,"isSuccess",true,"name",successedSR.getRateName()); //设置rate 表中对应的记录为发送成功
				baseDao.setByField(Rate.class, "sendTime",send_time , "name", successedSR.getRateName()); //更新对应类记录的发送时间
				
				List<Rate> rateList=baseDao.getByField(Rate.class, "name", successedSR.getRateName());
				this.setETAfterMail(rateList);  //修正cRate中的过期时间
				
			} catch ( Exception e) { 
				logger.error("", e);
				failedSR.setSendTime(send_time);
				baseDao.update(failedSR);
				throw new Exception(e.getMessage(), e.getCause());
			}
				

	}
	
	
	
}
