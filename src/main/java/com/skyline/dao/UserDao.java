package com.skyline.dao;

import com.skyline.pojo.User;
public interface UserDao {

	User getByName(String name) throws Exception;

}
