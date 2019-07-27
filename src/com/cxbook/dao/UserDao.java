package com.cxbook.dao;

import java.sql.SQLException;

import com.cxbook.bean.LayuiPage;
import com.cxbook.bean.User;
import com.cxbook.bean.UserConsume;

public interface UserDao {
	
	public boolean register(User user) throws SQLException;

	public boolean isexist(User user) throws SQLException;

	public User password(String username, String password) throws SQLException;

	public boolean vip(User user, double d) throws SQLException;

	public void setvip(String username) throws SQLException;

	public void user_consume(UserConsume consume) throws SQLException;

	public boolean user_recharge(int uid, double d) throws SQLException;

	public void reduceCount(User user) throws SQLException;

	public boolean downloadRecord(int uid, String bid) throws SQLException;

	public void deductBalance(int uid, double price) throws SQLException;

	public void consume(UserConsume consume) throws SQLException;

	public LayuiPage layuipage(LayuiPage layuipage) throws SQLException;

	public User isexistPhone(String phone) throws SQLException;

	public User openid(String openid) throws SQLException;

	public User QQ(User user) throws SQLException;

	public User openid(String username, String openid) throws SQLException;
}
