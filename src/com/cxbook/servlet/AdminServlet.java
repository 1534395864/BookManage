package com.cxbook.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.cxbook.bean.AdminInfo;
import com.cxbook.bean.AllAdmin;
import com.cxbook.bean.Book;
import com.cxbook.bean.CountMoney;
import com.cxbook.bean.GetAllBook;
import com.cxbook.bean.Root;
import com.cxbook.service.AdminService;
import com.cxbook.serviceImp.AdminServiceImp;
import com.cxbook.utils.Admin;
import com.cxbook.utils.GetTime;
import com.cxbook.utils.MD5;

/**
 * Servlet implementation class AdminServlet
 */
@SuppressWarnings("serial")
@WebServlet("/AdminServlet")
public class AdminServlet extends Admin {
	private static final AdminService admins = new AdminServiceImp();

	// 获取图书(分页)
	public void getAll(HttpServletRequest requset, HttpServletResponse response) {
		try {
			String page = requset.getParameter("page");
			GetAllBook books = new GetAllBook();
			books.setPage(page);
			books = admins.getAll(books);

			JSONObject json = new JSONObject();
			json.put("code", "0");
			json.put("count", books.getCount());
			json.put("msg", "成功");
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (Book b : books.getData()) {
				JSONObject data = new JSONObject();
				data.put("bid", b.getBid());
				data.put("name", b.getName());
				data.put("author", b.getAuthor());
				data.put("price", b.getPrice());
				data.put("img", b.getImage());
				data.put("content", b.getContent());
				data.put("status", b.getStatus());
				data.put("counts", b.getCounts());
				data.put("created", b.getCreated());
				list.add(data);
			}
			json.put("data", list);
			response.getWriter().write(json.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL异常");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("输出到网页异常");
		}
	}

	// 搜索图书
	public void getbook(HttpServletRequest requset, HttpServletResponse response) throws SQLException, IOException, ServletException {
		String bookname = requset.getParameter("bookname");
		if (bookname != "") {
			List<Book> books = admins.getbook(bookname);
			if (books.size() > 0) {
				JSONObject json = new JSONObject();
				json.put("code", "0");
				json.put("count", books.size());
				json.put("msg", "成功");
				List<JSONObject> list = new ArrayList<JSONObject>();
				for (Book b : books) {
					JSONObject data = new JSONObject();
					data.put("bid", b.getBid());
					data.put("name", b.getName());
					data.put("author", b.getAuthor());
					data.put("price", b.getPrice());
					data.put("img", b.getImage());
					data.put("content", b.getContent());
					data.put("status", b.getStatus());
					data.put("counts", b.getCounts());
					data.put("created", b.getCreated());
					list.add(data);
				}
				json.put("data", list);
				response.getWriter().write(json.toString());
			} else {
				JSONObject json = new JSONObject();
				json.put("code", "200");
				json.put("msg", "暂无数据");
				response.getWriter().write(json.toString());
			}
		}
	}

	// 上下架
	public void deleteBook(HttpServletRequest requset, HttpServletResponse response) throws SQLException, IOException {
		String status = requset.getParameter("status");
		String bid = requset.getParameter("bid");
		if (status.equals("1")) {
			admins.deleteBook(0, bid);
		} else if (status.equals("0")) {
			admins.deleteBook(1, bid);
		} else {
			JSONObject code = new JSONObject();
			code.put("code", "500");
			response.getWriter().write(code.toString());
			return;
		}
		JSONObject code = new JSONObject();
		code.put("code", "0");
		response.getWriter().write(code.toString());
	}

	// 退出
	public void exit(HttpServletRequest requset, HttpServletResponse response) throws IOException {
		requset.getSession().removeAttribute("admin");
		response.sendRedirect(this.getServletContext().getContextPath() + "/login.jsp");
	}

	// 编辑图书
	public void editBook(HttpServletRequest requset, HttpServletResponse response) throws SQLException, IOException {
		String bid = requset.getParameter("bid");
		String name = requset.getParameter("name");
		String author = requset.getParameter("author");
		String price = requset.getParameter("price");
		String content = requset.getParameter("content");
		Book book = new Book();
		book.setBid(bid);
		book.setName(name);
		book.setAuthor(author);
		book.setPrice(price);
		book.setContent(content);
		boolean a = admins.editbook(book);
		if (a) {
			// 成功
			JSONObject json = new JSONObject();
			json.put("code", "200");
			response.getWriter().write(json.toString());
		} else {
			// 失败
			JSONObject json = new JSONObject();
			json.put("code", "500");
			response.getWriter().write(json.toString());
		}
	}

	// 新添图书
	public void addBook(HttpServletRequest requset, HttpServletResponse response) throws IOException, ServletException, SQLException {
		String booktoken = (String) requset.getSession().getAttribute("addbook");
		String admintoken = requset.getParameter("btoken");
		if (booktoken == null || !booktoken.equals(admintoken)) {
			response.sendRedirect(this.getServletContext().getContextPath() + "/admin/addbook.jsp");
			return;
		}
		requset.getSession().removeAttribute("addbook");
		String name = requset.getParameter("bookname");
		String author = requset.getParameter("author");
		String price = requset.getParameter("price");
		String text = requset.getParameter("text");
		String[] classify = requset.getParameterValues("classify");
		String img = requset.getParameter("img");
		String[] base = img.split(",");
		Book book = new Book();
		book.setName(name);
		book.setAuthor(author);
		book.setPrice(price);
		book.setContent(text);
		book.setCreated(GetTime.get());
		boolean addbook = admins.addbook(book, classify, base[1]);
		if (addbook) {
			File file = new File("D://BOOK/txts/" + name + ".txt");
			file.createNewFile();
			requset.getSession().setAttribute("msg", "<script> window.onload = function(){" + "layer.msg(\'添加成功\');" + "}</script>");
			response.sendRedirect(this.getServletContext().getContextPath() + "/admin/addbook.jsp");

		} else {
			requset.getSession().setAttribute("msg", "<script> window.onload = function(){" + "layer.msg(\'系统错误,请重新添加\');" + "}</script>");
			response.sendRedirect(this.getServletContext().getContextPath() + "/admin/addbook.jsp");
		}
	}

	// 添加管理员
	public void addAdmin(HttpServletRequest requset, HttpServletResponse response) throws SQLException, IOException {
		String name = requset.getParameter("adminName");
		String password = requset.getParameter("password");
		String md5 = MD5.md5(password);
		boolean result = admins.addAdmin(name, password, md5);
		if (result) {
			JSONObject json = new JSONObject();
			json.put("result", "0");
			response.getWriter().write(json.toString());
		} else {
			JSONObject json = new JSONObject();
			json.put("result", "500");
			response.getWriter().write(json.toString());
		}
	}

	// 网站盈利日志
	public void allMoney(HttpServletRequest requset, HttpServletResponse response) throws IOException, SQLException {
		String date = requset.getParameter("date");
		String curr = requset.getParameter("curr");
		// 封装
		Root root = new Root();
		root.setDate(date);
		root.setCurr(curr);
		// 查询
		root = admins.queryMoney(root);
		// 页面数据
		List<CountMoney> data = root.getData();
		// 输出
		if (data.size() == 0) {
			JSONObject result = new JSONObject();
			result.put("count", "0");
			response.getWriter().write(result.toString());
		} else {
			JSONObject result = new JSONObject();
			result.put("count", root.getCount());
			ArrayList<JSONObject> list = new ArrayList<JSONObject>();
			for (CountMoney c : data) {
				JSONObject j = new JSONObject();
				j.put("username", c.getUsername());
				j.put("created", c.getCreated());
				j.put("msg", c.getXc().equals("1") ? "充值了余额" : "购买了图书>" + c.getName());
				j.put("money", c.getMoney());
				list.add(j);
			}
			result.put("countmoney", root.getCountmoney());
			result.put("data", list);
			response.getWriter().write(result.toString());
		}
	}

	// 查看管理员(分页)
	public void allAdmin(HttpServletRequest requset, HttpServletResponse response) throws SQLException, IOException {
		String page = requset.getParameter("page");
		AllAdmin aa = new AllAdmin();
		aa.setPage(page);
		aa = admins.getAllAdmin(aa);
		List<AdminInfo> data = aa.getData();
		// 返回数据
		if (data.size() > 0) {
			JSONObject json = new JSONObject();
			json.put("code", "0");
			json.put("msg", "请求成功");
			json.put("count", aa.getCount());
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (AdminInfo a : data) {
				JSONObject j = new JSONObject();
				j.put("uid", a.getUid());
				j.put("username", a.getUsername());
				j.put("password", a.getPassword());
				j.put("status", a.getStatus().equals("1") ? "正常" : "冻结");
				list.add(j);
			}
			json.put("data", list);
			response.getWriter().write(json.toString());
		} else {
			JSONObject json = new JSONObject();
			json.put("code", "200");
			json.put("msg", "暂无数据");
			response.getWriter().write(json.toString());
		}

	}

	// 编辑管理员
	public void editAdmin(HttpServletRequest requset, HttpServletResponse response) throws SQLException, IOException {
		String uid = requset.getParameter("uid");
		String username = requset.getParameter("username");
		String password = requset.getParameter("password");
		String status = requset.getParameter("status");
		String md5 = MD5.md5(password);

		boolean flag = admins.editadmin(uid, username, password, status, md5);

		if (flag) {
			// 成功
			JSONObject json = new JSONObject();
			json.put("code", "200");
			response.getWriter().write(json.toString());
		} else {
			// 失败
			JSONObject json = new JSONObject();
			json.put("code", "500");
			response.getWriter().write(json.toString());
		}

	}

	// 删除管理员
	public void deleteAdmin(HttpServletRequest requset, HttpServletResponse response) throws SQLException, IOException {
		String uid = requset.getParameter("uid");
		boolean flag = admins.deleteAdmin(uid);
		JSONObject code = new JSONObject();
		code.put("code", "0");
		response.getWriter().write(code.toString());
	}

	// 搜索管理员
	public void getAdmin(HttpServletRequest requset, HttpServletResponse response) throws IOException, SQLException {
		String name = requset.getParameter("name");
		if (name != " " || name != "") {
			List<AdminInfo> list = admins.getadmin(name);
				if(list.size() >0) {
					JSONObject json = new JSONObject();
					json.put("code", "0");
					json.put("count", list.size());
					List<JSONObject> arr = new ArrayList<JSONObject>();
					for(AdminInfo a :list) {
						JSONObject j = new JSONObject();
						j.put("uid", a.getUid());
						j.put("username", a.getUsername());
						j.put("password", a.getPassword());
						j.put("status", a.getStatus().equals("1") ? "正常" : "冻结");
						arr.add(j);
					}
					json.put("data", arr);
					response.getWriter().write(json.toString());
				}else {
					JSONObject json = new JSONObject();
					json.put("code", "200");
					json.put("msg", "暂无数据");
					response.getWriter().write(json.toString());
				}
		}
	}
}
