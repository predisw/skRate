package com.skyline.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sys")
public class SysAction {
	@RequestMapping("getVerInfo.do")
	public void getVerInfo(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		req.getRequestDispatcher("/WEB-INF/jsp/sys/version.jsp").forward(req, res);
	}

}
