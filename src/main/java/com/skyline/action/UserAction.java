package com.skyline.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.StaleObjectStateException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import ch.qos.logback.classic.Logger;

import com.skyline.pojo.Onliner;
import com.skyline.pojo.User;
import com.skyline.service.BaseService;
import com.skyline.service.UserService;

@Controller
@RequestMapping("/user")
public class UserAction {
	@Autowired
	private UserService userService;
	@Autowired
	private BaseService baseService;
	
	private org.slf4j.Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Description("登录")
	@RequestMapping("/login.do")
	public void login(User user,HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{

		User user_db=null;
		try {
			user_db = userService.getByName(user.getUName());  //根据名字获取数据库中的对应的user
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			req.setAttribute("login_error", e.getMessage());
			req.getRequestDispatcher("/login.jsp").forward(req, res);
			return;
		}
		
		if(user_db!=null){
			if( user.getPassword().equals(user_db.getPassword()) ){
				
				ServletContext application = req.getSession().getServletContext();
				Map<String,Onliner> onlinerMap=(Map<String,Onliner>)application.getAttribute("onlinerMap");
				System.out.println("count"+onlinerMap);
				
				Onliner onliner =(Onliner) onlinerMap.get(req.getSession().getId());
				onliner.setIp(req.getRemoteAddr());
				onliner.setName(user_db.getUName());
				onliner.setBrowser(req.getHeader("User-Agent"));
				
				application.setAttribute("onlinerMap", onlinerMap);
				
				System.out.println("xxxxxxxxxx"+onliner.getBrowser());
				req.getSession().setAttribute("user", user_db); //将登录用户保存到session 中
				req.getRequestDispatcher("/index.jsp").forward(req, res);
			}else{
				req.setAttribute("login_error", "用户名或者密码不正确");
				req.getRequestDispatcher("/login.jsp").forward(req, res);
			}
		}else{
			req.setAttribute("login_error", "用户名 "+user.getUName()+" 不存在!");
			req.getRequestDispatcher("/login.jsp").forward(req, res);
		}
	}
	
	//注销登录
	 @RequestMapping("logout.do")
	public void logout(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{

		 req.getSession().invalidate();
		res.sendRedirect(req.getContextPath());
	}
	
	@RequestMapping("toUser.do")
	public void getUser(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
		}
		req.getRequestDispatcher("/WEB-INF/jsp/user/addUser.jsp").forward(req, res);;
	}
	

	@RequestMapping(value="addUser.do")
	public String addUser(User user,RedirectAttributes red)  {
		try{
			baseService.save(user);
			red.addFlashAttribute("Message","添加成功");
		}catch(Exception e){
			logger.error("", e);
			red.addFlashAttribute("Message","添加失败 "+e.getMessage());
		}
		return "redirect:toUser.do";
	}

	//---------------通过ajax 在提交前先检查是否可以添加----
	@RequestMapping("addCheck.do")
	public void addCheck(HttpServletRequest req,HttpServletResponse res) throws IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter out =res.getWriter();
		String json="";
		String uName=req.getParameter("uName");
		if("".equals(uName)||uName==null){
			out.print(json);
			return;
		}
		
		if(!baseService.isUnique(User.class, "UName", uName)){
			json= "{\"Message\":\""+uName+" 已存在\",\"isTrue\":\""+"false\"}";
		}else{
			json = "{\"Message\":\""+uName+" 可用\",\"isTrue\":\""+"true\"}";
		}
		//isTrue 是用来移除添加按钮的disabled 属性的.因为添加要防止 添加不符合条件的用户

		
		//System.out.println(json);*/
		out.print(json);
	}

	//---通过ajax 获取所有用户用户名---,用于列出需要修改的所有用户名-
	@RequestMapping("getAllUName.do")
	public void getAllUName(HttpServletRequest req,HttpServletResponse res) throws IOException{
		res.setContentType("text/html;charset=UTF-8");
		StringBuffer json_str= new StringBuffer("[");
		List<User> userList=baseService.getByClass(User.class);

		for(int i=0;i<userList.size();i++){
			json_str.append("\"");
			json_str.append(userList.get(i).getUName());
			json_str.append("\",");
		}
		
		if(userList.size()>0){
			json_str.setCharAt(json_str.length()-1, ']');
		}else{ //假如是空List 就返回空数组[];
			json_str.append("]");  
		}
	//	String str="["+"\""+cusListOfEmp.get(0).getVosId()+"\""+","+"\""+cusListOfEmp.get(0).getVosId()+"\""+"]";
	//	System.out.println(json_str);
		PrintWriter out =res.getWriter();
		out.print(json_str);
	}
	
	//---------------ajax ----将选择用户名和id 赋值给input ,用于默认显示
	@RequestMapping("getUserByName.do")
	public void getUserByName(HttpServletRequest req,HttpServletResponse res) throws IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter out =res.getWriter();
		String json="";
		String uName=req.getParameter("UName");
		//UName 被select 标签限制了,所以其对象一定存在,所有这里不做null 判断
		User user =(User)baseService.getByField(User.class, "UName", uName).get(0);

		json = "{\"UId\":\""+user.getUId()+"\",\"UName\":\""+user.getUName()+"\",\"version\":\""+user.getVersion()+"\"}";
//		System.out.print(json);
		out.print(json);
	}
	
	@RequestMapping("updateCheck.do")
	public String updateCheck(HttpServletRequest req,HttpServletResponse res,User user1,RedirectAttributes red){
		User db_user = (User)baseService.getById(User.class, user1.getUId());
		//是否重命名
		if(!user1.getUName().equals(db_user.getUName())){
			if(!baseService.isUnique(User.class, "UName", user1.getUName())){
				red.addFlashAttribute("Message", user1.getUName()+"已存在");
				return "redirect:toUser.do";
			}
		}
		//原来密码是否正确
		String old_pass=req.getParameter("old_pass");
		if(!old_pass.equals(db_user.getPassword())){
			red.addFlashAttribute("Message", "原密码不正确");
			return "redirect:toUser.do";
		}
		
		//确认密码是否和新密码一致
		String new_pass=req.getParameter("new_pass");
		if(!user1.getPassword().equals(new_pass)){
			red.addFlashAttribute("Message", "新密码不一致");
			return "redirect:toUser.do";
		}
		
		//更新用户
		try{
			baseService.update(user1);
			}
		catch(StaleObjectStateException e){
			red.addFlashAttribute("Message", "其他人已经更新了数据,请重新更新");
			logger.error("", e);
			return "redirect:toUser.do";
		}catch(Exception e){
			red.addFlashAttribute("Message", "更新失败");
			logger.error("", e);
			return "redirect:toUser.do";
		}

		red.addFlashAttribute("Message", "更新成功");
		return "redirect:toUser.do";

	}
	
}
