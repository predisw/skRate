package com.skyline.action;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.predisw.exception.UniException;
import com.skyline.comparatorImple.EmpComparator;
import com.skyline.pojo.Employee;
import com.skyline.service.BaseService;
import com.skyline.service.EmployeeService;
import com.skyline.util.HttpUpAndDownload;
import com.skyline.util.PoiExcel;
import com.skyline.util.SingletonProps;

@Controller
@RequestMapping("/emp")
public class EmployeeAction  {
	private Logger logger =LoggerFactory.getLogger(EmployeeAction.class);
	@Autowired
	private EmployeeService empService;
	@Autowired
	private PoiExcel poiExcel;
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("toAddEmp.do")
	public void toaddEmp(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
		req.getRequestDispatcher("/WEB-INF/jsp/emp/addEmp.jsp").forward(req, res);
	}
	

	
	//使用了spring mvc 来传递post参数到pojo
	@RequestMapping(value="addEmp.do")
	public void save(Employee emp,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
		emp.setENum(emp.getENum().trim());
		emp.setEStatus(true); //默认设置新增员工为在职状态
		try{
			baseService.addUniCheck(emp, "ENum", emp.getENum());
			baseService.save(emp);
		}catch(UniException e){
			logger.error("", e);
			req.setAttribute("Message", "添加失败 "+e.getMessage());
			req.getRequestDispatcher("toAddEmp.do").forward(req, res);
		}
		catch(Exception e){
			logger.error("", e);
			req.setAttribute("Message", "添加失败");
			req.getRequestDispatcher("toAddEmp.do").forward(req, res);
		}
		req.setAttribute("Message", "添加成功");
		req.getRequestDispatcher("toAddEmp.do").forward(req, res);
	
	}
	
	@RequestMapping("importEmp.do")
	public void importEmp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException, NumberFormatException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException{
	
		//加载properties 文件的属性
		SingletonProps sProps=SingletonProps.getInstance();
		Properties props=sProps.getProperties();

		Map<String, String> uploadInput;
		String fileName="";
		try {
			uploadInput = HttpUpAndDownload.getUploadInput(req);
			fileName=uploadInput.get("upload");
		} catch (Exception e) {
			logger.error("", e);
			req.setAttribute("Message", "上传失败 "+e.getMessage()+" cause: "+e.getCause());
			req.getRequestDispatcher("toAddEmp.do").forward(req, res);
			return;
		}
		
		String[]  excel_sheetHead_order=props.getProperty("emp_import_excel").split(",");
		String[]  empAttributeOrder=props.getProperty("emp_dbtable_fields").split(",");
		
		try{
			empService.saveExcelEmpToDb(fileName, excel_sheetHead_order, empAttributeOrder);
		}catch(Exception e){
			logger.error("", e);
			req.setAttribute("Message", "上传失败 "+e.getMessage()+" cause: "+e.getCause());
			req.getRequestDispatcher("toAddEmp.do").forward(req, res);
			return;
		}
		
		req.setAttribute("Message", "导入成功");
		req.getRequestDispatcher("toAddEmp.do").forward(req, res);
	}
	
//	转跳到修改emp的页面
	@RequestMapping("toUpdateEmp.do")
	public void toModifyEmp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		List<Employee> empList=baseService.getByClass(Employee.class);
		Collections.sort(empList,new EmpComparator());
		
		req.setAttribute("empList", empList);
		req.getRequestDispatcher("/WEB-INF/jsp/emp/updateEmp.jsp").forward(req, res);
	}
	
	//---------获取员工
	@RequestMapping("getEmpInfo.do")
	public void getEmp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		String[] eids=req.getParameterValues("eids");
		//只取第一个eid		
		Employee emp=(Employee)baseService.getById(Employee.class, Integer.parseInt(eids[0]));
		req.setAttribute("emp", emp);
		req.getRequestDispatcher("toUpdateEmp.do").forward(req, res);
	}
	
	//-------------更新前的检查和处理----------
	@RequestMapping("updateCheck.do")
	public void updateCheck(HttpServletRequest req,HttpServletResponse res,Employee emp) throws ServletException, IOException{
		if(emp.getEId()==null){
			req.setAttribute("Message", "请先选择再修改");
			req.getRequestDispatcher("toUpdateEmp.do").forward(req, res);
			return;
		}
		Employee db_emp=(Employee)baseService.getById(Employee.class, emp.getEId());
		
		if(!db_emp.getENum().equals(emp.getENum())){
			if(!baseService.isUnique(Employee.class, "ENum", emp.getENum())){
				req.setAttribute("Message", emp.getENum()+"已存在");
				req.getRequestDispatcher("toUpdateEmp.do").forward(req, res);
				return;
			}
		}
		req.setAttribute("emp", emp);
		req.getRequestDispatcher("updateEmp.do").forward(req, res);
		
		
		
	}

	
	@RequestMapping("updateEmp.do")
	public void update(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		Employee emp =(Employee)req.getAttribute("emp");
		try{
			baseService.update(emp);
			}
		catch(StaleObjectStateException e){
			req.setAttribute("Message", "其他人已经更新了数据,请重新更新");
			logger.error("", e);
			req.getRequestDispatcher("toUpdateEmp.do").forward(req, res);
			return;
		}catch(Exception e){
			req.setAttribute("Message", "更新失败");
			logger.error("", e);
			req.getRequestDispatcher("toUpdateEmp.do").forward(req, res);
			return;
		}
		
		req.setAttribute("Message", "更新成功");
		req.getRequestDispatcher("toUpdateEmp.do").forward(req, res);
	}
	
	
}
