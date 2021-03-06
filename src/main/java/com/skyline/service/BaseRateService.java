package com.skyline.service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.hibernate.HibernateException;

import com.skyline.dao.BaseRateDao;
import com.skyline.pojo.BaseRate;
import com.skyline.util.PageInfo;

/**
 * * @author predisw
 *rRate 类和rate 类都需要用到的service
 */
public interface BaseRateService extends BaseRateDao{
	//获取客户历史报价的分页,假如 country 为 "all" 则返回所有的
	public PageInfo getRateByPage(Date sDate, Date tDate, String vosId,String country, boolean is_success,boolean is_correct,boolean is_available, PageInfo page,Class rateClazz);


	void saveIsrExceltoDb(String fileName, String[] excelHeaders, BaseRate rate)			throws HibernateException, FileNotFoundException, IOException, CloneNotSupportedException;


	boolean checkExcel(String fileName, String[] excelHeaders, String vosId)			throws FileNotFoundException, IOException;
	

	public void setChangeStatus(BaseRate oldRate, BaseRate newRate);


	void setRatesCorrected(String[] ids, Class<? extends BaseRate> clazz,boolean is_corrected);
}
