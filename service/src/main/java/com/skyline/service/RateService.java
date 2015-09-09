package com.skyline.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.predisw.exception.NotSameException;
import com.skyline.dao.RateDao;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Rate;
import com.skyline.pojo.SendRecord;
import com.skyline.util.PageInfo;

public interface RateService  extends RateDao{



	//检查多个客户的rate 的level ,billing_type 是否一致,并返回一个Rate 对象,用于设置共同的默认值如level ,billing_unit 之类,
		//如果为空null则说明这些customer之前没有发送过报价.
	Rate doBeforeGetRate(List<Customer> cusList) throws NotSameException;
	
	//在重发邮件之前需要检查rate 数据的发送日期 是否为当天.如果不是则不能发送.
	//也就是说第二天不能重发送前一天的邮件,因为这是判断是否需要添加失效日期而导致的.
	boolean checkBfResend(SendRecord sr);
	
	//设置某个报价不正确,则同时会将同一个vosId 的,比这个报价迟的已存在报价都设置为无效
	void setMailInCorrect(List<SendRecord> srList);

}
