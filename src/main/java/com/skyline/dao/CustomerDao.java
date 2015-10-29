package com.skyline.dao;

import java.util.List;

import com.skyline.pojo.Customer;
import com.skyline.pojo.Partner;


public interface CustomerDao{
	//某个客户某个类型的所有客户
	public List<Customer> getCustomersByType(String eNum,Partner partnerType);

}
