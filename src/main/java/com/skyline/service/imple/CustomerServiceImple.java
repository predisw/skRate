package com.skyline.service.imple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.annotation.Log;
import com.predisw.util.SingletonLock;
import com.skyline.dao.BaseDao;
import com.skyline.dao.BaseRateDao;
import com.skyline.dao.CustomerDao;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Partner;
import com.skyline.pojo.RRate;
import com.skyline.pojo.Rate;
import com.skyline.service.BaseService;
import com.skyline.service.CustomerService;
import com.skyline.service.LogService;
import com.skyline.util.PoiExcel;

@Service
public class CustomerServiceImple   implements CustomerService{
	@Autowired
	private BaseService baseService;
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private BaseRateDao baseRateDao;	
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private SessionFactory sf;
	@Autowired
	private CustomerDao cusDao;
	@Autowired
	private LogService logService;
	

	
	
	@Override
	public void saveExcelCusToDb(String excelFile,String[] excel_sheetHead_order,String[] cusAttributeOrder) throws NumberFormatException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, FileNotFoundException, IOException {
			// TODO Auto-generated method stub
		
		List cusList=poiExcel.readByPoi(excelFile, 0, excel_sheetHead_order);

		String[] cus_value;

		SimpleDateFormat str_date =null;//这里没用到,格式化时间
		for(int i=0;i<cusList.size();i++){
			cus_value=(String[])cusList.get(i);
			 Customer cus= new Customer();  //必须.....

			cus=(Customer)poiExcel.setStringArrayToObj(cus_value, cusAttributeOrder, cus, str_date);
			cus.setVosId(cus.getVosId().trim());
			baseService.saveOrReplaceIfDup(cus, "vosId");
		}
	}

	@Log
	@Override
	public void updateCus(Customer cus){
		
		Customer db_cus=(Customer)baseDao.getById(Customer.class, cus.getCId());
		
		Class rateClazz =null;
		
		if(Partner.CUSTOMER.equals(cus.getCType())){
			rateClazz=Rate.class;
		}
		if(Partner.PROVIDER.equals(cus.getCType())){
			rateClazz=RRate.class;
		}
		
		
		//假如修改了vosId
		if(!db_cus.getVosId().equals(cus.getVosId())){ 

			//修改了vosId 和工号eNum,也要对象的修改关联的数据.rate 表和customer 表的vosId 数值需要跟着更改.eNum 只有rate 有关联.
			baseRateDao.upAndBakVos(db_cus.getVosId(), cus.getVosId(),rateClazz);  //rateClazz 为null,会抛空值异常
		}

		//-------工号是否被修改
		if(!db_cus.getENum().equals(cus.getENum())){ //假如修改了工号,也要把rate 中的对应的修改
			baseDao.setByField(rateClazz, "ENum", cus.getENum(), "ENum", db_cus.getENum());
		}
		if(sf.getCurrentSession().isOpen()){
			sf.getCurrentSession().evict(db_cus);
		}
		
		baseDao.update(cus);
		
		try{
			String content= "before/"+db_cus.toString()+"after/"+cus.toString();
			logService.logToDb("更新客户", "", content);
		}catch(Exception e){
		}
		
	}


	@Override
	public List<Customer> getCustomersByType(String eNum, Partner partnerType) {
		// TODO Auto-generated method stub
		return cusDao.getCustomersByType(eNum, partnerType);
	}
	
	
}
