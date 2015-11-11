package com.skyline.service.imple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.dao.BaseDao;
import com.skyline.pojo.Powers;
import com.skyline.pojo.Role;
import com.skyline.pojo.User;
import com.skyline.service.PowersService;

@Service("powersService")
public class PowersServiceImple implements PowersService {

	@Autowired
	private BaseDao baseDao;
	
	@Override
	public Set<Powers> getUserPowers(User user) {
		Set<Role> roles = user.getRoles();
		Set<Powers> powers = new TreeSet<Powers>();
		for(Role role:roles){
			powers.addAll(role.getPowerses());
		}
		return powers;
	}
	
	
	@Override
	public Set<Powers> getMenuInUser(User user){
		
		Set<Powers> powers =getUserPowers(user);
		Iterator<Powers> ite = powers.iterator();
		while(ite.hasNext()){
			if(ite.next().getIsMenu()!=true){
				ite.remove();
			}
			
		}
		
/*		//抛出异常ConcurrentModificationException
	for(Powers power:powers){
			if(power.getIsMenu()!=true){
				powers.remove(power);
			}
		}*/
		
		return powers;
	}
	
	
	@Override
	public  Map<String,Boolean> getNotMenuPowersStatus(User user){
		//user 拥有的非目录权限
		Set<Powers> ownedNMPowers=getUserPowers(user);
		ownedNMPowers.removeAll(getMenuInUser(user));
		//所有非目录权限
		List<Powers> allNMPowers=baseDao.getByField(Powers.class, "isMenu", false);
		
		Map<String,Boolean> pStatus=new TreeMap<>();
		
		for(Powers power:allNMPowers){
			Boolean status = false;
			if(ownedNMPowers.contains(power)){
				status=true;
			}
			System.out.println("xxxxxxxxxxxx第三级权限的value: "+status+"/key: "+power.getKeyName());
			pStatus.put(power.getKeyName(), status);
		}

		return pStatus;
	}


	@Override
	public List<String> getNoPowersUrl(User user){
		List<String> powersUrl=new ArrayList<>();
		
		List<Powers> allPowers=baseDao.getByClass(Powers.class);
		
		Set<Powers> ownedPowers=getUserPowers(user);
		
		allPowers.removeAll(ownedPowers);

		for(Powers power:allPowers){
			powersUrl.add(power.getUrl());
		}
		
		return powersUrl;
	
	}
	
	
	
}
