package com.skyline.dao.imple;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skyline.dao.BaseRateDao;
import com.skyline.pojo.BaseRate;
import com.skyline.pojo.RRate;
import com.skyline.pojo.Rate;
@Repository("baseRateDao")
public class BaseRateDaoImple implements BaseRateDao {
	@Autowired
	private SessionFactory sf;
	@Override
	public List<BaseRate> getRate(Date sDate, Date tDate, String vosId,
			String country, boolean is_success,boolean is_correct,Class rateClazz) {
		// TODO Auto-generated method stub

		Session ss=sf.getCurrentSession();
		Criteria cr=ss.createCriteria(rateClazz);
		//排序
		cr.addOrder(Order.asc("code"));
		cr.addOrder(Order.asc("sendTime"));
		//limit 数量,分页
		
		//限制条件
		Criterion cr_vosId=Restrictions.eq("vosId", vosId);
		Criterion time;
		if(sDate==null){
			time=Restrictions.le("effectiveTime", tDate);
		}else{
			time=Restrictions.between("effectiveTime", sDate, tDate);
		}
		Criterion cr_isSuccess=Restrictions.eq("isSuccess", is_success);
		Criterion cr_isCorrect=Restrictions.eq("isCorrect", is_correct);
		Conjunction cons=Restrictions.and(cr_vosId,time,cr_isSuccess,cr_isCorrect);
		//添加国家限制
		cr.add(Restrictions.and(Restrictions.eq("country", country), cons));

			
		return cr.list();
	}
	
	@Override
	public List<BaseRate> getLastRate(Date tDate,String vosId, String country, boolean is_success,boolean is_correct,Class rateClazz) {
		// TODO Auto-generated method stub
		String rateTable="";
		if(rateClazz==Rate.class){
			rateTable="rate";
		}
		if(rateClazz==RRate.class){
			rateTable="r_rate";
		}
		Session ss=sf.getCurrentSession();
		String sql="select * from (select * from "+rateTable+" where vosId=:vosId and country=:country and is_success=:is_success and is_correct=:is_correct  and effective_time<=:tDate order by send_time desc) abc group by code";
		Query query=ss.createSQLQuery(sql).addEntity(rateClazz);
//		String hql="select *  from  ( select * from Rate  r where r.vosId=:vosId and r.country=:country and r.isSuccess=:is_success and r.isCorrect=:is_correct  and r.effectiveTime<=:tDate order by r.sendTime desc  )  r group by r.code";
//		Query query=ss.createQuery(hql);
		query.setParameter("vosId", vosId);
		query.setParameter("country", country);
		query.setParameter("is_success", is_success);
		query.setParameter("is_correct", is_correct);
		query.setParameter("tDate", tDate);
		return query.list();
	}
	
	
	
	@Override
	public List getCountry(Date sDate, Date tDate, String vosId,
			 boolean is_success, Integer firstResult, Integer maxResult,boolean is_correct,Class rateClazz) {
		// TODO Auto-generated method stub
		Session ss =sf.getCurrentSession();
		Criteria cr=ss.createCriteria(rateClazz);

		//限制条件
		Criterion cr_vosId=Restrictions.eq("vosId", vosId);
		Criterion time;
		if(sDate==null){
			time=Restrictions.le("effectiveTime", tDate);
		}else{
			time=Restrictions.between("effectiveTime", sDate, tDate);
		}
		Criterion cr_isSuccess=Restrictions.eq("isSuccess", is_success);
		Criterion cr_isCorrect=Restrictions.eq("isCorrect", is_correct);
		Conjunction cons=Restrictions.and(cr_vosId,time,cr_isSuccess,cr_isCorrect);
		cr.add(cons);
		if(firstResult!=null && maxResult!=null){
			cr.setFirstResult(firstResult);
			cr.setMaxResults(maxResult);
		}
		//添加groupby
		cr.setProjection(Projections.groupProperty("country"));

		return cr.list();
	}
}
