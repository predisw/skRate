package com.skyline.service.imple;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skyline.dao.imple.BaseRateDaoImple;
import com.skyline.pojo.BaseRate;
import com.skyline.pojo.Rate;
import com.skyline.service.BaseRateService;
import com.skyline.util.PageInfo;

@Service("baseRateService")
public class BaseRateServiceImple extends BaseRateDaoImple implements BaseRateService{
	


	
	@Override
	public PageInfo getRateByPage(Date sDate, Date tDate, String vosId,
			String country, boolean is_success, PageInfo page,boolean is_correct,Class rateClazz) {
		// TODO Auto-generated method stub
		int  firstResult=(page.getCurPage()-1)*page.getPageSize();
		int maxResult=page.getPageSize();
		List<BaseRate> rates;
		List<String> total_c=new ArrayList<>();

		if(!"all".equalsIgnoreCase(country)&&country!=null && !"".equals(country)){
			if(sDate==null){ //如果sDate(就是没有选择sDate),则返回某个country 所有code 最新的报价记录
				rates=super.getLastRate(tDate,vosId, country, is_success,is_correct,rateClazz); 
			}else{
				rates=super.getRate(sDate, tDate, vosId, country, is_success,is_correct,rateClazz);  //返回一个国家的所有rate 记录
			}
			total_c.add(country);
		}else{
			 rates=new ArrayList<>();
			List tmpRates;
			List<String> countrys=super.getCountry(sDate, tDate, vosId, is_success, firstResult, maxResult,is_correct,rateClazz); // 返回多个国家的记录
			
			if(sDate==null){ //假如没有选择 sDate,则返回多个国家各个code最新的rate 记录
				for(int i=0;i<countrys.size();i++){
					tmpRates=super.getLastRate(tDate,vosId, countrys.get(i), is_success,is_correct,rateClazz);
					rates.addAll(tmpRates);
					tmpRates.clear();
				}
			}else{
				for(int i=0;i<countrys.size();i++){
					tmpRates=super.getRate(sDate, tDate, vosId, countrys.get(i), is_success,is_correct,rateClazz);
					rates.addAll(tmpRates);
					tmpRates.clear();
				}
			}

			
			total_c=super.getCountry(sDate, tDate, vosId, is_success, null, null,true,rateClazz); //查询没有limit 限制,所有的国家
		}
		
		page.setData(rates);
		page.setTotalCount(total_c.size());
		page.setTotalPage(page.getTotalPage());
		return page;
	}
	

}
