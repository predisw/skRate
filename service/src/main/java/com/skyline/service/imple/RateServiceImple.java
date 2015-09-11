package com.skyline.service.imple;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.annotation.Description;
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
import com.skyline.service.RateService;
import com.skyline.util.PageInfo;
@Service
public class RateServiceImple  extends RateDaoImple implements RateService{
	@Autowired
	private RateDao rateDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JavaMailService javaMailService;


	

	
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
	


	
	@Override
	@Description(name="设置报价错误")
	public void setMailInCorrect(List<SendRecord> srList) {

		for(SendRecord sr:srList){
			List<Rate> rateList=baseDao.getByField(Rate.class, "name", sr.getRateName());
			for(Rate rate:rateList){
				rate.setIsCorrect(false);
				baseDao.update(rate);
				if(rate.getPRid()!=null){
					Rate old_rate=(Rate)baseDao.getById(Rate.class,rate.getPRid() );
					old_rate.setExpireTime(null);
					baseDao.update(old_rate);
				}
				
				
			}
			sr.setIsCorrect(false);
			baseDao.update(sr);
		}
	}


	@Override
	public void delCusRates(String[] rIdArr) {
		// TODO Auto-generated method stub
		if(rIdArr!=null){
			for(int i=0;i<rIdArr.length;i++){
				Rate rate=(Rate)baseDao.getById(Rate.class,Integer.parseInt(rIdArr[i]));
				super.setIsAvailable(rate.getVosId(), rate.getCode(), false);
			}
		}

	}


}
