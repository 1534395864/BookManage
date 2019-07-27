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
	// �����
	private final static UserService users = new UserServiceImp();
	//session����
	SessionMap map = SessionMap.getinstance();

	// �û�ע��
	public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		// ���json
		PrintWriter writer = response.getWriter();
		// ���ʱ��
		int session = (int) request.getSession().getAttribute("unix");
		boolean check_unix = Check.check_unix(session);
		if (!check_unix) {
			JSONObject json = new JSONObject();
			json.put("code", "408");
			writer.write(json.toString());
			return;
		}
		// ���token
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
		// �Ƿ����û�
		boolean isexist = users.isexist(user);
		if (isexist) {
			JSONObject json = new JSONObject();
			json.put("code", "1");
			writer.write(json.toString());
			return;
		}
		// ͨ��ע��
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

	// �û������¼
	public void passwordlogin(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String autologin = request.getParameter("autologin");
		// �ж�����
		User user = users.passlogin(username, password);
		if (user != null) {
			int rid = user.getRid();
			if (rid != 1) {
				request.getSession().setAttribute("admin", user);
				JSONObject json = new JSONObject();
				json.put("result", "999");
				response.getWriter().write(json.toString());
			} else {
				// �Ƿ��Զ���¼
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

	// �˳���¼
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

	// ����VIP
	public void vip(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		User user = (User) request.getSession().getAttribute("user");
		PrintWriter writer = response.getWriter();
		if (user.getMoney() < 300.00) {
			JSONObject json = new JSONObject();
			json.put("result", "NOTENOUGH");
			writer.write(json.toString());
			writer.close();
		} else {
			// ��Ǯ
			boolean result = users.vip(user, 300.00);
			if (result) {
				users.setVip(user.getUsername());
				JSONObject json = new JSONObject();
				json.put("result", "YES");
				writer.write(json.toString());
				writer.close();
				// ������Ϣ
				UpdateUser.updating(request, user);
			}
			JSONObject json = new JSONObject();
			json.put("result", "ERROR");
			writer.write(json.toString());
			writer.close();
		}
	}

	// ��ֵ
	public void recharge(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String money = request.getParameter("money");
		String paymentMethod = request.getParameter("paymentMethod");
		User user = (User) request.getSession().getAttribute("user");
		PrintWriter writer = response.getWriter();
		// �����û����Ѽ�¼��
		users.user_consume(user, money, 1);
		// ���û���ֵ
		boolean flag = users.user_recharge(user, money);
		if (flag) {
			// ������Ϣ
			UpdateUser.updating(request, user);
			JSONObject json = new JSONObject();
			json.put("result", "0");
			writer.write(json.toString());
		}
	}

	// ����
	public void consumption(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException, ServletException {
		// ��ȡ�û�
		User user = (User) request.getSession().getAttribute("user");
		// ��ȡͼ��ID
		String bid = request.getParameter("bid");
		// �Ƿ��¼
		if (user == null) {
			// �����
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("result", "NOTLOGIN");
			writer.write(json.toString());
			writer.close();
			return;
		}
		// �Ƿ����
		boolean flag = users.isFree(bid);
		int vip = Integer.parseInt(user.getVip());
		// ��ȡͼ��
		BookService books = new BookServiceImp();
		Book book = books.getBookinfo(bid);
		/**
		 * ���ͼ��
		 */
		if (flag) {
			// �Ƿ��ǻ�Ա
			if (vip == 1) {
				// Կ��
				request.getSession().setAttribute("bid", bid);
				// �������
				PrintWriter writer = response.getWriter();
				JSONObject json = new JSONObject();
				json.put("result", "YES");
				writer.write(json.toString());
				writer.close();
			} else {
				// ��ȡ����
				int count = user.getCounts();
				// ��������
				if (count > 4) {
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "NOTCOUNT");
					writer.write(json.toString());
					writer.close();
				} else {
					// ������,����һ��������ػ���
					users.reduceConut(user);
					// ������Ϣ
					UpdateUser.updating(request, user);
					// Կ��
					request.getSession().setAttribute("bid", bid);
					// �������
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "YES");
					writer.write(json.toString());
					writer.close();
				}
			}
		} else {
			/**
			 * ����ͼ��
			 */
			// ��ǰ�Ƿ����
			boolean dr = users.downloadRecord(user.getUid(), book.getBid());
			if (dr) {
				// Կ��
				request.getSession().setAttribute("bid", bid);
				// �������
				PrintWriter writer = response.getWriter();
				JSONObject json = new JSONObject();
				json.put("result", "YES");
				writer.write(json.toString());
				writer.close();
			} else {
				// ��ȡ�۸�,�Ƿ��Ա
				double price = Double.valueOf(book.getPrice());
				if (vip == 1) {
					DecimalFormat df = new DecimalFormat("#.00");
					String str = df.format(price * 0.8);
					price = Double.valueOf(str);
				}
				// ����Ƿ��㹻
				if (user.getMoney() >= price) {
					// �۳����,�����Ѽ�¼
					users.deductBalance(user, price, book.getBid());
					// ������Ϣ
					UpdateUser.updating(request, user);
					// Կ��
					request.getSession().setAttribute("bid", bid);
					// �������
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "YES");
					writer.write(json.toString());
					writer.close();
				} else {
					// ��ʾûǮ
					PrintWriter writer = response.getWriter();
					JSONObject json = new JSONObject();
					json.put("result", "NOTMONEY");
					writer.write(json.toString());
					writer.close();
				}
			}
		}
	}

	// �û����Ѽ�¼
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
		// ����
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

	// ��ȡ��¼��֤��
	public void getCode(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String phone = request.getParameter("phone");
		User user = users.isexistPhone(phone);
		// ������
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
		// ������֤��
		int random = PhoneCode.getRandom();
		// ����
		SmsSingleSenderResult result = Qcloudsms.sendsms(phone, random);
		if (result.result == 0) {
			session.removeAttribute("random");
			session.setAttribute("random", phone + "," + random + "," + GetTime.time());
			JSONObject json = new JSONObject().put("code", "0");
			response.getWriter().write(json.toString());
			session.setAttribute("time", newtime);
		} else {
			JSONObject json = new JSONObject().put("code", "�����˴���,���Ժ�����~");
			response.getWriter().write(json.toString());
		}
	}

	// ��֤���¼
	public void phoneLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String phone = request.getParameter("phone");
		String code = request.getParameter("code");
		HttpSession session = request.getSession();
		String arr = (String) session.getAttribute("random");
		// �ǻ�ȡ����֤��
		if (arr == null) {
			JSONObject json = new JSONObject();
			json.put("code", "ERROR");
			response.getWriter().write(json.toString());
			return;
		}
		// ��֤���Ƿ����
		String[] params = arr.split(",");
		if (((GetTime.time() - Integer.parseInt(params[2]))) > 180) {
			JSONObject json = new JSONObject();
			json.put("code", "OVERTIME");
			response.getWriter().write(json.toString());
			return;
		}
		// ��֤���Ƿ���ȷ
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
			//��ȡ��Ϣ
			CloseableHttpClient httpcilent = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder("https://graph.qq.com/user/get_user_info");
			builder.addParameter("access_token", access_token);
			builder.addParameter("oauth_consumer_key", "101530672");
			builder.addParameter("openid", openid);
			 java.net.URI uri = builder.build();
			 HttpGet get = new HttpGet(uri);
			 //����
			 CloseableHttpResponse execute = httpcilent.execute(get);
			 String result = EntityUtils.toString(execute.getEntity());
			JSONObject data = new JSONObject(result);
			if(data.get("ret").toString().equals("0")) {
				User qq = new User();
				qq.setUsername(filter(data.get("nickname").toString()));
				qq.setSex(data.get("gender").equals("��")?1:0);
				qq.setTelephone("QQ�û�");
				qq.setEmail("QQ�û�");
				qq.setImg(data.get("figureurl_1").toString());
				qq.setOpenid(openid);
				qq = users.QQ(qq);
				request.getSession().setAttribute("user", qq);
				//map.getSession(state).setAttribute("user", user);
				response.sendRedirect(this.getServletContext().getContextPath()+"/index.jsp");
			}
		}
	}
	//�������������EmojiFilter
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
