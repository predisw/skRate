package com.skyline.dao;

import java.util.List;

import com.skyline.pojo.Employee;

public interface EmployeeDao {
		public List<Employee> getEmps(boolean status);
}
