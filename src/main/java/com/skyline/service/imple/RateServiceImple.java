package com.skyline.service.imple;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.annotation.Description;
import com.predisw.annotation.Log;
import com.predisw.exception.NotSameException;
import com.skyline.dao.BaseDao;
import com.skyline.dao.RateDao;
import com.skyline.dao.SendRecordDao;
import com.skyline.dao.imple.RateDaoImple;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Rate;
import com.skyline.pojo.SendRecord;
import com.skyline.service.BaseService;
import com.skyline.service.JavaMailService;
import com.skyline.service.LogService;
import com.skyline.service.RateService;
import com.skyline.util.PageInfo;
@Service
public class RateServiceImple   implements RateService{
	@Autowired
	private RateDao rateDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JavaMailService javaMailService;
	@Autowired
	private LogService logService;

	

	
	@Override
	public Rate doBeforeGetRate(List<Customer> cusList) throws NotSameException {
		// TODO Auto-generated method stub
		List<Rate> rateList=new ArrayList<>();
		//获取所有客户的一个曾发送过的rate 对象
		for(Customer cus:cusList){
			Rate rate=rateDao.getOneRate(cus.getVosId(),  true, true,true);
			if(rate!=null){
				rateList.add(rate);
			}
		}
		//假如多于一个客户曾经发过报价
		if(rateList.size()>1){

			for(int i=0;i+1<rateList.size();i++){
				//检查level 是否一致
				if(!rateList.get(i).getLevel().equals(rateList.get(i+1).getLevel())){
					throw  new NotSameException("多个客户之间的level 不一致");
				}
				//检查计费方式 是否一致
				if(!rateList.get(i).getBillingType().equals(rateList.get(i+1).getBillingType())){
					throw  new NotSameException("多个客户之间的billing 方式 不一致");
				}
				//检查level prefix 是否一致
				if(rateList.get(i).getLevelPrefix()!=null){
					if(rateList.get(i+1).getLevelPrefix()!=null){ //假如i 和i+1 rate 对象的prefixLevel 都不为空则比价
						if(!rateList.get(i).getLevelPrefix().equals(rateList.get(i+1).getLevelPrefix())){
							throw  new NotSameException("多个客户之间的prefix  不一致");
						}
					}else{ //假如i 的prefixLevel 不为空,而i+1 的为空,则....
						throw  new NotSameException("多个客户之间的prefix  不一致");
					}
					
				}
				
			}
		}
		
		if(rateList.size()>0){
			return rateList.get(0);
		}else{
			return null;
		}
		
	}
	
	
	@Override
	public boolean checkBfResend(SendRecord sr) {
		// TODO Auto-generated method stub
		
		Date sendTime=sr.getSendTime();
		Date now=new Date();
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(sendTime);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		sendTime=calendar.getTime();
		
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		now=calendar.getTime();
//		System.out.println("sendTime is "+sendTime+",now is "+now);
		
		if(now.after(sendTime)){ //超过当天则不能再重发
			return false;
		}
		
		if(sr.getSendStatus()!=null && sr.getSendStatus()==1){ //发送成功则不能再重发.
			return false;
		}
		return true;
	}
	


	


	@Log
	@Override
	public void delCusRates(String[] rIdArr) {
		// TODO Auto-generated method stub
		if(rIdArr!=null){
			for(int i=0;i<rIdArr.length;i++){
				Rate rate=(Rate)baseDao.getById(Rate.class,Integer.parseInt(rIdArr[i]));
				rateDao.setIsAvailable(rate.getVosId(), rate.getCode(), false); //不要用super 调用....,否则无法记录日志

				logService.logToDb("移除报价code", null, "vosId:"+rate.getVosId()+"-code:"+rate.getCode());
			}
		}

	}


	@Override
	public List<Rate> getRateByCid(int cid, String code, boolean is_available,
			boolean is_success, boolean is_correct) {
		// TODO Auto-generated method stub
		return rateDao.getRateByCid(cid, code, is_available, is_success, is_correct);
	}


	@Override
	public List<Rate> getLastRateByCid(int cid, boolean is_available,
			boolean is_success, boolean is_correct) {
		// TODO Auto-generated method stub
		return rateDao.getLastRateByCid(cid, is_available, is_success, is_correct);
	}


	@Override
	public void setChange(String isChange) {
		// TODO Auto-generated method stub
		rateDao.setChange(isChange);
	}


	@Override
	public void setSuccess(String rateName, boolean is_success) {
		// TODO Auto-generated method stub
		rateDao.setSuccess(rateName, is_success);
	}


	@Override
	public void setIsAvailable(String vosid, String code, boolean is_available) {
		// TODO Auto-generated method stub
		rateDao.setIsAvailable(vosid, code, is_available);
	}


	@Override
	public Rate getOneRate(String vosId, boolean is_available,
			boolean is_success, boolean is_correct) {
		// TODO Auto-generated method stub
		return rateDao.getOneRate(vosId, is_available, is_success, is_correct);
	}


}
