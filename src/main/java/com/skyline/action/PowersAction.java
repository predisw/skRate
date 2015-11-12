package com.skyline.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.skyline.pojo.Powers;
import com.skyline.pojo.Role;
import com.skyline.pojo.User;
import com.skyline.service.BaseService;
import com.skyline.service.PowersService;

@Controller
@RequestMapping("/powers")
public class PowersAction {
	Logger logger =LoggerFactory.getLogger(PowersAction.class);
	
	@Autowired
	private BaseService baseService;
	@Autowired
	private PowersService powersService;
	
	
	@RequestMapping("toPowers.do")
	public String  toPowers(HttpServletRequest req,HttpServletResponse res){
		return "forward:/WEB-INF/jsp/powers/powers.jsp";
	}
	
	@RequestMapping("getPowers.do")
	public String getPowers(HttpServletRequest req,HttpServletResponse res){
		String role_id =req.getParameter("role_id");
		
		Role role = (Role) baseService.getById(Role.class, Integer.valueOf(role_id));
		
		req.setAttribute("role", role);
		
		List<Powers> powers = baseService.getByClass(Powers.class);
		req.setAttribute("powers", powers);

		
		return "forward:toPowers.do";
	}
	
	
	
	
	
	@RequestMapping("toRoles.do")
	public String toRoles(HttpServletRequest req,HttpServletResponse res){
		
		return "forward:/WEB-INF/jsp/powers/role.jsp";
	}
	
	@RequestMapping("getRoles.do")
	public String  getRoles(HttpServletRequest req,HttpServletResponse res){
		
		List<Role> roles = baseService.getByClass(Role.class);
		req.setAttribute("roles", roles);
		return "forward:toRoles.do";
	}
	
	
	@RequestMapping("addRole.do")
	public void addRole(HttpServletRequest req,HttpServletResponse res) throws IOException {
		
		String name = req.getParameter("role_name");
		System.out.println(name);
		
		Role role = new Role(name);
		String Message="success";
		try{
			baseService.save(role);
		}catch(Exception e){
			logger.error("",e);
			Message=e.getMessage();
		}

//		req.setAttribute("Message", Message);
		
		PrintWriter out = res.getWriter();
		out.write(Message);
	}
	
	@RequestMapping("delRole")
	public void delRole(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String id =req.getParameter("role_id");
		
		String Message="success";
		try{
			baseService.delete(baseService.getById(Role.class, Integer.valueOf(id)));
		}catch(Exception e){
			logger.error("",e);
			Message=e.getMessage();
		}
		
		PrintWriter out = res.getWriter();
		out.write(Message);
		
	}
	
	@RequestMapping("savePowers.do")
	public String savePowers(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String roleId = req.getParameter("role_id");
		String[] powerIds=req.getParameterValues("power_id");
		
		Role role =(Role) baseService.getById(Role.class, Integer.valueOf(roleId));
		Set<Powers> powers = new HashSet<>();
		
		String Message="success";
		try{
			
			for(String powerId:powerIds){
				Powers power= (Powers) baseService.getById(Powers.class, Integer.valueOf(powerId));
				powers.add(power);
			}	
			
			role.setPowerses(powers);
			baseService.update(role);
		}catch(Exception e){
			logger.error("",e);
			Message=e.getMessage();
		}
		
		req.setAttribute("Message", Message);
		return "forward:getPowers.do";
		
	}
	
	@RequestMapping("toUserRole.do")
	public String toUserRole(HttpServletRequest req,HttpServletResponse res){
		
		Map<String,?> map=RequestContextUtils.getInputFlashMap(req);
		if(map!=null){
			req.setAttribute("Message",map.get("Message"));
		}
		
		List<User> users=baseService.getByClass(User.class);
		List<Role> roles = baseService.getByClass(Role.class);
		
		req.setAttribute("roles", roles);
		req.setAttribute("users", users);
		
		return "forward:/WEB-INF/jsp/powers/userRole.jsp";
	}

	@RequestMapping("getRolesByUser.do")
	public void getRolesByUser(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String id = req.getParameter("id");
		User user =(User) baseService.getById(User.class, Integer.valueOf(id));
		Set<Role> roles =user.getRoles();
		JSONArray  roleNames= new JSONArray();

		for(Role role:roles){
			JSONObject jsonRole=new JSONObject();
			jsonRole.put("value",role.getId());
			jsonRole.put("name", role.getName());

			roleNames.put(jsonRole);
		}
		
		res.setCharacterEncoding("UTF-8");
		PrintWriter out =res.getWriter();
		out.print(roleNames.toString());

	}

	@RequestMapping("saveOrUpdateRolesToUser.do")
	public void saveOrUpdateRolesToUser(HttpServletRequest req,HttpServletResponse res) throws IOException{
		String idsStr=req.getParameter("ids");
		String[] ids=idsStr.split(",");
		if("".equals(idsStr)){
			ids=new String[0];
		}

		Set<Role> roles=new HashSet<>();
		String Message="Success";
		
		res.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		
		try{
			for(String id:ids){
				roles.add((Role) baseService.getById(Role.class, Integer.valueOf(id)));
			}
		}catch(Exception e){
			logger.error("获取Role失败",e);
			Message="获取role失败 "+e.getMessage()+" Cause: "+e.getCause();
			out.print(Message);
		}

		String uId=req.getParameter("uId");
		User user =(User) baseService.getById(User.class, Integer.valueOf(uId));
		
		try{
			user.setRoles(roles);
			baseService.saveOrUpdate(user);
		}catch(Exception e){
			logger.error("更新role失败",e);
			Message="更新role失败 "+e.getMessage()+" Cause: "+e.getCause();
			out.print(Message);
		}
		
		out.print(Message);
	}
	
	
	@RequestMapping("getChildPowersId.do")
	public void getChildPowersId(HttpServletRequest req,HttpServletResponse res) throws IOException{
		
		String idstr = req.getParameter("id");
		List<Powers> childPowers= new ArrayList<>();
		String Message = "success";
		try{
			childPowers=powersService.getChildPowers(Integer.valueOf(idstr));
		}catch(Exception e){
			logger.error("",e);
			Message=e.getMessage();
		}
		
		if(childPowers.size()==0){
			return;
		}
		
		JSONObject data =new JSONObject();
		JSONArray ids= new JSONArray();
		
		for(Powers power:childPowers){
			ids.put(power.getId());
		}
		
		data.put("Message", Message);
		data.put("ids",ids);
		
		res.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		System.out.println(data.toString());
		
		out.print(data.toString());
		
	}
	
	

}
