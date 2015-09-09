package com.skyline;

import org.junit.Test;


public class TestPredisw {

	@Test
	public  void TestAdd(){
        
        Person person=new Person();
        Predisw predisw=new Predisw();

		System.out.println(predisw.getName()+":"+predisw.getAge());
	}
}
