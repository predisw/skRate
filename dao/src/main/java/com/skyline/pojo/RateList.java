package com.skyline.pojo;

import java.util.List;
/*
 这个对象是自定义的,不是和数据库关联的;这个类是为了方便spring mvc 接收来自jsp 前台传过来的Rate 对象列表
 */
public class RateList {
	private List<Rate> rateList;

	/**
	 * @return the rateList
	 */
	public List<Rate> getRateList() {
		return rateList;
	}

	/**
	 * @param rateList the rateList to set
	 */
	public void setRateList(List<Rate> rateList) {
		this.rateList = rateList;
	}
	
	//构造函数
	public RateList(){
		super();
	}
	
	//带参数的构造函数
	public RateList(List<Rate> rateList){
		super();
		this.rateList=rateList;
	}
	
}
