package com.skyline.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.skyline.util.PageInfo;

public interface LogDao {
	public PageInfo getPageOfOpLog(PageInfo page,Map<String,Object> cons);
}
