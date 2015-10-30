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
import com.skyline.pojo.Rate;
import com.skyline.service.BaseService;

@Transactional
@TransactionConfiguration(defaultRollback=false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class SaveTest {
	@Autowired
	private BaseService baseService;
	
	
	@Test
	public void test(){
		RRate rate =(RRate)baseService.getById(RRate.class, 50843);

		rate.setBakVosId("abc");
		rate.setVosId("ddda");
		rate.setRate(0.01);
		rate.setEffectiveTime(new Date());
		rate.setSendTime(new Date());
		rate.setCId(0);
		baseService.save(rate);
		
	}
}
