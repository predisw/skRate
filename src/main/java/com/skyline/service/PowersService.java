package com.skyline.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skyline.pojo.Powers;
import com.skyline.pojo.User;

public interface PowersService {

	Set<Powers> getUserPowers(User user);

	Set<Powers> getMenuInUser(User user);

	Map<String, Boolean> getNotMenuPowersStatus(User user);

	//获取用户没有的权限的url
	List<String> getNoPowersUrl(User user);

	Set<Powers> getAllMenuPowers();

	Map<String, Boolean> getAllNMPowersStatus(Boolean status);
	
}
