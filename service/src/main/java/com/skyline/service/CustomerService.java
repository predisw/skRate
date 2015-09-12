package com.skyline.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import com.predisw.exception.UniException;
import com.skyline.dao.CustomerDao;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Partner;


public interface CustomerService extends CustomerDao{


	
	public void saveExcelCusToDb(String excelFile,String[] excel_sheetHead_order,String[] cusAttributeOrder) throws NumberFormatException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, FileNotFoundException, IOException;
}
