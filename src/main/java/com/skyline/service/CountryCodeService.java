package com.skyline.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.predisw.exception.UniException;
import com.skyline.dao.CountryCodeDao;
import com.skyline.pojo.CountryCode;
import com.skyline.util.PageInfo;

public interface CountryCodeService  extends CountryCodeDao  {
	

	
	//从ISR 导出的excel 中导入地区信息数据到数据表CountryCode中,是专门为ISR 导出excel 表设计的
	public void saveISRListToCcTable(List<String[]> ccList) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	//根据id 数组中的id 删掉对应的对象
	public void delBulkById(String[]  idArr);

	//设置多个国家为常用国家
	void setOftenCountrys(boolean is_often, String[] ccIds);
	

	
}
