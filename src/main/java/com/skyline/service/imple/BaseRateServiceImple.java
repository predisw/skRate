package com.skyline.service.imple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.util.DateFormatUtil;
import com.predisw.util.NumberUtils;
import com.skyline.dao.BaseDao;
import com.skyline.dao.BaseRateDao;
import com.skyline.dao.imple.BaseRateDaoImple;
import com.skyline.pojo.BaseRate;
import com.skyline.pojo.Rate;
import com.skyline.service.BaseRateService;
import com.skyline.util.PageInfo;
import com.skyline.util.PoiExcel;

@Service("baseRateService")
public class BaseRateServiceImple  implements BaseRateService{
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private BaseRateDao baseRateDao;

	
	@Override
	public PageInfo getRateByPage(Date sDate, Date tDate, String vosId,
			String country, boolean is_success,boolean is_available, boolean is_correct,PageInfo page,Class rateClazz) {
		// TODO Auto-generated method stub
		int  firstResult=(page.getCurPage()-1)*page.getPageSize();
		int maxResult=page.getPageSize();
		List<BaseRate> rates;
		List<String> total_c=new ArrayList<>();

		if(!"all".equalsIgnoreCase(country)&&country!=null && !"".equals(country)){
			if(sDate==null && tDate!=null){ //如果sDate(就是没有选择sDate),则返回某个country 所有code 最新的报价记录
				rates=baseRateDao.getLastRate(tDate,vosId, country, is_success,is_correct,is_available,rateClazz); 
			}else{
				rates=baseRateDao.getRate(sDate, tDate, vosId, country, is_success,is_correct,rateClazz);  //返回一个国家的所有rate 记录
			}
			total_c.add(country);
		}else{
			 rates=new ArrayList<>();
			List tmpRates;
			List<String> countrys=baseRateDao.getCountry(sDate, tDate, vosId, is_success, firstResult, maxResult,is_correct,rateClazz); // 返回多个国家的记录
			
			if(sDate==null  && tDate!=null){ //假如没有选择 sDate,则返回多个国家各个code最新的rate 记录
				for(int i=0;i<countrys.size();i++){
					tmpRates=baseRateDao.getLastRate(tDate,vosId, countrys.get(i), is_success,is_correct,is_available,rateClazz);
					rates.addAll(tmpRates);
					tmpRates.clear();
				}
			}else{
				for(int i=0;i<countrys.size();i++){
					tmpRates=baseRateDao.getRate(sDate, tDate, vosId, countrys.get(i), is_success,is_correct,rateClazz);
					rates.addAll(tmpRates);
					tmpRates.clear();
				}
			}

			
			total_c=baseRateDao.getCountry(sDate, tDate, vosId, is_success, null, null,true,rateClazz); //查询没有limit 限制,所有的国家
		}
		
		page.setData(rates);
		page.setTotalCount(total_c.size());
		page.setTotalPage(page.getTotalPage());
		return page;
	}
	
	
	
	@Override
	public void saveIsrExceltoDb(String fileName, String[] excelHeaders,BaseRate newrate)  throws HibernateException, FileNotFoundException, IOException, CloneNotSupportedException {
		// TODO Auto-generated method stub
		List<String[]> rateList =poiExcel.readByPoi(fileName, 0, excelHeaders);

		for(String[] rateArr : rateList){
			BaseRate rate =(BaseRate) newrate.clone();
			//--billingUnit
			String billingUnit="";
			if(StringUtils.isNumeric(rateArr[5]) && StringUtils.isNumeric(rateArr[6])){
				billingUnit=rateArr[5]+"/"+rateArr[6];
			}
			//--code
			String code=rateArr[3];
			if(StringUtils.isNumeric(rateArr[4])){
				code +=rateArr[4];
			}
		
			
			String vosId=rateArr[0]!=null?rateArr[0].trim():null;
			String country=rateArr[1]!=null?rateArr[1].trim():null;
			code=(code!=null?code.trim():null);
			String operator = rateArr[2]!=null?rateArr[2].trim():null;
			
			rate.setVosId(vosId);
			rate.setBakVosId(vosId);
			rate.setCountry(country);
			rate.setCode(code);
			rate.setOperator(operator);
			rate.setBillingType(billingUnit);
			
			
			if(rateArr[7]!=null && NumberUtils.isNumber(rateArr[7]) ) {
				rate.setRate(Double.valueOf(rateArr[7]));
			}
			
			rate.setSendTime(new Date());
			
			if(rateArr[8]!=null){
				rate.setEffectiveTime(DateFormatUtil.getSdf("MM-dd-yyyy").parse(rateArr[8], new ParsePosition(0)));
			}

			if(rateArr[9]!=null){
				rate.setExpireTime(DateFormatUtil.getSdf("MM-dd-yyyy").parse(rateArr[9], new ParsePosition(0)));
			}
			
			rate.setIsAvailable(true);
			rate.setIsCorrect(true);
			rate.setIsSuccess(true);
			
			try{
				
				baseDao.save(rate);
			}catch(HibernateException he){
				throw he;
			}

			
		}
		
		
		
	}

	@Override
	public boolean checkExcel(String fileName, String[] excelHeaders,String vosId) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		List<String[]> rateList =poiExcel.readByPoi(fileName, 0, excelHeaders);
		return vosId.equals(rateList.get(0)[0]);
	}



	@Override
	public List<BaseRate> getRate(Date sDate, Date tDate, String vosId,
			String country, boolean is_success, boolean is_correct,
			Class rateClazz) {
		// TODO Auto-generated method stub
		return baseRateDao.getRate(sDate, tDate, vosId, country, is_success, is_correct, rateClazz);
	}



	@Override
	public List<BaseRate> getLastRate(Date tDate, String vosId, String country,
			boolean is_success, boolean is_correct,boolean is_available, Class rateClazz) {
		// TODO Auto-generated method stub
		return baseRateDao.getLastRate(tDate, vosId, country, is_success, is_correct,is_available, rateClazz);
	}



	@Override
	public List getCountry(Date sDate, Date tDate, String vosId,
			boolean is_success, Integer firstResult, Integer maxResult,
			boolean is_correct, Class rateClazz) {
		// TODO Auto-generated method stub
		return baseRateDao.getCountry(sDate, tDate, vosId, is_success, firstResult, maxResult, is_correct, rateClazz);
	}



	@Override
	public void upAndBakVos(String old_vosId, String new_vosId, Class rateClazz) {
		// TODO Auto-generated method stub
		baseRateDao.upAndBakVos(old_vosId, new_vosId, rateClazz);
	}



	@Override
	public void setChangeStatus(BaseRate oldRate, BaseRate newRate) {

		double oldRateValue=oldRate.getRate();
		double newRateValue=newRate.getRate();
		if(newRateValue>oldRateValue){
			newRate.setIsChange("Increase");
		}else if(newRateValue<oldRateValue){
			newRate.setIsChange("Decrease");
		}else newRate.setIsChange("current");
		
	}

	

	
}
