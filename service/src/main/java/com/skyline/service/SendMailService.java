package com.skyline.service;

import java.util.List;

import com.skyline.pojo.Email;
import com.skyline.pojo.Rate;
import com.skyline.pojo.SendRecord;

public interface SendMailService {

	public void sendMail(Email email,SendRecord successedSR,SendRecord failedSR) throws Exception;

	void setETAfterMail(List<Rate> rateList);
	//将新增的发送的rate /email内容写到数据库中,同时将一份发送记录sendRecord对象写到数据库中
	void saveForMail(List<Rate> rateList, SendRecord sr, Email email);
}
