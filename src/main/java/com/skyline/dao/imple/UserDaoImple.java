package com.skyline.dao.imple;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skyline.dao.UserDao;
import com.skyline.pojo.User;
@Repository
public class UserDaoImple implements UserDao{
	@Autowired
	private SessionFactory sf;
	@Override
	public User getByName(String name) throws HibernateException{
		// TODO Auto-generated method stub
		//是get 方法，还需要事务管理吗？
		Session ss=sf.getCurrentSession();
		User user=null;
		try{
			Query query=ss.createQuery("from User u where u.UName=:name");
			query.setParameter("name", name);
			user=(User)query.uniqueResult();

		}catch(HibernateException ex){
			ex.printStackTrace();
			throw new HibernateException("用户名不唯一");
		}
		return user;
	}
	

}