package com.skyline.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.skyline.pojo.Log;
import com.skyline.util.PageInfo;

public interface LogDao {
	public PageInfo getPageOfOpLog(PageInfo page,Map<String,Object> cons);
	
	
	//和save 一样的方法,只是因为需要对save 调用aop ,所以新建一个不同名的保存日志.
	public void saveLog(Log log );
}
