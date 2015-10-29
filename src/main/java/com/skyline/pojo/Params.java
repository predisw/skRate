package com.skyline.pojo;

import java.util.Map;
/**
 *  这个对象是自定义的,不是和数据库关联的;这个类是为了方便spring mvc 接收来自jsp 前台传过来的参数,如logback 的map 参数对.
 * @author predisw
 *
 */
public class Params {
	private Map<String,String>  logParams;

	public Params(){}
	
	public Params(Map<String,String>  logParams){
		super();
		this.logParams=logParams;
	}
	/**
	 * @return the logParams
	 */
	public Map<String,String> getLogParams() {
		return logParams;
	}

	/**
	 * @param logParams the logParams to set
	 */
	public void setLogParams(Map<String,String> logParams) {
		this.logParams = logParams;
	}
	
	
	
}
