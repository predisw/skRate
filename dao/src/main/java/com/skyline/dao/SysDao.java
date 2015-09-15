package com.skyline.dao;

import java.io.IOException;

import org.json.JSONObject;

public interface SysDao {

	//将文件内的内容读出来,以字符串的形式返回
	public String readFileToString(String pathFileName) throws IOException;
	
	//将字符串写入文件
	public void writePerformance(String pathFileName,JSONObject json) throws IOException;
}
