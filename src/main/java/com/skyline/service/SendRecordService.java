package com.skyline.service;

import java.util.List;

import com.skyline.dao.SendRecordDao;
import com.skyline.pojo.SendRecord;

public interface SendRecordService extends SendRecordDao{
	
	
	//设置某个报价不正确,则同时会将同一个vosId 的,比这个报价迟的已存在报价都设置为无效
	void setMailInCorrect(List<SendRecord> srList);
}
