package com.skyline.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import com.skyline.dao.EmployeeDao;
import com.skyline.pojo.Employee;

public interface EmployeeService  extends EmployeeDao{

	void saveExcelEmpToDb(String fileName, String[] excel_sheetHead_order,String[] empAttributeOrder) throws NumberFormatException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, FileNotFoundException, IOException;
	
	
}
