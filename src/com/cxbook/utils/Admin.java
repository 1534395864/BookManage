package com.cxbook.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.cxbook.bean.User;
import com.cxbook.dao.AdminDao;
import com.cxbook.daoImp.AdminDaoImp;

@SuppressWarnings("serial")
public class Admin extends HttpServlet {
	private static final AdminDao admindao = new AdminDaoImp();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 接受处理乱码
			request.setCharacterEncoding("utf-8");
			// 告诉游览器解析方式
			response.setContentType("text/html;charset=utf-8");
			User admin = (User) request.getSession().getAttribute("admin");
			if(admin == null) {
				response.sendRedirect(this.getServletContext().getContextPath()+"/login.jsp");
			}
			// 获取方法名
			String method = request.getParameter("method");
			List<String> url = admindao.getUrl(admin.getRid());
			if (url.contains(method)) {
				// 获取字节码
				Class<? extends Admin> clzz = this.getClass();
				Method method2 = clzz.getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
				method2.invoke(this, request, response);
			}else {
				JSONObject json = new JSONObject();
				json.put("code", "NOTF");
				json.put("msg", "没有权限");
				response.getWriter().write(json.toString());	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
