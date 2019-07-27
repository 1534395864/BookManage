package com.cxbook.utils;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.cxbook.bean.User;
import com.cxbook.dao.UserDao;
import com.cxbook.daoImp.UserDaoImp;

public class UpdateUser {
	public static void updating(HttpServletRequest request,User user) throws SQLException {
		UserDao dao =new UserDaoImp();
		if(user.getOpenid() != null) {
			User password = dao.openid(user.getUsername(), user.getOpenid());
			request.getSession().setAttribute("user", password);
			return;
		}
		User password = dao.password(user.getUsername(), user.getMd5());
		request.getSession().setAttribute("user", password);
	}
}
