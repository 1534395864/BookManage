package com.cxbook.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cxbook.bean.Book;
import com.cxbook.bean.LayuiPage;
import com.cxbook.bean.User;
import com.cxbook.bean.UserConsume;
import com.cxbook.listener.SessionMap;
import com.cxbook.service.BookService;
import com.cxbook.service.UserService;
import com.cxbook.serviceImp.BookServiceImp;
import com.cxbook.serviceImp.UserServiceImp;
import com.cxbook.utils.Check;
import com.cxbook.utils.GetTime;
import com.cxbook.utils.PhoneCode;
import com.cxbook.utils.Qcloudsms;
import com.cxbook.utils.UpdateUser;
import com.cxbook.utils.UserBase;
import com.github.qcloudsms.SmsSingleSenderResult;


/**
 * Servlet implementation class UserServlet
 */
@SuppressWarnings("serial")
@WebServlet("/UserServlet")
public class UserServlet extends UserBase {
	// 服务层
	private final static UserService users = new UserServiceImp();
	//session集合
	SessionMap map = SessionMap.getinstance();

	// 用户注册
	public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		// 输出json
		PrintWriter writer = response.getWriter();
		// 检查时间
		int session = (int) request.getSession().getAttribute("unix");
		boolean check_unix = Check.check_unix(session);
		if (!check_unix) {
			JSONObject json = new JSONObject();
			json.put("code", "408");
			writer.write(json.toString());
			return;
		}
		// 检查token
		String usertoken = request.getParameter("token");
		String token = (String) request.getSession().getAttribute("token");
		boolean check_token = Check.check_token(token, usertoken);
		if (!check_token) {
			JSONObject json = new JSONObject();
			json.put("code", "400");
			writer.write(json.toString());
			return;
		}

