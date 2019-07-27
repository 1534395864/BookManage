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
			// ���ܴ�������
			request.setCharacterEncoding("utf-8");
			// ����������������ʽ
			response.setContentType("text/html;charset=utf-8");
			// ��ȡ������
			String method = request.getParameter("method");

			// ��ȡ�ֽ���
			Class<? extends BookBase> clzz = this.getClass();

			Method method2 = clzz.getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
			method2.invoke(this, request, response);

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("δ�ҵ�����");
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
