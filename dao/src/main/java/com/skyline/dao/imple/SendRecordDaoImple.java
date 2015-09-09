package com.skyline.dao.imple;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skyline.dao.SendRecordDao;
import com.skyline.pojo.SendRecord;
@Repository("sendRecordDao")
public class SendRecordDaoImple implements SendRecordDao {
	@Autowired
	private SessionFactory sf;
	


	@Override
	public SendRecord getLastExcelTp(String vosId) {
		// TODO Auto-generated method stub
		Session ss =sf.getCurrentSession();
		String hql="from SendRecord sr where sr.vosId=:vosId Order by sr.sendTime desc";
		Query query =ss.createQuery(hql);
		query.setParameter("vosId", vosId);
		
		query.setMaxResults(1);// 
		
		SendRecord sr=(SendRecord)query.uniqueResult();
		return sr;
	}

	@Override
	public List<SendRecord> getSRAfterSendTime(Date sendTime, int c_id) {
		// TODO Auto-generated method stub
		Session ss =sf.getCurrentSession();
		String hql="from SendRecord sr where sr.sendTime>:sendTime and sr.CId=:c_id";
		Query query=ss.createQuery(hql);
		query.setParameter("sendTime", sendTime);
		query.setParameter("c_id", c_id);
		return query.list();
	}

}
