package com.cxbook.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BookBase extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			// 接受处理乱码
			request.setCharacterEncoding("utf-8");
			// 告诉游览器解析方式
			response.setContentType("text/html;charset=utf-8");
			// 获取方法名
			String method = request.getParameter("method");

			// 获取字节码
			Class<? extends BookBase> clzz = this.getClass();

			Method method2 = clzz.getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
			method2.invoke(this, request, response);

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("未找到方法");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
