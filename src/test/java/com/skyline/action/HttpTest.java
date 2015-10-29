package com.skyline.action;


import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.skyline.pojo.User;

@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class HttpTest {
	private MockHttpServletRequest req;
	private MockHttpServletResponse res;
	
	@Autowired
	private UserAction userAction;
	
	
	@Before
	public void setup(){
		req = new MockHttpServletRequest();
		res =new MockHttpServletResponse();
		
	}
	
	
	
	@Test
	public void test() throws ServletException, IOException{
		User user=new User();
		user.setPassword("admin");
		user.setUName("admin");
		userAction.login(user, req, res);
		
		System.out.println(req.getServletContext().getAttribute("WebApplicationContext.ROOT")); //为null
		
		System.out.println(req.getContextPath());  //为 空字符串"";
		
	}
}
