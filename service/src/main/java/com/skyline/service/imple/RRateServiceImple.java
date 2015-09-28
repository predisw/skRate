package com.skyline.service.imple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParsePosition;
import java.util.ArrayList;
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
		// TODO Auto-generated method stub

		for(int i=0;i<jsonArr.length();i++){
			JSONObject obj=jsonArr.getJSONObject(i);
			
			RRate rRate =(RRate)baseDao.getById(RRate.class, Integer.valueOf(obj.getString("id")));
			
			rRate.setSendTime(new Date()); //发送时间相当于修改时间
			
			if(NumberUtils.isNumber(obj.getString("rate"))){
				rRate.setRate(Double.valueOf(obj.getString("rate")));
			}
			else{
				rRate.setRate(0.0);
			}
			
			rRate.setBillingType(obj.getString("billing_unit"));
			rRate.setEffectiveTime(DateFormatUtil.getSdf("yyyy-MM-dd").parse(obj.getString("effect_time"), new ParsePosition(0)));
			rRate.setExpireTime(DateFormatUtil.getSdf("yyyy-MM-dd").parse(obj.getString("expire_time"), new ParsePosition(0)));
			rRate.setRemark(obj.getString("remark"));
			baseDao.save(rRate);
		}
		
	}



	
}
