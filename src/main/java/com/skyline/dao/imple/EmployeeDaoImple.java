package com.skyline.dao.imple;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skyline.dao.EmployeeDao;
import com.skyline.pojo.Employee;

@Repository
public class EmployeeDaoImple implements EmployeeDao {
//	private Logger logger = LoggerFactory.getLogger(EmployeeDaoImple.class);
	
	@Autowired
	private SessionFactory sf;
	

	@Override
	public List<Employee> getEmps(boolean status) {
		// TODO Auto-generated method stub
		Session ss= sf.getCurrentSession();
//		Transaction tx=ss.beginTransaction();
		List list=null;
		try{
			Query query=ss.createQuery("from Employee emp where emp.EStatus=:status  order by emp.ENum asc");
			query.setParameter("status", status);
			list = query.list();

//		tx.commit();
		}catch (HibernateException ex){
			ex.printStackTrace();
		}
/*		finally{
			if(ss.isOpen()){
				ss.close();
			}
		}
*/
		return list;
	}

}
