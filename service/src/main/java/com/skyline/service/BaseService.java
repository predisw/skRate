package com.skyline.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Collection;

import com.predisw.exception.UniException;
import com.skyline.dao.BaseDao;




//公有业务接口
public interface BaseService extends BaseDao {

	//保存对象,如果对象已经存在,则替换.//通过程序来实现replace into
	//的功能.因为 通用的方法中,sql语句中不能确定映射对象的真实字段.无法写入
	//obj 对象的 field_name 属性的值存在于表中,则替换
	public void saveOrReplaceIfDup(Object obj,String field_name) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	//检查新添加的某个对象是否唯一,通过某个字段来判断
	public boolean isUnique(Class class_name,String field_name,Object field_value);
	
	//检查更新的对象某项是否唯一
	public Object updateUniCheck(Object obj,String field_name,Object field_value,String id_field_name) throws UniException, NoSuchMethodException, SecurityException,IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	//添加的对象某项是否唯一
	public Object addUniCheck(Object obj,String field_name,Object field_value) throws UniException;

	//? 能update 吗?
	public void  updateBulk(Collection<? extends Object> collection);
	
	
	//? 能save 吗?
	public void  saveBulk(Collection<? extends Object> collection);
}
