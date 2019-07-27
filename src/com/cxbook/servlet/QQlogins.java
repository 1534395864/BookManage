package com.cxbook.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QQlogins
 */
@WebServlet("/QQLogin")
public class QQlogins extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String url  ="https://graph.qq.com/oauth2.0/authorize?";
	private static String response_type = "code";
	private static String client_id = "101530672";
	private static String redirect_uri = "https://zxg.cqchunxiu.com/QQCheck";
	/**
     * @see HttpServlet#HttpServlet()
     */
    public QQlogins() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String state  = request.getSession().getId();
		response.sendRedirect(url+"response_type="+response_type+"&client_id="+client_id+"&redirect_uri="+redirect_uri+"&state="+state);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
