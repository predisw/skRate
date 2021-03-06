package com.skyline.action;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.dev.ReSave;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.predisw.exception.UniException;
import com.predisw.util.DateFormatUtil;
import com.skyline.comparatorImple.CcComparator;
import com.skyline.pojo.CountryCode;
import com.skyline.service.BaseService;
import com.skyline.service.CountryCodeService;
import com.skyline.util.HttpUpAndDownload;
import com.skyline.util.PageInfo;
import com.skyline.util.PoiExcel;
import com.skyline.util.SingletonProps;


@Controller
@RequestMapping("/cc")
public class CountryCodeAction {   //如果要使用spring 的自动注入，那么就不能extends HttpServlet,因为HttpServlet 是tomcat 直接负责的。
	Logger logger =LoggerFactory.getLogger(CountryCodeAction.class);
	@Autowired
	private CountryCodeService ccService;
	@Autowired
	private PoiExcel poiExcel;   //这个不是接口而是实现类
	@Autowired
	private BaseService baseService;
	
	
	//excel 批量导入国家
	@RequestMapping(value="importCountry.do")
	public String  importCountry(HttpServletRequest req,HttpServletResponse res,RedirectAttributes red){  
		//HttpServlet 和HttpServletRequest  不是必须绑定在 一起使用的
		long begin_time=System.currentTimeMillis();
		
		String fileName=null;;
		try {
			fileName = HttpUpAndDownload.getUploadInput(req).get("upload");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("上传失败", e);
			red.addFlashAttribute("Message", "上传失败");
			return "redirect:getCountrys.do";
		}
		int sheetNumber=0;
		String[] tbIndex={"Destination","Name","Destination Code","code"};
		
		List<String[]> cclist=null;
		try{
			cclist = poiExcel.readByPoi(fileName,sheetNumber, tbIndex);
		}catch(Exception e){
			logger.error("读上传文件失败", e);
			red.addFlashAttribute("Message", "读上传文件失败 "+e.getMessage()+" cause:"+e.getCause());
			return "redirect:getCountrys.do";
		}
		
		
		try {
			ccService.saveISRListToCcTable(cclist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("导入失败", e);
			red.addFlashAttribute("Message", "导入失败 "+e.getMessage()+" cause:"+e.getCause());
		}
		red.addFlashAttribute("Message", "用时: "+(System.currentTimeMillis()-begin_time)/1000+" 秒");
		return "redirect:getCountrys.do";
	}
	
	
	//显示country_code 数据库的内容
	@RequestMapping(value="/getCountrys.do")
	public void getCountrys(HttpServletRequest req,HttpServletResponse res,PageInfo page) throws ServletException, IOException{
		String country=req.getParameter("scountry");
		String code=req.getParameter("scode");
		String curPage="";
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
			country=(String)map.get("scountry"); 
			code=(String)map.get("scode"); 
			curPage=(String)map.get("curPage");
			if(curPage!=null && !"".equals(curPage)){
				page.setCurPage(Integer.parseInt(curPage));
			}
//			logger.debug("from getCountrys.do,the curPage is [{}]",page.getCurPage());
		}

		
		if(page==null){
			page=new PageInfo(); //新实例化的一个page 带有默认值
			logger.debug("new a page from getCountrys.do");
		}
		

		if(country==null){
			country="";
		}
		if(code==null){
			code="";
		}

		
		String[] conditions={country.trim().toLowerCase(),code.trim().toLowerCase()};
		
		page=ccService.getCountrys(page, conditions);
		
		List<CountryCode> ccList=page.getData();

		req.setAttribute("ccList", ccList);
		req.setAttribute("page", page);
		req.setAttribute("scountry", country);
		req.setAttribute("scode", code);
		
	
		
		req.getRequestDispatcher("/WEB-INF/jsp/country/addCountrys.jsp").forward(req, res);  //把参数属性传给jsp 页面
		
	}
	
	
	//删除或批量删除
	@RequestMapping(value="/delCountryCode.do")
	public String delById(HttpServletRequest req,RedirectAttributes red) throws ServletException, IOException{
		//搜索条件
		String country=req.getParameter("scountry");
		String code=req.getParameter("scode");
		String curPage=req.getParameter("curP");
		
		red.addFlashAttribute("scountry",country);
		red.addFlashAttribute("scode",code);
		red.addFlashAttribute("curPage",curPage);
		
		String ids=req.getParameter("ids"); //需要删除的对象的id
		String[] idArr=ids.split(",");
		try{
			ccService.delBulkById(idArr);
		}catch(Exception e){
			logger.error("删除失败", e);
			red.addFlashAttribute("Message","删除失败 "+e.getMessage()+" cause:"+e.getCause());
			return "redirect:getCountrys.do";
		}

		red.addFlashAttribute("Message","删除成功");
		return "redirect:getCountrys.do";
	}
	
	
	
