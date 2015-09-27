package com.skyline.dao;

import java.io.IOException;
import java.nio.file.Path;

import org.json.JSONObject;

public interface SysDao {

	//将文件内的内容读出来,以字符串的形式返回
	public String readFileToString(Path path) throws IOException;
	
	//将字符串写入文件
	public void writeStringToFile(Path path,String  content) throws IOException;

	void moveAndinit(Path source, Path target) throws IOException;

	//将初始化数据写入了文件
	void initPerformance(Path path) throws IOException;

	String createPerformance(String content) throws IOException;
	
	
}
