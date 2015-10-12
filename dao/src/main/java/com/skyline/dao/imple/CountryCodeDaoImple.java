package com.skyline.dao.imple;

import java.util.Collections;
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


import com.skyline.dao.CountryCodeDao;
import com.skyline.pojo.CountryCode;
import com.skyline.util.PageInfo;

@Repository("countryCodeDao")
public class CountryCodeDaoImple  implements CountryCodeDao {
//	Logger logger=LoggerFactory.getLogger(CountryCodeDaoImple.class);
	@Autowired
	private SessionFactory sf;

	
	//将conditions 条件的结果进行分页
	//hibernate 写入数据库时,如果字符串类型是null ,则将空字符串写入""
	@Override
	public PageInfo getCountrys(PageInfo page ,String[] conditions) {
		// TODO Auto-generated method stub
		Session ss = sf.getCurrentSession();
		try{
//			Transaction tx=ss.beginTransaction();
			Query query;
			Query countQuery;
			if( (conditions[0]==null || "".equals(conditions[0]) )  && (conditions[1]==null ||"".equals(conditions[1]) ) ){
				query=ss.createQuery("from CountryCode cc order by cc.country ");

				countQuery=ss.createQuery("select count(*) from CountryCode");
				List countList=countQuery.list();
				page.setTotalCount(Integer.parseInt(countList.get(0).toString()));  //设置总记录条数
//				System.out.println(list.get(0).toString());//会转为字符串吗?
				page.setTotalPage(page.getTotalPage());//将总条数转为总页数
				
		}else{
				query=ss.createQuery("from CountryCode cc where cc.country like :scountry or cc.code like:scode");
				
				countQuery=ss.createQuery("select count(*) from CountryCode cc where cc.country like :scountry or cc.code like:scode");
	
				
				if("".equals(conditions[0])||conditions[0]==null){
					query.setParameter("scountry", null);
					countQuery.setParameter("scountry", null);
				}else{
					query.setParameter("scountry", conditions[0]+"%");
					countQuery.setParameter("scountry", conditions[0]+"%");
				}
				if("".equals(conditions[1])||conditions[1]==null){
					query.setParameter("scode", null);
					countQuery.setParameter("scode", null);
				}else{
					query.setParameter("scode", conditions[1]+"%");
					countQuery.setParameter("scode", conditions[1]+"%");
				}
				
				List countList=countQuery.list();
				page.setTotalCount(Integer.parseInt(countList.get(0).toString()));  //设置总记录条数
//				System.out.println(list.get(0).toString());//会转为字符串吗?
				page.setTotalPage(page.getTotalPage());//将总条数转为总页数
			}
			query.setFirstResult((page.getCurPage()-1)*page.getPageSize());  //if curPage=0 then how ?
			query.setMaxResults(page.getPageSize());
			List list = query.list();

			page.setData(list); //将查到当页的内容返回
			//之后 query 和list 指向其他的 query和list 会对之前的有影响吗?
			//实践证明是没有影响的,因为list 的内容由setData 让其他的list 指向了,而之前的query 不再需要了,可以释放掉 
			

//			tx.commit();
			return page; //只有返回了page,页面page 才有信息
		}catch(HibernateException e){
			e.printStackTrace();
		}
	
		return page;  //关键
	}


	@Override
	public List<CountryCode> getCountrys() {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
	//	Transaction tx = ss.beginTransaction();
		List list=null;
		try{
			String hql="from CountryCode cc group by cc.country,cc.CCode";
			Query query =ss.createQuery(hql);
			list=query.list();
//			tx.commit();
	//		logger.debug("[{}] kinds of country name returned ",list.size());
		}catch(HibernateException ex){
			ex.printStackTrace();
		}
/*		finally{
			if(ss.isOpen()){
				ss.close();
			}
		}*/
		
		
		return list;
	}


	@Override
	public void setOftenCountry(boolean is_often,int ccId) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
//		Transaction tx = ss.beginTransaction();
		try{
			String hql="update CountryCode cc set cc.ctyOften=:is_often where cc.ccId=:ccId";
			Query query =ss.createQuery(hql);
			query.setParameter("is_often", is_often);
			query.setParameter("ccId", ccId);
			query.executeUpdate();
//			tx.commit();
		}catch(HibernateException ex){
			ex.printStackTrace();
		}
/*		finally{
			if(ss.isOpen()){
				ss.close();
			}
		}*/
	
	}



	@Override
	public List<CountryCode> getOfenCountrys(boolean is_often) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
	//	Transaction tx = ss.beginTransaction();
		List list=null;
		try{
			String hql=" from CountryCode cc where cc.ctyOften=:is_often";
			Query query =ss.createQuery(hql);
			query.setParameter("is_often", is_often);
			list=query.list();
//			tx.commit();
//			logger.debug("[{}] oftened country are  returned ",list.size());
		}catch(HibernateException ex){
			ex.printStackTrace();
		}
/*		finally{
			if(ss.isOpen()){
				ss.close();
			}
		}*/
	
		
		
		return list;
	}


	@Override
	public List<CountryCode> getOperators(String country) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
//		Transaction tx = ss.beginTransaction();
		List list=null;
		try{
			String hql=" from CountryCode cc where cc.country=:country";
			Query query =ss.createQuery(hql);
			query.setParameter("country", country);
			list=query.list();
//			tx.commit();
//			logger.debug("there are [{}] records belonged to [{}]  are returned ",list.size(),country);
		}catch(HibernateException ex){
			ex.printStackTrace();
		}
/*		finally{
			if(ss.isOpen()){
				ss.close();
			}
		}*/
		return list;
	}



}
