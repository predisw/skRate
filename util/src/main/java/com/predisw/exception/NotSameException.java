package com.predisw.exception;

public class NotSameException extends Exception {
	public NotSameException(){}
	
	public NotSameException(String msg){
		super(msg);
	}
	public NotSameException(String msg,Exception cause){
		super(msg,cause);
	}
}
