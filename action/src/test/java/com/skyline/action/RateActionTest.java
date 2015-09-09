package com.skyline.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.skyline.pojo.Rate;
import com.skyline.pojo.User;
import com.skyline.service.BaseService;




@Transactional
@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class RateActionTest  {
	@Autowired
	private BaseService baseService;
	@Autowired
	private UserAction userAction;
	
	private MockHttpServletRequest req;
	private MockHttpServletResponse res;
	
	@Before
	public void setup(){
		req = new MockHttpServletRequest();
		res =new MockHttpServletResponse();
		
	}
	
	@Test
	public void abc() throws ServletException, IOException{
			
/*		User user=new User();
		user.setPassword("admin");
		user.setUName("admin");
		userAction.login(user, req, res);*/

		System.out.println(req.getContextPath());
	}

	
	
	

}