		String username = request.getParameter("username");
		String sex = request.getParameter("sex");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setSex(Integer.parseInt(sex));
		user.setTelephone(phone);
		user.setEmail(email);
		// 是否新用户
		boolean isexist = users.isexist(user);
		if (isexist) {
			JSONObject json = new JSONObject();
			json.put("code", "1");
			writer.write(json.toString());
			return;
		}
		// 通过注册
		boolean register = users.register(user);
		if (register) {
			request.getSession().removeAttribute("token");
			JSONObject json = new JSONObject();
			json.put("code", "0");
			writer.write(json.toString());
		} else {
			JSONObject json = new JSONObject();
			json.put("code", "500");
			writer.write(json.toString());
		}
	}

	// 用户密码登录
	public void passwordlogin(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String autologin = request.getParameter("autologin");
		// 判断密码
		User user = users.passlogin(username, password);
		if (user != null) {
			int rid = user.getRid();
			if (rid != 1) {
				request.getSession().setAttribute("admin", user);
				JSONObject json = new JSONObject();
				json.put("result", "999");
				response.getWriter().write(json.toString());
			} else {
				// 是否自动登录
				if (autologin.equals("true")) {
					Cookie name = new Cookie("username", user.getUsername());
					Cookie pass = new Cookie("password", user.getMd5());
					name.setMaxAge(60 * 60 * 24 * 7);
					pass.setMaxAge(60 * 60 * 24 * 7);
					name.setDomain("zxg.cqchunxiu.com");
					pass.setDomain("zxg.cqchunxiu.com");
					response.addCookie(name);
					response.addCookie(pass);
				}
				request.getSession().setAttribute("user", user);
				JSONObject json = new JSONObject();
				json.put("result", "0");
				response.getWriter().write(json.toString());
			}
		} else {
			JSONObject json = new JSONObject();
			json.put("result", "500");
			response.getWriter().write(json.toString());
		}
	}

	// 退出登录
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("user");
		Cookie name = new Cookie("username", "");
		Cookie pass = new Cookie("password", "");
		name.setMaxAge(0);
		pass.setMaxAge(0);
		name.setDomain("zxg.cqchunxiu.com");
		pass.setDomain("zxg.cqchunxiu.com");
		response.addCookie(name);
		response.addCookie(pass);
		response.sendRedirect(this.getServletContext().getContextPath() + "/index.jsp");
	}

	// 购买VIP
	public void vip(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		User user = (User) request.getSession().getAttribute("user");
		PrintWriter writer = response.getWriter();
		if (user.getMoney() < 300.00) {
			JSONObject json = new JSONObject();
			json.put("result", "NOTENOUGH");
			writer.write(json.toString());
			writer.close();
		} else {
			// 扣钱
			boolean result = users.vip(user, 300.00);
			if (result) {
				users.setVip(user.getUsername());
				JSONObject json = new JSONObject();
				json.put("result", "YES");
				writer.write(json.toString());
				writer.close();
				// 更新信息
				UpdateUser.updating(request, user);
			}
			JSONObject json = new JSONObject();
			json.put("result", "ERROR");
			writer.write(json.toString());
			writer.close();
		}
	}

	// 充值
	public void recharge(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String money = request.getParameter("money");
		String paymentMethod = request.getParameter("paymentMethod");
		User user = (User) request.getSession().getAttribute("user");
		PrintWriter writer = response.getWriter();
		// 所有用户消费记录表
		users.user_consume(user, money, 1);
		// 给用户充值
		boolean flag = users.user_recharge(user, money);
		if (flag) {
			// 更新信息
			UpdateUser.updating(request, user);
			JSONObject json = new JSONObject();
			json.put("result", "0");
			writer.write(json.toString());
		}
	}

	// 消费
	public void consumption(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException, ServletException {
		// 获取用户
		User user = (User) request.getSession().getAttribute("user");
		// 获取图书ID
		String bid = request.getParameter("bid");
		// 是否登录
		if (user == null) {
			// 输出流
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("result", "NOTLOGIN");
			writer.write(json.toString());
			writer.close();
			return;
		}
		// 是否免费
		boolean flag = users.isFree(bid);
		int vip = Integer.parseInt(user.getVip());
		// 获取图书
		BookService books = new BookServiceImp();
		Book book = books.getBookinfo(bid);
		/**
		 * 免费图书
		 */
		if (flag) {
			// 是否是会员
			if (vip == 1) {
				// 钥匙
				request.getSession().setAttribute("bid", bid);
				// 输出下载
				PrintWriter writer = response.getWriter();
				JSONObject json = new JSONObject();
				json.put("result", "YES");
				writer.write(json.toString());
				writer.close();
			} else {
				// 获取次数
				int count = user.getCounts();
				// 次数不够
				if (count > 4) {
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "NOTCOUNT");
					writer.write(json.toString());
					writer.close();
				} else {
					// 次数够,减少一次免费下载机会
					users.reduceConut(user);
					// 更新信息
					UpdateUser.updating(request, user);
					// 钥匙
					request.getSession().setAttribute("bid", bid);
					// 输出下载
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "YES");
					writer.write(json.toString());
					writer.close();
				}
			}
		} else {
			/**
			 * 付费图书
			 */
			// 以前是否购买过
			boolean dr = users.downloadRecord(user.getUid(), book.getBid());
			if (dr) {
				// 钥匙
				request.getSession().setAttribute("bid", bid);
				// 输出下载
				PrintWriter writer = response.getWriter();
				JSONObject json = new JSONObject();
				json.put("result", "YES");
				writer.write(json.toString());
				writer.close();
			} else {
				// 获取价格,是否会员
				double price = Double.valueOf(book.getPrice());
				if (vip == 1) {
					DecimalFormat df = new DecimalFormat("#.00");
					String str = df.format(price * 0.8);
					price = Double.valueOf(str);
				}
				// 余额是否足够
				if (user.getMoney() >= price) {
					// 扣除余额,存消费记录
					users.deductBalance(user, price, book.getBid());
					// 更新信息
					UpdateUser.updating(request, user);
					// 钥匙
					request.getSession().setAttribute("bid", bid);
					// 输出下载
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "YES");
					writer.write(json.toString());
					writer.close();
				} else {
					// 提示没钱
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "NOTMONEY");
					writer.write(json.toString());
					writer.close();
				}
			}
		}
	}

	// 用户消费记录
	public void layuiPage(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String curr = request.getParameter("curr");
		LayuiPage layuipage = new LayuiPage();
		layuipage.setCurr(curr);
		User user = (User) request.getSession().getAttribute("user");
		layuipage.setUid(user.getUid());
		LayuiPage lay = users.layuipage(layuipage);
		List<UserConsume> data = lay.getList();
		JSONObject json = new JSONObject();
		json.put("count", lay.getCount());
		if (data.size() == 0) {
			response.getWriter().write(json.toString());
			return;
		}
		// 数组
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (UserConsume u : data) {
			JSONObject j = new JSONObject();
			j.put("time", u.getCreated());
			j.put("name", u.getName());
			j.put("price", u.getMoney());
			list.add(j);
		}
		json.put("data", list);
		response.getWriter().write(json.toString());
	}

	// 获取登录验证码
	public void getCode(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String phone = request.getParameter("phone");
		User user = users.isexistPhone(phone);
		// 不存在
		if (user == null) {
			JSONObject json = new JSONObject();
			json.put("code", "NULLU");
			response.getWriter().write(json.toString());
			return;
		}
		int newtime = GetTime.time();
		HttpSession session = request.getSession();
		Object time = session.getAttribute("time");
		if (time != null) {
			if ((newtime - (int) time) < 60) {
				JSONObject json = new JSONObject();
				json.put("code", "TIME");
				json.put("time", 60 - (newtime - (int) time));
				response.getWriter().write(json.toString());
				return;
			}
		}
		// 生成验证码
		int random = PhoneCode.getRandom();
		// 发送
		SmsSingleSenderResult result = Qcloudsms.sendsms(phone, random);
		if (result.result == 0) {
			session.removeAttribute("random");
			session.setAttribute("random", phone + "," + random + "," + GetTime.time());
			JSONObject json = new JSONObject().put("code", "0");
			response.getWriter().write(json.toString());
			session.setAttribute("time", newtime);
		} else {
			JSONObject json = new JSONObject().put("code", "发生了错误,请稍后再试~");
			response.getWriter().write(json.toString());
		}
	}

	// 验证码登录
	public void phoneLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String phone = request.getParameter("phone");
		String code = request.getParameter("code");
		HttpSession session = request.getSession();
		String arr = (String) session.getAttribute("random");
		// 是获取过验证码
		if (arr == null) {
			JSONObject json = new JSONObject();
			json.put("code", "ERROR");
			response.getWriter().write(json.toString());
			return;
		}
		// 验证码是否过期
		String[] params = arr.split(",");
		if (((GetTime.time() - Integer.parseInt(params[2]))) > 180) {
			JSONObject json = new JSONObject();
			json.put("code", "OVERTIME");
			response.getWriter().write(json.toString());
			return;
		}
		// 验证码是否正确
		if (phone.equals(params[0]) && code.equals(params[1])) {
			User user = users.isexistPhone(phone);
			session.setAttribute("user", user);
			JSONObject json = new JSONObject();
			json.put("code", "YSE");
			response.getWriter().write(json.toString());
		} else {
			JSONObject json = new JSONObject();
			json.put("code", "ERROR");
			response.getWriter().write(json.toString());
		}
	}

	public void QQ(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, URISyntaxException, ServletException {
		String openid = request.getParameter("openid");
		String access_token = request.getParameter("access_token");
		String state = request.getParameter("state");
		User user = users.openid(openid);
		if(user!=null) {
			request.getSession().setAttribute("user", user);
			//map.getSession(state).setAttribute("user", user);
			response.sendRedirect(this.getServletContext().getContextPath()+"/index.jsp");
		}else {
			//获取信息
			CloseableHttpClient httpcilent = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder("https://graph.qq.com/user/get_user_info");
			builder.addParameter("access_token", access_token);
			builder.addParameter("oauth_consumer_key", "101530672");
			builder.addParameter("openid", openid);
			 java.net.URI uri = builder.build();
			 HttpGet get = new HttpGet(uri);
			 //发送
			 CloseableHttpResponse execute = httpcilent.execute(get);
			 String result = EntityUtils.toString(execute.getEntity());
			JSONObject data = new JSONObject(result);
			if(data.get("ret").toString().equals("0")) {
				User qq = new User();
				qq.setUsername(filter(data.get("nickname").toString()));
				qq.setSex(data.get("gender").equals("男")?1:0);
				qq.setTelephone("QQ用户");
				qq.setEmail("QQ用户");
				qq.setImg(data.get("figureurl_1").toString());
				qq.setOpenid(openid);
				qq = users.QQ(qq);
				request.getSession().setAttribute("user", qq);
				//map.getSession(state).setAttribute("user", user);
				response.sendRedirect(this.getServletContext().getContextPath()+"/index.jsp");
			}
		}
	}
	//表情符过滤器类EmojiFilter
	public  String filter(String str) {
		
		if(str.trim().isEmpty()){
			return str;
		}
		String pattern="[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
		String reStr="";
		Pattern emoji=Pattern.compile(pattern);
		Matcher emojiMatcher=emoji.matcher(str);
		str=emojiMatcher.replaceAll(reStr);
		return str;
	}
}
