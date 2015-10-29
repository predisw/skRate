package com.skyline.service.imple;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyline.dao.SendRecordDao;
import com.skyline.dao.imple.SendRecordDaoImple;
import com.skyline.pojo.SendRecord;
import com.skyline.service.SendRecordService;

@Service("sendRecordService")
public class SendRecordServiceImple  implements SendRecordService {
	@Autowired
	private SendRecordDao srDao;
	
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

}
