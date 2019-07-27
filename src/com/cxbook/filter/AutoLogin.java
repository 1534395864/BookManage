package com.cxbook.filter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxbook.bean.User;
import com.cxbook.service.UserService;
import com.cxbook.serviceImp.UserServiceImp;

/**
 * Servlet Filter implementation class AutoLogin
 */
@WebFilter(urlPatterns = "*.jsp")
public class AutoLogin implements Filter {

	/**
	 * 
	 * Default constructor.
	 */
	public AutoLogin() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// 获取是哪个页面
		String path = request.getRequestURI();
		if(path.indexOf("/admin") >-1) {
			User admin = (User) request.getSession().getAttribute("admin");
			if(admin ==null) {
				response.sendRedirect(request.getServletContext().getContextPath()+"/login.jsp");
				return;
			}else {
				chain.doFilter(request, response);
				return;
			}
		}
		// 拦截主页
		if (path.indexOf("/index.jsp") > -1 || path.length() == 12) {
			String currentpage = request.getParameter("currentpage");
			Cookie[] cookies = request.getCookies();
			if (currentpage == null || cookies == null) {
				request.getRequestDispatcher("/BookServlet?method=indexPage&currentpage=1").forward(request, response);
			}
			chain.doFilter(request, response);
			return;
		}
		// 拦截登录
		if (path.indexOf("/login.jsp") > -1) {
			chain.doFilter(request, response);
			return;
		}
		// 拦截注册
		if (path.indexOf("/register.jsp") > -1) {
			// 获取时间戳
			int unix = (int) (System.currentTimeMillis() / 1000);
			// 生成Token
			String tonken = UUID.randomUUID().toString().replaceAll("-", "");
			request.getSession().setAttribute("unix", unix);
			request.getSession().setAttribute("token", tonken);
			chain.doFilter(request, response);
			return;
		}
		// 自动登录
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			chain.doFilter(request, response);
			return;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			response.sendRedirect(request.getServletContext().getContextPath() + "/index.jsp");
			return;
		}
		String name = null;
		String password = null;
		for (Cookie c : cookies) {
			if (c.getName().equals("username")) {
				name = c.getValue();
			}
			if (c.getName().equals("password")) {
				password = c.getValue();
			}
		}
		if (name != null && password != null) {
			try {
				UserService users = new UserServiceImp();
				User use = users.passlogin(name, password);
				if (use != null) {
					request.getSession().setAttribute("user", use);
					chain.doFilter(request, response);
					return;
				}
				response.sendRedirect(request.getServletContext().getContextPath() + "/index.jsp");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			response.sendRedirect(request.getServletContext().getContextPath() + "/index.jsp");
			return;
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
