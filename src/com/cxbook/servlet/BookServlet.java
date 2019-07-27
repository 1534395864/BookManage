package com.cxbook.servlet;

import com.cxbook.bean.*;
import com.cxbook.service.BookService;
import com.cxbook.service.UserService;
import com.cxbook.serviceImp.BookServiceImp;
import com.cxbook.serviceImp.UserServiceImp;
import com.cxbook.utils.BookBase;
import com.cxbook.utils.GetTime;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/BookServlet")
public class BookServlet extends BookBase {
	// 服务层
	private final static BookService books = new BookServiceImp();

	// 下载排行榜
	public void getDownloadList(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

		List<DownloadList> downloadlist = books.getDownloadlist();
		JSONObject json = new JSONObject();
		json.put("code", "0");
		List<JSONObject> jsonlist = new ArrayList<JSONObject>();
		for (DownloadList d : downloadlist) {
			JSONObject j = new JSONObject();
			j.put("bid", d.getBid());
			j.put("username", d.getName());
			jsonlist.add(j);
		}
		json.put("data", jsonlist);
		response.getWriter().write(json.toString());
	}

	// 首页分页
	public void indexPage(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		// 获取当前页
		String currentpage = request.getParameter("currentpage");
		IndexPage indepage = books.indepage(currentpage);
		request.setAttribute("indexpage", indepage);
		// 获取分类
		List<Category> category = books.category();
		request.setAttribute("category", category);
		// 获取cookie
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		// 遍历Cookies
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
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					return;
				}
				request.getRequestDispatcher("/index.jsp").forward(request, response);
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	// 图书详情
	public void getbook(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String bid = request.getParameter("bid");
		Book bookinfo = books.getBookinfo(bid);
		request.setAttribute("book", bookinfo);
		request.getRequestDispatcher("/bookinfo.jsp").forward(request, response);
	}

	// 图书分类
	public void category(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		// 获取分类
		String cid = request.getParameter("cid");
		// 获取当前页
		String currentpage = request.getParameter("currentpage");
		IndexPage categoryinfo = books.categoryinfo(cid, currentpage);
		request.setAttribute("categoryinfo", categoryinfo);
		request.setAttribute("cid", cid);
		// 获取分类
		List<Category> category = books.category();
		request.setAttribute("category", category);
		request.getRequestDispatcher("/category.jsp").forward(request, response);
	}

	// 下载图书
	public void downloadBook(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		// 处理请求
		ServletOutputStream out = null;
		Book bookinfo = null;
		FileInputStream fs = null;
		try {
			// 验证Token
			String sessionbid = (String) request.getSession().getAttribute("bid");
			String bid = request.getParameter("bid");
			if (sessionbid == null || bid == null || !sessionbid.equals(bid)) {
				response.getWriter().write("非法访问下载资源！");
				System.out.println("有人非法访问");
				return;
			}
			bookinfo = books.getBookinfo(bid);
			// 读取要下载的文件
			File f = new File("C:/BOOK/txts/" + bookinfo.getName() + ".txt");
			// 读取要下载的文件
			if (f.exists()) {
				fs = new FileInputStream(f);
				String filename = URLEncoder.encode(f.getName(), "utf-8"); // 解决中文文件名下载后乱码的问题
				// 告诉下载而不是打开
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				// 告诉大小
				// response.setHeader("Content-Length", String.valueOf(f.length()));
				// 分块
				// response.setHeader("Transfer-Encoding", "chunked");
				// 获取响应报文输出流对象
				out = response.getOutputStream();
				// 输出
				byte[] b = new byte[1204 * 10];
				int length = 0;
				while ((length = fs.read(b)) != -1) {
					out.write(b, 0, length);
					out.flush();
				}
				request.getSession().removeAttribute("bid");
			}

		} catch (IOException e) {
			return;
		} finally {
			if (out == null || fs == null) {
				return;
			}
			User user = (User) request.getSession().getAttribute("user");
			if (bookinfo != null && user != null) {
				// 增加下载记录
				books.Downloadrecord(Integer.valueOf(user.getUid()), Integer.valueOf(bookinfo.getBid()), GetTime.get());
				// 增加图书下载次数
				books.addBookCount(Integer.valueOf(bookinfo.getBid()));
			}
			try {
				out.close();
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 新书排行榜
	public void newBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		List<DownloadList> downloadlist = books.getNewBook();
		JSONObject json = new JSONObject();
		json.put("code", "0");
		List<JSONObject> jsonlist = new ArrayList<JSONObject>();
		for (DownloadList d : downloadlist) {
			JSONObject j = new JSONObject();
			j.put("bid", d.getBid());
			j.put("username", d.getName());
			jsonlist.add(j);
		}
		json.put("data", jsonlist);
		response.getWriter().write(json.toString());
	}
}
