package com.predisw.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public  class SingletonLock {

	private static class RWLock{
		private static  ReadWriteLock Lock=new ReentrantReadWriteLock();
		}
	
	public static  ReadWriteLock getSingletonReadWriteLock(){
//		System.out.println(RWLock.Lock);
		return RWLock.Lock;
	}
	
}
