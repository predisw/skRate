package com.skyline.service.imple;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.dao.imple.CountryCodeDaoImple;
import com.skyline.pojo.CountryCode;
import com.skyline.service.BaseService;
import com.skyline.service.CountryCodeService;
import com.skyline.util.PageInfo;


@Service("countryCodeService")
public class CountryCodeServiceImple extends CountryCodeDaoImple  implements CountryCodeService{

	@Autowired 
	private BaseService baseService;

	
	//只把country ，operator 和code 字段加到数据库中的country 表中
		public void saveISRListToCcTable(List<String[]> ccList) throws  NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			// TODO Auto-generated method stub
			
			for(int i=0;i<ccList.size();i++){
				String[]  ccs =(String[]) ccList.get(i); //countryCode list
				CountryCode cc = new CountryCode();
				cc.setCountry(ccs[0].trim());
				cc.setCCode(ccs[2].trim());
				cc.setOperator(ccs[1].trim());
				if(ccs[3]==null){
					ccs[3]="";
				}
				cc.setCode(ccs[2].trim()+ccs[3].trim());
				baseService.saveOrReplaceIfDup(cc, "code");
			}
		}


	@Override
	public void delBulkById(String[] idArr) {
		// TODO Auto-generated method stub

		for(int i=0;i<idArr.length;i++){
			if(!idArr[i].equals("")){

				baseService.delete( baseService.getById(CountryCode.class, Integer.parseInt(idArr[i])) );
			}
		}
		
	}
	
			
	@Override
	public void setOftenCountrys(boolean is_often,String[] ccIds) {
		// TODO Auto-generated method stub
		for(String ccId:ccIds){
			super.setOftenCountry(is_often, Integer.parseInt(ccId));
		}

	}
	
		
		
		
		
		
		
}
