package com.skyline.dao.imple;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.predisw.annotation.Description;
import com.predisw.annotation.Log;
import com.skyline.dao.BaseDao;
import com.skyline.util.PageInfo;

@Controller("baseDao")
public class BaseDaoImple implements BaseDao {
	@Autowired
	private SessionFactory sf;
	


	@Override
	public void save(Object obj) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		ss.save(obj);
	}
	
	

	@Override
	@Log
	@Description(name="删除")
	 public void delete(Object obj){
		Session ss=sf.getCurrentSession();
		ss.delete(obj);
	 }
	
	
	@Override
	@Log
	@Description(name="修改")
	public void update(Object obj) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		ss.update(obj);
	}

	@Override
	public List getByClass(Class class_name) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		String hql="from "+" "+class_name.getSimpleName();  //获取类名
		List list=ss.createQuery(hql).list();
		
		return list;
	}

	/*
	对性能上的影响:
	1.用了反射通过class 类来获取类名
	2.拼接字符串比较多,所以用了StringBuffer ,不知是否比String 类的拼接更有效率
	 */
	@Override
	public List getByField(Class class_name, String field_name,Object field_value) {
		// TODO Auto-generated method stub
		Session ss = sf.getCurrentSession();
		String cName=class_name.getSimpleName();
	//	String hql="from "+" "+cName+" o";  //获取类名
		StringBuffer hql=new StringBuffer();
		hql.append("from ");
		hql.append(cName+" "+"o"+" ");
		hql.append("where o."+field_name+"=:field_value");
		Query query=ss.createQuery(hql.toString());
		query.setParameter("field_value", field_value);
		
		return query.list();
	}

	@Override
	public Object getById(Class cla,int id) {
		// TODO Auto-generated method stub
		Session ss =sf.getCurrentSession();
		return ss.get(cla, id);

	}

	@Override
	public void saveOrUpdate(Object obj) {
		// TODO Auto-generated method stub
		sf.getCurrentSession().saveOrUpdate(obj);
	}

	@Override
	public void delByField(Class class_name, String field_name,
			Object field_value) {
		// TODO Auto-generated method stub
		Session ss = sf.getCurrentSession();
		String cName=class_name.getSimpleName();
	//	String hql="from "+" "+cName+" o";  //获取类名
		StringBuffer hql=new StringBuffer();
		hql.append("delete from ");
		hql.append(cName+" "+"o"+" ");
		hql.append("where o."+field_name+"=:field_value");
		Query query=ss.createQuery(hql.toString());
		query.setParameter("field_value", field_value);
		query.executeUpdate();
	}

	@Override
	public void setByField(Class class_name, String field_name,
			Object field_value,String condition_field_name,Object condition_field_value) {
		// TODO Auto-generated method stub
		Session ss = sf.getCurrentSession();
		String cName=class_name.getSimpleName();
	//	String hql="from "+" "+cName+" o";  //获取类名
		StringBuffer hql=new StringBuffer();
		hql.append("update  "); //需要空格
		hql.append(cName);
		hql.append("  o  "); //两边都需要空格
		hql.append("set o.");
		hql.append(field_name+"=:field_value  "); //需要有空格
		hql.append("where o.");
		hql.append(condition_field_name+"=:condition_field_value");
		Query query=ss.createQuery(hql.toString());
		query.setParameter("field_value", field_value);
		query.setParameter("condition_field_value", condition_field_value);
		query.executeUpdate();
	}

	@Override
	public PageInfo getByPage(String hql, PageInfo page) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		Query query=ss.createQuery(hql);
		page.setTotalCount(query.list().size());  //设置总记录条数
		page.setTotalPage(page.getTotalPage());//将总条数转为总页数
		//根据当前页码,获取需要显示的数据库数据
		query.setFirstResult((page.getCurPage()-1)*page.getPageSize());  //if curPage=0 then how ?
		query.setMaxResults(page.getPageSize());
		page.setData(query.list());
		
		return page;
	}

	@Override
	public void setFiedValue(Class class_name, String field_name,
			Object field_value) {
		// TODO Auto-generated method stub
		Session ss=sf.getCurrentSession();
		String cName=class_name.getSimpleName();
		StringBuffer hql=new StringBuffer();
		hql.append("update  "); //需要空格
		hql.append(cName);
		hql.append("  o  "); //两边都需要空格
		hql.append("set o.");
		hql.append(field_name+"=:field_value"); 
		
		Query query = ss.createQuery(hql.toString());
		query.setParameter("field_value", field_value);
		query.executeUpdate();
	}



}
