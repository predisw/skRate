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

		String i="";
		long start = System.currentTimeMillis();
		System.out.println("浪费内存中.....");
		for(int j = 0;j < 10000;j++)
		{
			i += j;
		}
		
		long end = System.currentTimeMillis();
		System.out.println("执行此程序总共花费了" + ( end - start)+ "毫秒");
		System.out.println("已用内存" + myRun.totalMemory()/(1024*1024));
		System.out.println("最大内存" + myRun.maxMemory()/(1024*1024));
		System.out.println("可用内存" + myRun.freeMemory()/(1024*1024));
		myRun.gc();
		System.out.println("清理垃圾后");
		System.out.println("已用内存" + myRun.totalMemory()/(1024*1024));
		System.out.println("最大内存" + myRun.maxMemory()/(1024*1024));
		System.out.println("可用内存" + myRun.freeMemory()/(1024*1024));
	}
}