	//添加单个 country_code 记录，如果是修改，不能用hibernate 自带的save，save 是新添加多一个，而不是在原来的基础上修改
	@RequestMapping(value="/saveCountryCode.do")
	public String save(CountryCode cc ,RedirectAttributes red) throws IOException{
			cc.setCountry(cc.getCountry().trim());
			cc.setCode(cc.getCode().trim());
		try{
			baseService.addUniCheck(cc,"code", cc.getCode()); 		//检查是否唯一
			baseService.save(cc);
		}catch(Exception e){
			logger.error("新增失败", e);
			red.addFlashAttribute("Message", "新增失败 "+e.getMessage()+" cause: "+e.getCause());
			return "redirect:getCountrys.do";
		}
		red.addFlashAttribute("Message", "新增成功");
		return "redirect:getCountrys.do";
	}
	
	
	//修改country_code 的记录
	@RequestMapping("/updateCountryCode.do")
	public String  update(CountryCode cc,HttpServletRequest req,RedirectAttributes red){
		//搜索条件:
		String country=req.getParameter("scountry");
		String code=req.getParameter("scode");
		String curPage=req.getParameter("curP");
		
		red.addFlashAttribute("scountry",country);
		red.addFlashAttribute("scode",code);
		red.addFlashAttribute("curPage",curPage);
		
		cc.setCountry(cc.getCountry().trim());
		cc.setCode(cc.getCode().trim());
		try{
			baseService.updateUniCheck(cc, "code", cc.getCode(),"ccId");   //检查code是否唯一
			baseService.update(cc);

		}catch(Exception e){
			logger.error("更新失败 ",e);
			red.addFlashAttribute("Message", "更新失败 "+e.getMessage()+" cause: "+e.getCause());
			return "redirect:getCountrys.do";
		}

		red.addFlashAttribute("Message", "更新成功");
		return "redirect:getCountrys.do";
		
	}
	
	@RequestMapping("exportAllCC.do")
	public void exportAllCC(HttpServletRequest req,HttpServletResponse res) throws IOException{
		List<CountryCode> ccList=baseService.getByClass(CountryCode.class);
		
		if(ccList.size()==0){
			res.setContentType("text/html;charset=UTF-8");
			res.getWriter().write("<script>alert('不存在');history.back();</script>");
			return;
			
		}
		Properties props = SingletonProps.getInstance().getProperties(); 
		String[] db_header =props.getProperty("cc_db_header").split(",");
		String[] excel_header =props.getProperty("cc_excel_header").split(",");
		SimpleDateFormat date_fomat=DateFormatUtil.getSdf("yyyy-MM-dd");
		String filePath=req.getServletContext().getRealPath(props.getProperty("excel_export")); //生产环境下的excel 导出绝对路径
		String fileName="AllCodes_"+date_fomat.format(new Date())+".xls";  //excel2003
		String fullPathName=filePath+File.separator+fileName;
		
		poiExcel.export(ccList, fullPathName, db_header, excel_header, date_fomat);
		HttpUpAndDownload.downLoadFile(fullPathName, res);
		
		
	}

	
	
	
	
	
	
	
	
	
}
