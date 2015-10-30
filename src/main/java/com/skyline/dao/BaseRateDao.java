package com.skyline.dao;

import java.util.Date;
import java.util.List;

import com.skyline.pojo.BaseRate;
import com.skyline.pojo.Rate;

public interface BaseRateDao {
	//获取某段时间内,某个客户的某个的country(地区,大方向) 所有is_success 为true 的rate 记录,order by code ,effective_time
	//假如sDate 是null,则 表示 没有sDate<= 只有<=tDate
	public List<BaseRate> getRate(Date sDate,Date tDate,String vosId,String country,boolean is_success,boolean is_correct,Class rateClazz);
	
	//获取某个客户某个country 所有code 各自自最新的记录,按照sendTime 字段 排序
	public List<BaseRate> getLastRate(Date tDate,String vosId,String country,boolean is_success,boolean is_correct,boolean is_available,Class rateClazz);
	
	//获取所有国家数量,当firstResult 和 firstResult 都为null 时,查询所有
	//假如sDate 是null,则 表示 没有sDate<= 只有<=tDate
	public List getCountry(Date sDate,Date tDate,String vosId,boolean is_success,Integer firstResult,Integer  maxResult,boolean is_correct,Class rateClazz);

	//当修改客户的vosId时,需要同步修改rate 中对应的vosId 和,设置old_vosId
	//只修改vosId而不修改bak_vosId,bak_vosId初始值就是等于第一次写入的vosId值.新发的报价生成的bak_vosid 会替换旧的
	void upAndBakVos(String old_vosId, String new_vosId, Class rateClazz);

}
