package com.skyline.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.predisw.util.DateFormatUtil;
import com.predisw.util.NumberUtils;
import com.skyline.pojo.BaseRate;
import com.skyline.pojo.CountryCode;
import com.skyline.pojo.Customer;
import com.skyline.service.BaseRateService;
import com.skyline.service.BaseService;
import com.skyline.service.CountryCodeService;
import com.skyline.service.RateService;
import com.skyline.util.PageInfo;
import com.skyline.util.PoiExcel;

/**
 * 被CRAteAction 和rateAction 共用的Action
 * @author predisw
 *
 */
@Controller
class BaseRateAction {
	@Autowired
	private CountryCodeService ccService;
	@Autowired
	private BaseRateService baseRateService;


	
	
	@RequestMapping("getBaseRatePageInfo.do")
	public HttpServletRequest  getBaseRatePageInfo(HttpServletRequest req,HttpServletResponse res){
		

		List<CountryCode> ccList=ccService.getCountrys();
		//不把page 放到 getRateRecord(HttpServletRequest req,HttpServletResponse res) 中,是因为这样自动 new 一个PageInfo page 了.无法设置默认pageSize
		PageInfo page=(PageInfo)req.getAttribute("page");
		if(page==null){
			page=new PageInfo(); //新实例化的一个page 带有默认值
			page.setPageSize(3);//默认每页显示3个国家
		}
		req.setAttribute("page", page);

		req.setAttribute("ccList", ccList);
		
		return req;
	}

	
	public HttpServletRequest getSearchResult(PageInfo page,HttpServletRequest req,HttpServletResponse res,Class ratecCazz){
		
		SimpleDateFormat sdf=DateFormatUtil.getSdf("yyyy-MM-dd");
		String sDateStr=req.getParameter("sDate");
		String tDateStr=req.getParameter("tDate");
		String vosId=req.getParameter("vosId");
		String country=req.getParameter("country");

		Date sDate=sdf.parse(sDateStr, new ParsePosition(0)); //如果字符串不能被格式话则返回null.

		Date tDate=sdf.parse(tDateStr, new ParsePosition(0));//如果字符串不能被格式话则返回null.
		if(tDate==null) tDate=new Date();
		
		boolean is_success=true;
		boolean is_correct=true;
		
		if(page==null){
			page=new PageInfo(); //新实例化的一个page 带有默认值
		}
		
		int  firstResult=(page.getCurPage()-1)*page.getPageSize();
		int maxResult=page.getPageSize();
		
		List<String> countryList=new ArrayList<>();
		if(!"all".equalsIgnoreCase(country)&&country!=null && !"".equals(country)){
			countryList.add(country);
			page = baseRateService.getRateByPage(sDate, tDate, vosId, country, is_success, page,is_correct,ratecCazz); //page.data 就是rList
		}else{
			countryList=baseRateService.getCountry(sDate, tDate, vosId, is_success, firstResult, maxResult,is_correct,ratecCazz);
			page = baseRateService.getRateByPage(sDate, tDate, vosId, country, is_success, page,is_correct,ratecCazz); //page.data 就是rList
		}

		//用于在html 上显示 提交的值
		req.setAttribute("sDate", sDateStr);
		req.setAttribute("tDate", tDateStr); 
		req.setAttribute("vosId", vosId); 
		req.setAttribute("country", country); 
		req.setAttribute("page",page );
		req.setAttribute("cList", countryList);
		
		return req;
	}
	

	
	
}
