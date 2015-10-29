package com.predisw.test;

import com.predisw.exception.UniException;

public class ExceptionTest {
	

	
	public ExceptionTest() throws UniException{

		throw new UniException();
	}
	
	
	
	
	public static void main(String[] args){

		try{
			   ExceptionTest etest=new ExceptionTest();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
