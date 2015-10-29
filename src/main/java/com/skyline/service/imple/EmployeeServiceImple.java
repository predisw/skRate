package com.skyline.service.imple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.dao.EmployeeDao;
import com.skyline.dao.imple.EmployeeDaoImple;
import com.skyline.pojo.Employee;
import com.skyline.service.BaseService;
import com.skyline.service.EmployeeService;
import com.skyline.util.PoiExcel;
import com.skyline.util.SingletonProps;
@Service
public class EmployeeServiceImple extends EmployeeDaoImple implements EmployeeService {
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private BaseService baseService;
	
	
	@Override
	public void saveExcelEmpToDb(String fileName, String[] excel_sheetHead_order,String[] empAttributeOrder) throws NumberFormatException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, FileNotFoundException, IOException{

		
		SimpleDateFormat str_date =null;
		
		
		List list=poiExcel.readByPoi(fileName, 0, excel_sheetHead_order);
		
		
		String[] dbvalues;
		for(int i=0;i<list.size();i++){
			dbvalues=(String[])list.get(i);
			Employee emp = new Employee();
			emp=(Employee)poiExcel.setStringArrayToObj(dbvalues, empAttributeOrder, emp, str_date);
			baseService.saveOrReplaceIfDup(emp,"ENum");
		}
		
	}

}
