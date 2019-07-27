package com.cxbook.service;

import java.sql.SQLException;

import com.cxbook.bean.LayuiPage;
import com.cxbook.bean.User;

public interface UserService {
	//注册
	public boolean register(User user) throws SQLException;
	//判断是否注册过
	public boolean isexist(User user) throws SQLException;
	//密码登录
	public User passlogin(String username, String password) throws SQLException;
	//扣冲VIP钱
	public boolean vip(User user, double d) throws SQLException;
	//设置VIP
	public void setVip(String username) throws SQLException;
	//所有用户充值记录表
	public void user_consume(User user, String money, int xc) throws SQLException;
	//用户充值
	public boolean user_recharge(User user, String money) throws SQLException;
	//图书是否免费
	public boolean isFree(String bid) throws SQLException;
	//减少免费下载次数
	public void reduceConut(User user) throws SQLException;
	//查询是否购买过
	public boolean downloadRecord(int uid, String bid) throws SQLException;
	//扣除余额,记录消费
	public void deductBalance(User user, double price,String bid) throws SQLException;
	//查询个人消费记录
	public LayuiPage layuipage(LayuiPage layuipage) throws SQLException;
	//手机号是否存在
	public User isexistPhone(String phone) throws SQLException;
	//QQ用户是否登录过
	public User openid(String openid) throws SQLException;
	//注册
	public User QQ(User user) throws SQLException;
	
	
}
