package com.skyline.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum SingletonPropsByEnum {
	INSTANCE;
	
	private Properties properties;
	private InputStream in;
	
	private SingletonPropsByEnum(){
		in = this.getClass().getResourceAsStream("/System.properties");  //注意,这里不能改成参数的形式!!!,因为会初始化一次.
		properties = new Properties();									
	}
	
	public Properties getProperties() throws IOException{
		properties.load(in);
		return properties;
	}
}
