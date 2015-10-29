package com.skyline.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.skyline.service.BaseService;
import com.skyline.util.PageInfo;
import com.skyline.pojo.Email;
import com.skyline.pojo.SendRecord;
@Controller
@RequestMapping("/sendRecord")
public class SendRecordAction {
	@Autowired
	private BaseService baseService;
	@RequestMapping("getSendRecords.do")
	
	public void getSendRecords(PageInfo page,HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		if(page==null){
			page=new PageInfo(); //新实例化的一个page 带有默认值
		}
		page.setPageSize(15); //设置每页15条,必须放到 getByPage 之前,否则页面只会显示8条.因为传递page,还是8
		page=baseService.getByPage("from SendRecord sr order by sr.sendTime desc", page);
//获取邮件内容
		List<SendRecord> list=page.getData();
		List<Email> emailList =new ArrayList<>();
		for(SendRecord sr:list){
			Email email=(Email) baseService.getByField(Email.class, "name", sr.getEmailName()).get(0);
			emailList.add(email);
		}
		
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
		}
		req.setAttribute("emailList", emailList);
		req.setAttribute("page", page);
		req.getRequestDispatcher("/WEB-INF/jsp/rate/send_record.jsp").forward(req, res);
	}
}
