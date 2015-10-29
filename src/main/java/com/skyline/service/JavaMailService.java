package com.skyline.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;

import com.skyline.pojo.Email;

public interface JavaMailService {
	//给toList 地址送一个带有fileName 附件的邮件
	//toList 是一个邮箱地址或者多个以"," 逗号分开的邮箱地址,
	
	public boolean sendMail(Email email) throws IOException, AddressException, MessagingException;
	//缩放图片
	public void zoomImg(String srcImg,String destImg,int x,int y) throws FileNotFoundException, IOException;
	
	
	//接收邮件
	public void receiveEmail() throws IOException,  MessagingException;
	
}
