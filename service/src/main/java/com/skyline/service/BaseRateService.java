package com.skyline.service;


import java.util.Date;

import com.skyline.dao.BaseRateDao;
import com.skyline.util.PageInfo;

public interface BaseRateService extends BaseRateDao{
	//获取客户历史报价的分页,假如 country 为 "all" 则返回所有的
	public PageInfo getRateByPage(Date sDate, Date tDate, String vosId,String country, boolean is_success, PageInfo page,boolean is_correct,Class rateClazz);
}
