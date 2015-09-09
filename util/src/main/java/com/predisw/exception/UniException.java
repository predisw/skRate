package com.predisw.exception;



//自定义的唯一异常,假如数据不唯一可以抛出此异常通知上层
public class UniException extends Exception{
	
	public UniException(){}
	
	public UniException(String msg){
		super(msg);
	}
	public UniException(String msg,Exception cause){
		super(msg,cause);
	}
	
}
