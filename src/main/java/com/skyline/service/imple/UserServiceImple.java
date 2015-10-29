package com.skyline.service.imple;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.dao.UserDao;
import com.skyline.dao.imple.UserDaoImple;
import com.skyline.pojo.User;
import com.skyline.service.UserService;

@Service
public class UserServiceImple   implements UserService {
	@Autowired
	private UserDao userDao;
	
	@Override
	public User getByName(String name) throws Exception {
		// TODO Auto-generated method stub
		return userDao.getByName(name);
	}

}
