package com.skyline.schedule;

import java.lang.Thread.UncaughtExceptionHandler;



public class DefaultUnCatchException implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
		System.out.println("fffffffffffffffffffffffffffffffffff");
//		t.currentThread().interrupt();
	//	throw new  RuntimeException(e);
	}

}
