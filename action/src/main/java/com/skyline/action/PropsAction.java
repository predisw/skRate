package com.skyline.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.StaleObjectStateException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skyline.pojo.Props;
import com.skyline.service.BaseService;

@Controller
@RequestMapping("/props")
public class PropsAction {
	@Autowired
	private  BaseService baseService;
	
	@RequestMapping("toAddProp.do")
	public void toAddProp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		String mail_content="";
		List<Props> pList =baseService.getByField(Props.class, "name", "default_mail_content");
		if(pList!=null && pList.size()>0){
			mail_content=pList.get(0).getValue();
			mail_content=mail_content.replace("</p>", "</p>\\");
			mail_content=mail_content.replace("<br />", "</br />\\");
			mail_content=mail_content.replace("\n\r", "\\"); //注意在linux 服务器中不是"\n" 而是"\n\r"
			pList.get(0).setValue(mail_content);
			req.setAttribute("default_mail_content", pList.get(0));
		}
		//抄送地址
		List<Props> bccAddr =baseService.getByField(Props.class, "name", "bccAddr");
		if(bccAddr!=null && bccAddr.size()>0){
			req.setAttribute("bccAddr", bccAddr.get(0));
		}
		//列出属性
		
		req.getRequestDispatcher("/WEB-INF/jsp/props/addProp.jsp").forward(req, res);
	}
	
	@RequestMapping("addProp.do")
	public void addProp(Props prop,HttpServletRequest req,HttpServletResponse res) throws IOException{
		baseService.save(prop);
		res.sendRedirect(req.getContextPath()+"/props/toAddProp.do");
		
	}
	
	@RequestMapping("saveOrUpdate.do")
	public void saveOrUpdate(Props prop,HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException{
		try{
			baseService.saveOrUpdate(prop);
			}
		catch(StaleObjectStateException e){
			req.setAttribute("Message", "其他人已经更新了数据,请重新更新");
			e.printStackTrace();
			req.getRequestDispatcher("toAddProp.do").forward(req, res);
			return;
		}

		res.sendRedirect(req.getContextPath()+"/props/toAddProp.do");
	}
	
	//通过ajax 获取对应属性名的属性列表
	@RequestMapping("getProps.do")
	public void getProps(HttpServletRequest req,HttpServletResponse res) throws IOException{
		//获取可选的name 为 billing_unit属性的对象,对象类似一个map
		String props_name=req.getParameter("name");
		List<Props> pList=baseService.getByField(Props.class, "name", props_name);
		Map<String, String> map=new HashMap<String, String>();
		JSONArray jsonArray=new JSONArray();
		for(int i=0;i<pList.size();i++){
			JSONObject obj=new JSONObject();
			obj.put("id", pList.get(i).getId().toString());
			obj.put("name", pList.get(i).getValue());
			jsonArray.put(obj);

		}
//		System.out.println(jsonArray);
		res.setCharacterEncoding("UTF8");
		PrintWriter out = res.getWriter();
		out.print(jsonArray);

	}
	
	//通过ajax 删除一些属性
	@RequestMapping("delProps.do")
	public void delProps(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String id =req.getParameter("id");
		JSONObject obj=new JSONObject();

		try{
			baseService.delById(Props.class,Integer.parseInt(id));
		}catch(Exception e){
			e.printStackTrace();
			obj.put("Message", "删除失败"+e.getMessage()+" "+e.getCause());
			obj.put("isSuccess", false);
		}
		obj.put("Message", "删除成功");
		obj.put("isSuccess", true);
		res.setCharacterEncoding("UTF8");
		PrintWriter out = res.getWriter();
		out.print(obj);
		
	}
	
}
