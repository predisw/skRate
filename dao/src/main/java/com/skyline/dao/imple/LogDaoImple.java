package com.skyline.dao.imple;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skyline.dao.LogDao;
import com.skyline.pojo.Log;
import com.skyline.util.PageInfo;
@Repository("logDao")
public class LogDaoImple implements LogDao{
	@Autowired
	private SessionFactory sf;
	
	@Override
	public PageInfo getPageOfOpLog(PageInfo page, Map<String,Object> cons) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		Criteria cr=ss.createCriteria(Log.class);
		cr.addOrder(Order.desc("time"));
		

		Criterion user=Restrictions.eq("who", cons.get("userName")); //cons.get("userName") 为null ,ok 否?
		Criterion time; //默认tDate 不为空

		if(cons.get("fDate")==null){
			time=Restrictions.le("time", cons.get("tDate"));
		}else{
			time=Restrictions.between("time", cons.get("fDate"), cons.get("tDate"));
		}
		

		
		if(!"".equals(cons.get("userName")) && cons.get("userName")!=null ){
			cr.add(Restrictions.and(time, user));
		}else{
			cr.add(time);
		}
		//总共
		List list_total =cr.list();
		page.setTotalCount(list_total.size());  //设置总记录条数
		page.setTotalPage(page.getTotalPage());//将总条数转为总页数
	
		//每页
		cr.setFirstResult((page.getCurPage()-1)*page.getPageSize());
		cr.setMaxResults(page.getPageSize());
		List list =cr.list();
		page.setData(list);

		
		return page ;
	}

	@Override
	public void saveLog(Log log) {
		// TODO Auto-generated method stub
		sf.getCurrentSession().save(log);
	}

}
