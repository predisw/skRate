package com.skyline.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.skyline.pojo.Log;
import com.skyline.util.PageInfo;
//公有数据操作接口
public interface BaseDao {

	public void save(Object obj) ;
	public void update(Object obj);
	public void saveOrUpdate(Object obj);

	public void delete(Object obj);
	
	//通过类名获取该类的所有对象
//	public void delById(Class cla,int id);

	//通过id 获取对象
	public Object getById(Class cal,int id);
	
	public List getByClass(Class cla);
	//通过类的某个属性获取有这个属性的所有对象
	
	public List getByField(Class class_name,String field_name,Object field_value);
	//通过某个类的某项属性来删掉某个/些对象
	public void delByField(Class class_name,String field_name,Object field_value);
	//设置某个类所有记录的某个属性的值都为field_value
	public void setByField(Class class_name,String field_name,Object field_value,String condition_field_name,Object condition_field_value);
	//设置所有的某个域的值都为field_value
	public void setFiedValue(Class class_name,String field_name,Object field_value);

	//分页,保存数据到Pageinfo 对象中,在显示层,将其显示出来
	public PageInfo getByPage(String hql,PageInfo page);


//	public <T> List<T> getByDetachedCriteria(final DetachedCriteria detachedCriteria);
	public <T> List<T> getRanged(final DetachedCriteria criteria,int firstIndex,int maxIndex);
	
}
