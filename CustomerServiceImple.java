package com.skyline.service.imple;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.aop.Description;
import com.skyline.dao.BaseDao;
import com.skyline.dao.CustomerDao;
import com.skyline.dao.RateDao;
import com.skyline.pojo.Customer;
import com.skyline.pojo.Rate;
import com.skyline.predisw.exception.UniException;
import com.skyline.service.BaseService;
import com.skyline.service.CustomerService;

@Service
public class CustomerServiceImple implements CustomerService{
	@Autowired
	private CustomerDao cusDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private BaseService baseService;
	@Autowired
	private RateDao rateDao;
	@Autowired
	private SessionFactory sf;
	
	@Override
	public List<Customer> getCusByEnum(String eNum) {
		// TODO Auto-generated method stub
		return cusDao.getCusByEnum(eNum);
	}

	@Override
	public Customer getCusById(int id) {
		// TODO Auto-generated method stub
		return cusDao.getCusById(id);
	}

	@Override
	public Customer  updateCheck(Customer cus) throws UniException {
		// TODO Auto-generated method stub
		//------vosId 是否重复
				Customer db_cus=cusDao.getCusById(cus.getCId());
				if(!db_cus.getVosId().equals(cus.getVosId())){ //假如修改了vosId
					if(!baseService.isUnique(Customer.class, "vosId", cus.getVosId())){ 	//检查新的vosId 是否唯一
						UniException e = new UniException(cus.getVosId()+" 已存在");
						throw e;
					}
					//修改了vosId 和工号eNum,也要对象的修改关联的数据.rate 表和customer 表的vosId 数值需要跟着更改.eNum 只有rate 有关联.
					rateDao.upAndBakVos(db_cus.getVosId(), cus.getVosId());
					

				}
				
				//-------工号是否被修改
				if(!db_cus.getENum().equals(cus.getENum())){ //假如修改了工号,也要把rate 中的对应的修改
					baseDao.setByField(Rate.class, "ENum", cus.getENum(), "ENum", db_cus.getENum());
				}

				//非常重要,下面这句是将db_cus 从事务中异常.hibernate 的同一个事务中不能存在两个主键一样的不同对象.
				//db_cus 通过get()加入了回话事务中,cus 也将通过update 加入回话事务中;他们是主键一样的不同对象.
				//这将会报错.所有先要把db_cus从回话中移除
				sf.getCurrentSession().evict(db_cus);
				//使用merge() 也可以 ,如下代码
//				Customer cus2=(Customer)sf.getCurrentSession().merge(cus);
				
				cus.setEmail(cus.getEmail().replace(" ", "")); //去掉所有的空格
				cus.setCcEmail(cus.getCcEmail().replace(" ", "")); //去掉所有的空格
				cus.setBccEmail(cus.getBccEmail().replace(" ", "")); //去掉所有的空格
				
				return cus;

	}
	
	@Description(name="修改客户")
	@Override
	public void update(Customer cus) throws UniException {
		// TODO Auto-generated method stub
		Customer cus_ok = this.updateCheck(cus);
		baseDao.update(cus_ok);
	}

	@Override
	public Customer addCheck(Customer cus) throws UniException {
		// TODO Auto-generated method stub
		//---vosId 是否已经存在
		if(!baseService.isUnique(cus.getClass(), "vosId", cus.getVosId())){
			UniException e = new UniException(cus.getVosId()+" 已存在");
			throw e;
		}
		cus.setEmail(cus.getEmail().replace(" ", "")); //去掉所有的空格
		cus.setCcEmail(cus.getCcEmail().replace(" ", "")); //去掉所有的空格
		cus.setBccEmail(cus.getBccEmail().replace(" ", "")); //去掉所有的空格

		return cus;
	}

	@Override
	public void add(Customer cus) throws UniException {
		// TODO Auto-generated method stub
		Customer cus_ok=this.addCheck(cus);
		baseDao.save(cus_ok);
	}


	
}
