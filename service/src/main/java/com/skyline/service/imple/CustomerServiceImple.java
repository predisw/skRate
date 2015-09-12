package com.skyline.service.imple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.dao.imple.CustomerDaoImple;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Partner;
import com.skyline.service.BaseService;
import com.skyline.service.CustomerService;
import com.skyline.util.PoiExcel;

@Service
public class CustomerServiceImple extends CustomerDaoImple implements CustomerService{
	@Autowired
	private BaseService baseService;
	@Autowired
	private PoiExcel poiExcel;
	
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
//			 System.out.println("xxxxxxxxxx:"+cus.getCcEmail());
			baseService.saveOrReplaceIfDup(cus, "vosId");
		}
	}


	
}
