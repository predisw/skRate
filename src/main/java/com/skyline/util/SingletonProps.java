package com.skyline.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//实际本质上就是只返回一个SingletonProps 实例.
public class SingletonProps {

	private Properties properties;
	private 	InputStream in;
//	public static int ii =0;
	private SingletonProps()    {
		in = this.getClass().getResourceAsStream("/System.properties");  //注意,这里不能改成参数的形式!!!,因为会初始化一次.
		properties = new Properties();																								
//		ii=ii+1;
	}; //私有
	//静态私有内部类
	private static class SingletonHolder   {
		private static final SingletonProps singleton_Props  =new SingletonProps();
	}
	
	public static final SingletonProps getInstance()  {
		return SingletonHolder.singleton_Props;   //无论什么时候调用都只返回一个singletonProps 类的实例
	}
	
	public Properties getProperties() throws IOException{
		properties.load(in);
		return properties;
	}
	
	
	
	

}
