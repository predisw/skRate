package com.predisw.test;

import java.util.Map;

import org.hibernate.hql.internal.ast.tree.MapEntryNode;
import org.junit.Test;

public class RuntimeTest {
	@Test
	public void RuntimeTest(){
		
		
		
		System.out.println(System.getProperty("os.name"));
		Runtime myRun=Runtime.getRuntime();
		System.out.println("total memory mbs"+Runtime.getRuntime().totalMemory()/(1024*1024));
		System.out.println("max memory mbs"+Runtime.getRuntime().maxMemory()/(1024*1024));
		System.out.println("free memory mbs"+Runtime.getRuntime().freeMemory()/(1024*1024));

	}
}
