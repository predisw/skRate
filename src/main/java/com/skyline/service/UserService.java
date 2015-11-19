package com.skyline.service;

import java.util.List;

import com.skyline.dao.UserDao;
import com.skyline.pojo.User;

public interface UserService extends UserDao {

	List<User> getRangedUser(int firstIndex, int maxIndex);

}
