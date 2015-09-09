package com.skyline.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.skyline.dao.LogDao;
import com.skyline.util.PageInfo;

public interface LogService extends LogDao {
	public void  modifyXml(Map<String, String> params,String fileName) throws DocumentException,IOException;
	//从logback.xml 中读取出需要设置的参数,放到Map 中.
	public Map<String, String> getParamsMap(String fileName) throws DocumentException  ;
	
	//从存放日志的文件路径处,获取后十个日志文件的名称.
	public Set<String> getLogFileName(String logDir) throws FileNotFoundException;

	
}
