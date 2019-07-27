package com.cxbook.utils;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserBase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 接受处理乱码
			request.setCharacterEncoding("utf-8");
			// 告诉游览器解析方式
			response.setContentType("text/html;charset=utf-8");
			// 获取方法名
			String method = request.getParameter("method");
			// 获取字节码
			Class<? extends UserBase> clzz = this.getClass();

			Method method2 = clzz.getMethod(method, HttpServletRequest.class, HttpServletResponse.class);

			method2.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
