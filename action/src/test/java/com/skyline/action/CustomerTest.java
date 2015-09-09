package com.skyline.action;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.skyline.pojo.Customer;
import com.skyline.pojo.Partner;
import com.skyline.service.BaseService;


@Transactional
@TransactionConfiguration(defaultRollback=false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class CustomerTest {
@Autowired
private BaseService baseService;
	@Test
	public void test() throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Customer cus=new Customer();
		Object obj =(Object) cus;
		Class cla=obj.getClass();
		String[] tbIndex={"vosId","CType"};
		for(int i=0;i<tbIndex.length;i++){
			Field field=cla.getDeclaredField(tbIndex[i]);
			Class paramType=field.getType();
			String methodStr="set"+ tbIndex[i].substring(0, 1).toUpperCase()+ tbIndex[i].substring(1);
			Method method=cla.getMethod(methodStr,paramType);
			if(paramType.isEnum() ){
				method.invoke(obj,Enum.valueOf(paramType,"CUSTOMER") );
				continue;
			}
		
			//其他类型直接设置
			method.invoke(obj,"001888");
		}

		

		
		Customer ccus=(Customer)obj;
		baseService.save(ccus);
		
	}

}
