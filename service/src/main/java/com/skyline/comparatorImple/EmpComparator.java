package com.skyline.comparatorImple;

import java.util.Comparator;

import com.skyline.pojo.Employee;

public class EmpComparator implements Comparator<Employee>{

	@Override
	public int compare(Employee o1, Employee o2) {
		// TODO Auto-generated method stub
		return o1.getENum().compareToIgnoreCase(o2.getENum());
	}

}
