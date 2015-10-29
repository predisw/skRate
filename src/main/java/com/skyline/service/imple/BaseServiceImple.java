package com.skyline.service.imple;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.predisw.exception.UniException;
import com.skyline.pojo.Employee;
import com.skyline.service.BaseService;
import com.skyline.util.PageInfo;
import com.skyline.util.PoiExcel;
import com.skyline.util.SingletonProps;
import com.skyline.dao.BaseDao;
import com.skyline.dao.imple.BaseDaoImple;

@org.springframework.stereotype.Service("baseService")
public class BaseServiceImple   implements BaseService { //通过继承来调用底层数据操作
	
	@Autowired
	private SessionFactory sf;
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public boolean isUnique(Class class_name, String field_name,
			Object field_value) {
		// TODO Auto-generated method stub
		List list=baseDao.getByField(class_name, field_name, field_value)	;
		return list.size()<1; //假如为0 则表示是新添加的
	}
	
	@Override
	public Object updateUniCheck(Object obj, String field_name,Object field_value,String id_field_name)
			throws UniException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		String getIdStr="get"+ id_field_name.substring(0, 1).toUpperCase()+ id_field_name.substring(1);
		String getFieldStr="get"+ field_name.substring(0, 1).toUpperCase()+ field_name.substring(1);
		Method getId=obj.getClass().getMethod(getIdStr);//就是getId();
		Object db_obj = baseDao.getById(obj.getClass(),(Integer)getId.invoke(obj));
		Method getField=obj.getClass().getMethod(getFieldStr); //fiel_name 的get方法
		
		if(!getField.invoke(db_obj).equals(getField.invoke(obj))){ //假如修改了vosId
			if(!this.isUnique(obj.getClass(), field_name, field_value)){
				throw new UniException(field_name+":"+field_value+" 已存在");
			}
		}
		if(sf.getCurrentSession().isOpen()){
			sf.getCurrentSession().evict(db_obj);
		}
		return obj;
	}

	@Override
	public Object addUniCheck(Object obj, String field_name,Object field_value)
			throws UniException {
		// TODO Auto-generated method stub
		if(!this.isUnique(obj.getClass(), field_name, field_value)){
			throw new UniException(field_name+":"+field_value+" 已存在");
		}
		return obj;

	}

	@Override
	public void saveOrReplaceIfDup(Object obj ,String field_name) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub

		String methodStr="get"+field_name.substring(0, 1).toUpperCase()+ field_name.substring(1);
		Method method=obj.getClass().getMethod(methodStr);
		
		if( baseDao.getByField(obj.getClass(), field_name, method.invoke(obj)).size()>0 ){ //假如存在 在属性域上和obj 相等的对象,则删除.
			baseDao.delByField( obj.getClass(), field_name, method.invoke(obj) );
			
		}
		baseDao.save(obj);

	}


	@Override
	public void updateBulk(Collection<? extends Object> collection) {
		// TODO Auto-generated method stub
		for(Object obj:collection){
			baseDao.update(obj);
			}
		}

	@Override
	public void saveBulk(Collection<? extends Object> collection) {
		// TODO Auto-generated method stub
		for(Object obj:collection){
			baseDao.save(obj);
			}
		}

	@Override
	public void delBulkByids(Class clazz, int[] ids) {
		// TODO Auto-generated method stub
		
		for(int id:ids){
			baseDao.delete(baseDao.getById(clazz, id));
		}

		
		
	}

	
	
	
	@Override
	public void save(Object obj) {
		// TODO Auto-generated method stub
		baseDao.save(obj);
	}

	@Override
	public void update(Object obj) {
		// TODO Auto-generated method stub
		baseDao.update(obj);
	}

	@Override
	public void saveOrUpdate(Object obj) {
		// TODO Auto-generated method stub
		baseDao.saveOrUpdate(obj);
	}

	@Override
	public void delete(Object obj) {
		// TODO Auto-generated method stub
		baseDao.delete(obj);
	}

	@Override
	public Object getById(Class cal, int id) {
		// TODO Auto-generated method stub
		return baseDao.getById(cal, id);
	}

	@Override
	public List getByClass(Class cla) {
		// TODO Auto-generated method stub
		return baseDao.getByClass(cla);
	}

	@Override
	public List getByField(Class class_name, String field_name,
			Object field_value) {
		// TODO Auto-generated method stub
		return baseDao.getByField(class_name, field_name, field_value);
	}

	@Override
	public void delByField(Class class_name, String field_name,
			Object field_value) {
		baseDao.delByField(class_name, field_name, field_value);
		
	}

	@Override
	public void setByField(Class class_name, String field_name,
			Object field_value, String condition_field_name,
			Object condition_field_value) {
		baseDao.setByField(class_name, field_name, field_value, condition_field_name, condition_field_value);
		
	}

	@Override
	public void setFiedValue(Class class_name, String field_name,
			Object field_value) {
		baseDao.setFiedValue(class_name, field_name, field_value);
		
	}

	@Override
	public PageInfo getByPage(String hql, PageInfo page) {
		// TODO Auto-generated method stub
		return baseDao.getByPage(hql, page);
	}
}
