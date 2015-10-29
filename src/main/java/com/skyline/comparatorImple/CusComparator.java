package com.skyline.comparatorImple;

import java.util.Comparator;

import com.skyline.pojo.Customer;

public class CusComparator implements Comparator<Customer> {

	@Override
	public int compare(Customer cus1, Customer cus2) {
		// TODO Auto-generated method stub
		return cus1.getVosId().compareToIgnoreCase(cus2.getVosId());
		

	}


}
