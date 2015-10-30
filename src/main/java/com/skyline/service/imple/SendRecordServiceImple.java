package com.skyline.service.imple;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.predisw.annotation.Description;
import com.predisw.annotation.Log;
import com.skyline.dao.BaseDao;
import com.skyline.dao.SendRecordDao;
import com.skyline.dao.imple.SendRecordDaoImple;
import com.skyline.pojo.Rate;
import com.skyline.pojo.SendRecord;
import com.skyline.service.SendRecordService;

@Service("sendRecordService")
public class SendRecordServiceImple  implements SendRecordService {
	@Autowired
	private SendRecordDao srDao;
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public SendRecord getLastExcelTp(String vosId) {
		// TODO Auto-generated method stub
		return srDao.getLastExcelTp(vosId);
	}

	@Override
	public List<SendRecord> getSRAfterSendTime(Date sendTime, int c_id) {
		// TODO Auto-generated method stub
		return srDao.getSRAfterSendTime(sendTime, c_id);
	}

	
	@Override
	@Log
	@Description(name="设置报价错误")
	public void setMailInCorrect(List<SendRecord> srList) {

		for(SendRecord sr:srList){
			List<Rate> rateList=baseDao.getByField(Rate.class, "name", sr.getRateName());
			for(Rate rate:rateList){
				rate.setIsCorrect(false);
				baseDao.update(rate);
				if(rate.getPRid()!=null){
					Rate old_rate=(Rate)baseDao.getById(Rate.class,rate.getPRid() );
					old_rate.setExpireTime(null);
					baseDao.update(old_rate);
				}

			}
			sr.setIsCorrect(false);
			baseDao.update(sr);
		}
	}
	
}
