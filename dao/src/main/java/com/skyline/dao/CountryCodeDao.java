package com.skyline.dao;

import java.util.List;

import com.skyline.pojo.CountryCode;
import com.skyline.util.PageInfo;

public interface CountryCodeDao{

	
	public PageInfo getCountrys(PageInfo page ,String[] conditions);

	//获取不重复国家名的国家列表
	public List<CountryCode> getCountrys();
	//设置哪些国家是要经常使用的，设置经常用的为true,false 为不常用
	public void setOftenCountry(boolean is_often,int ccId);
	//获取经常使用的国家列表,true表示经常
	public List<CountryCode> getOfenCountrys(boolean is_often);
	//通过id 获取国家
	//public CountryCode getCountryCode(int ccId);
	//获取一个国家同名的所有记录,用来获取同一个国家的所有operator
	public List<CountryCode> getOperators(String country);
	
}
