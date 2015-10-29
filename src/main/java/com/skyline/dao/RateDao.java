package com.skyline.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.skyline.pojo.Rate;
import com.skyline.util.PageInfo;

public interface RateDao {
	
	//获取客户(c_id)某些code 的记录
	public List<Rate> getRateByCid(int cid,String code,boolean is_available ,boolean is_success,boolean is_correct);
	//获取某个客户所有code 的最新记录
	public List<Rate> getLastRateByCid(int  cid,boolean is_available ,boolean is_success,boolean is_correct);
	
	//设置所有rate 记录的change 属性为"NO CHANGE"
	public void setChange(String isChange);
	//设置is_success 标志,即设置是否发送成功
	public void setSuccess(String rateName,boolean is_success);
	//设置客户(vosid)的某个code(code) 的is_available 状态 为is_available
	public void setIsAvailable(String vosid,String code,boolean is_available);
	

//	获取某个客户一个Rate 对象
	public Rate getOneRate(String vosId,boolean is_available ,boolean is_success,boolean is_correct);
	



}
