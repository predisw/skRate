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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.util.DateFormatUtil;
import com.predisw.util.NumberUtils;
import com.skyline.dao.BaseDao;
import com.skyline.pojo.RRate;
import com.skyline.service.RRateService;
import com.skyline.util.PoiExcel;
@Service
public class RRateServiceImple implements RRateService {

	@Autowired
	private BaseDao baseDao;
	


	@Override
	public  void SaveJsonToDb(JSONArray jsonArr) throws Exception {

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

			
			if(new_effectiveTime.after(old_effectiveTime)){
				effectiveDate.setTime(new_effectiveTime);
				effectiveDate.add(Calendar.DAY_OF_MONTH,-1);
				rRate.setExpireTime(effectiveDate.getTime());
				baseDao.update(rRate);
			}



			rRate.setEffectiveTime(new_effectiveTime);
			rRate.setExpireTime(DateFormatUtil.getSdf("yyyy-MM-dd").parse(obj.getString("expire_time"), new ParsePosition(0)));
			
			rRate.setSendTime(new Date()); //发送时间相当于修改时间
			
			if(NumberUtils.isNumber(obj.getString("rate"))){
				rRate.setRate(Double.valueOf(obj.getString("rate")));
			}
			else{
				rRate.setRate(0.0);
			}
		 
			rRate.setBillingType(obj.getString("billing_unit"));

			rRate.setRemark(obj.getString("remark"));

			baseDao.save(rRate);
		}
		
	}




	
	
}
