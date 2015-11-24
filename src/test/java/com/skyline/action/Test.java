package com.skyline.action;


import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.skyline.dao.BaseRateDao;
import com.skyline.pojo.BaseRate;
import com.skyline.pojo.RRate;
import com.skyline.pojo.Rate;

@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class Test {
	@Autowired
	private BaseRateDao baseRateDao;
	
	@org.junit.Test
	public void test(){
		
//	List<BaseRate> rateList=baseRateDao.getAllLastRate("0072R003CLI", RRate.class);
	//System.out.println(rateList.get(0).forLog());
	

	}
	
}
