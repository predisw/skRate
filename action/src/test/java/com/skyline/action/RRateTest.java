package com.skyline.action;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.skyline.pojo.RRate;
import com.skyline.service.BaseService;
@Transactional
@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class RRateTest {
	@Autowired
	private BaseService baseService;
	@Test
	public void TestAddRRate(){
		
		RRate rRate=new RRate();
		rRate.setSendTime(new Date());

		
		baseService.save(rRate);
		
	}
	
}
