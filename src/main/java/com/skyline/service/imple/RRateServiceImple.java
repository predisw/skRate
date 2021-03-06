package com.skyline.service.imple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.annotation.Log;
import com.predisw.util.DateFormatUtil;
import com.predisw.util.NumberUtils;
import com.skyline.dao.BaseDao;
import com.skyline.pojo.RRate;
import com.skyline.service.LogService;
import com.skyline.service.RRateService;
import com.skyline.util.PoiExcel;
@Service
public class RRateServiceImple implements RRateService {

	@Autowired
	private BaseDao baseDao;
	
	@Autowired
	private SessionFactory sf;
	
	@Autowired
	private LogService logService;

	Logger logger = LoggerFactory.getLogger(RRateServiceImple.class);
	
	@Log
	@Override
	public  void saveJsonToDb(JSONArray jsonArr) throws Exception {
		Date now = new Date();
		Calendar effectiveDate=Calendar.getInstance();
		
		for(int i=0;i<jsonArr.length();i++){
			JSONObject obj=jsonArr.getJSONObject(i);
			
			RRate rRate =(RRate)baseDao.getById(RRate.class, Integer.valueOf(obj.getString("id")));
			
			
			/**
			 * 根据生效日期来设置过期日期.之前的记录的失效日期为新的生效日期的前一日.
			 * 
			 */
			Date old_effectiveTime=rRate.getEffectiveTime();
			Date new_effectiveTime=DateFormatUtil.getSdf("yyyy-MM-dd").parse(obj.getString("effect_time"), new ParsePosition(0));

			
			if(new_effectiveTime.after(now)){
				effectiveDate.setTime(new_effectiveTime);
				effectiveDate.add(Calendar.DAY_OF_MONTH,-1);
				rRate.setExpireTime(effectiveDate.getTime());
				baseDao.update(rRate);
				sf.getCurrentSession().flush();   //在这个事务中,如果遇到异常也会回滚,不会写入数据库
			}

			sf.getCurrentSession().evict(rRate);

			rRate.setEffectiveTime(new_effectiveTime);
			rRate.setExpireTime(DateFormatUtil.getSdf("yyyy-MM-dd").parse(obj.getString("expire_time"), new ParsePosition(0)));
			
			rRate.setBakVosId(rRate.getVosId());  //更新s的vosId 为当前的vosId.
			rRate.setSendTime(now); //发送时间相当于修改时间
			
			if(NumberUtils.isNumber(obj.getString("rate"))){
				rRate.setRate(Double.valueOf(obj.getString("rate")));
			}
			else{
				rRate.setRate(0.0);
			}
		 
			rRate.setBillingType(obj.getString("billing_unit"));

			rRate.setRemark(obj.getString("remark"));


			baseDao.save(rRate);
			
			try{
				logService.logToDb("更新", "落地报价","新报价: "+ rRate.forLog());
			}catch(Exception e){
				logger.error("更新落地报价失败",e);
			}

			

		}
		
	}




	
	
}
