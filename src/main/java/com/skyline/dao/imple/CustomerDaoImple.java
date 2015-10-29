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

import com.skyline.dao.CustomerDao;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Partner;


@Repository("customerDao")
public class CustomerDaoImple  implements CustomerDao {
	@Autowired
	private SessionFactory sf;
	@Override
	public List<Customer> getCustomersByType(String eNum, Partner partnerType) {
		// TODO Auto-generated method stub
		Session ss =sf.getCurrentSession();
		String hql="from Customer cus where cus.ENum=:eNum and cus.CType=:partnerType";
		Query query = ss.createQuery(hql);
		query.setParameter("eNum", eNum);
		query.setParameter("partnerType", partnerType);
		
		return query.list();
	}


}
