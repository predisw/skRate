package com.skyline.action;


import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.skyline.dao.BaseRateDao;
import com.skyline.pojo.RRate;

@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class Test {
	@Autowired
	private BaseRateDao baseRateDao;
	
	@org.junit.Test
	public void test(){
		
		
		List list=baseRateDao.getCountry(null, null, "0039R013", true, 1, 10,true, RRate.class);
		List rList = baseRateDao.getRate(null, null, "0039R013", "Afghanistan", true, true, RRate.class);
		

	}
	
}
