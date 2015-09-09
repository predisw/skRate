package com.skyline.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.json.JSONArray;

import com.skyline.pojo.RRate;

public interface RRateService {
	
	public void saveIsrExceltoDb(String fileName,String[] excelHeaders) throws HibernateException ;

	//检查是否提交的供应商和excel 表中的一致
	
	public boolean checkExcel(String fileName,String[] excelHeaders,String vosId);
	

	public void SaveJsonToDb(JSONArray jsonArr) throws Exception;
}
