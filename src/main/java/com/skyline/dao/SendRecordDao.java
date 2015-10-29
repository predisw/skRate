package com.skyline.dao;

import java.util.Date;
import java.util.List;

import com.skyline.pojo.SendRecord;

public interface SendRecordDao {
	//发送状态(1,发送成功;0发送中;-1 发送失败)

	//返回一个客户最新的发送记录
	public SendRecord getLastExcelTp(String vosId);
	
	//获取 某个客户 某个发送时间之后(大于)的所有发送成功的发送记录
	public List<SendRecord> getSRAfterSendTime(Date sendTime,int c_id);
}
