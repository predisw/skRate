package com.skyline.dao.imple;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.predisw.annotation.Description;
import com.skyline.dao.RateDao;
import com.skyline.pojo.RRate;
import com.skyline.pojo.Rate;
import com.skyline.util.PageInfo;
@Repository
public class RateDaoImple implements RateDao {
	@Autowired
	private SessionFactory sf;


	@Override
	public void setChange(String isChange) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		String hql="update Rate r set r.isChange=:isChange";
		Query query = ss.createQuery(hql);
		query.setParameter("isChange", isChange);
		query.executeUpdate();
	}
	@Override
	public void setSuccess(String rateName, boolean is_success) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		String hql="update Rate r set r.isSuccess=:is_success where r.name=:rateName";
		Query query = ss.createQuery(hql);
		query.setParameter("rateName", rateName);
		query.setParameter("is_success", is_success);
		query.executeUpdate();
		
	}
	
	@Override
	public void setIsAvailable(String vosid, String code, boolean is_available) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		String hql="update Rate r set r.isAvailable=:is_available,r.remark=:remark where r.vosId=:vosid and r.code=:code";
		Query query = ss.createQuery(hql);
		query.setParameter("remark", "unavailable");
		query.setParameter("is_available", is_available);
		query.setParameter("vosid", vosid);
		query.setParameter("code", code);
		query.executeUpdate();
	}
	
	
	@Override
	public List<Rate> getRateByCid(int cid, String code, boolean is_available,
			boolean is_success,boolean is_correct) {
		// TODO Auto-generated method stub
		Session ss = sf.getCurrentSession();
		String hql="from Rate r where r.CId=:cid and r.code=:code and r.isAvailable=:is_available and r.isSuccess=:is_success and r.isCorrect=:is_correct order by r.sendTime desc";
		Query query=ss.createQuery(hql);
		query.setParameter("cid", cid);
		query.setParameter("code", code);
		query.setParameter("is_available", is_available);
		query.setParameter("is_success", is_success);
		query.setParameter("is_correct", is_correct);
		List<Rate> list=query.list();
		return list;
	}
	@Override
	public List<Rate> getLastRateByCid(int cid, boolean is_available,
			boolean is_success,boolean is_correct) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		String sql="select * from (select * from rate where c_id=:cid and is_available=:is_available and is_success=:is_success and is_correct=:is_correct order by send_time desc) abc  group by code";
		Query query=ss.createSQLQuery(sql).addEntity(Rate.class);
		query.setParameter("cid", cid);
		query.setParameter("is_available", is_available);
		query.setParameter("is_success", is_success);
		query.setParameter("is_correct", is_correct);
		return query.list();
	}
	
	@Override
	public void upAndBakVos(String old_vosId,String new_vosId) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		String hql="update Rate r set r.vosId=:new_vosId where r.vosId=:old_vosId";
		Query query=ss.createQuery(hql);
		query.setParameter("old_vosId", old_vosId);
		query.setParameter("new_vosId", new_vosId);
		query.executeUpdate();
	}

	@Override
	public Rate getOneRate(String vosId,boolean is_available,
			boolean is_success,boolean is_correct) {
		// TODO Auto-generated method stub
		Session ss = sf.getCurrentSession();
		String hql="from Rate r where r.vosId=:vosId  and r.isAvailable=:is_available and r.isSuccess=:is_success and r.isCorrect=:is_correct order by r.sendTime desc";
		Query query=ss.createQuery(hql);
		query.setParameter("vosId", vosId);
		query.setParameter("is_available", is_available);
		query.setParameter("is_success", is_success);
		query.setParameter("is_correct", is_correct);
		query.setMaxResults(1);
		Rate rate=(Rate)query.uniqueResult();
		return rate;
	}


	


}
