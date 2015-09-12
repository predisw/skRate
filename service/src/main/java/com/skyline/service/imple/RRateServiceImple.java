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
	private PoiExcel poiExcel;
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public void saveIsrExceltoDb(String fileName, String[] excelHeaders)  throws HibernateException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		List<String[]> rRateList =poiExcel.readByPoi(fileName, 0, excelHeaders);
		for(String[] rRateArr : rRateList){
			RRate rRate = new RRate();
			//--billingUnit
			String billingUnit="";
			if(StringUtils.isNumeric(rRateArr[5]) && StringUtils.isNumeric(rRateArr[6])){
				billingUnit=rRateArr[5]+"/"+rRateArr[6];
			}
			//--code
			String code=rRateArr[3];
			if(StringUtils.isNumeric(rRateArr[4])){
				code +=rRateArr[4];
			}
		
			rRate.setVosId(rRateArr[0]);
			rRate.setBakVosId(rRateArr[0]);
			rRate.setCountry(rRateArr[1]);
			rRate.setCode(code);
			rRate.setOperator(rRateArr[2]);
			rRate.setBillingType(billingUnit);
			
			if(rRateArr[7]!=null && NumberUtils.isNumber(rRateArr[7]) ) {
				rRate.setRate(Double.valueOf(rRateArr[7]));
			}
			
			rRate.setSendTime(new Date());
			
			if(rRateArr[8]!=null){
				rRate.setEffectiveTime(DateFormatUtil.getSdf("MM-dd-yyyy").parse(rRateArr[8], new ParsePosition(0)));
			}

			if(rRateArr[9]!=null){
				rRate.setExpireTime(DateFormatUtil.getSdf("MM-dd-yyyy").parse(rRateArr[8], new ParsePosition(0)));
			}
			
			rRate.setIsAvailable(true);
			rRate.setIsCorrect(true);
			rRate.setIsSuccess(true);
			
			try{
				baseDao.save(rRate);
			}catch(HibernateException he){
				throw he;
			}

			
		}
		
		
		
	}

	@Override
	public boolean checkExcel(String fileName, String[] excelHeaders,String vosId) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		List<String[]> rRateList =poiExcel.readByPoi(fileName, 0, excelHeaders);
		return vosId.equals(rRateList.get(0)[0]);
	}

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
