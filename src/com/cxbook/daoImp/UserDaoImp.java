package com.cxbook.daoImp;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.cxbook.bean.LayuiPage;
import com.cxbook.bean.User;
import com.cxbook.bean.UserConsume;
import com.cxbook.dao.UserDao;
import com.cxbook.utils.JDBC;
import com.cxbook.utils.MD5;

public class UserDaoImp implements UserDao {
	private static QueryRunner qr = new QueryRunner(JDBC.getc3p0());
	private static ReadWriteLock rw = new ReentrantReadWriteLock();
	private static ReadLock r = (ReadLock) rw.readLock();
	private static WriteLock w = (WriteLock) rw.writeLock();

	@Override
	public boolean register(User user) throws SQLException {
		String sql = "INSERT INTO user (uid,username,password,sex,telephone,email,rid,md5) VALUES (null, ?,?,?,?,?,?,?)";
		Object[] params = { user.getUsername(), user.getPassword(), user.getSex(), user.getTelephone(), user.getEmail(), user.getRid(), MD5.md5(user.getPassword()) };
		int update = qr.update(sql, params);
		if (update != 0)
			return true;
		return false;
	}

	@Override
	public boolean isexist(User user) throws SQLException {
		String sql = "select * from user where username = ? or telephone =?";
		Object[] params = { user.getUsername(), user.getTelephone() };
		User query = qr.query(sql, new BeanHandler<>(User.class), params);
		if (query != null)
			return true;
		return false;
	}

	@Override
	public User password(String username, String password) throws SQLException {
		String sql = "select * from user where username =? and md5 = ?";
		Object[] params = { username, password };
		User query = qr.query(sql, new BeanHandler<User>(User.class), params);
		return query;
	}

	@Override
	public boolean vip(User user, double d) throws SQLException {
		r.lock();
		String sql = "select money from user where username = ?";
		Object query = qr.query(sql, new ScalarHandler<Object>(), user.getUsername());
		r.unlock();
		w.lock();
		String money = "update user set money = ?-300.00 where username = ?;";
		int update = qr.update(money, query.toString(), user.getUsername());
		w.unlock();
		return update == 0 ? false : true;
	}

	@Override
	public void setvip(String username) throws SQLException {
		String sql = "update user set vip = 1 where username = ? ";
		qr.update(sql, username);
	}

	@Override
	public void user_consume(UserConsume consume) throws SQLException {
		
		String sql = "INSERT INTO user_consume (uid,created,vip,xc,money) VALUES(?,?,?,?,?)";
		Object[] params = { consume.getUid(), consume.getCreated(), consume.getVip(), consume.getXc(), consume.getMoney() };
		w.lock();
		qr.update(sql, params);
		w.unlock();
	}

	@Override
	public boolean user_recharge(int uid, double d) throws SQLException {
		r.lock();
		String sql = "select money from user where uid = ?";
		Object query = qr.query(sql, new ScalarHandler<Object>(),uid);
		r.unlock();
		w.lock();
		String money = "update user set money = ?+? where uid = ?;";
		int update = qr.update(money, query.toString(),d,uid);
		w.unlock();
		return update == 0 ? false : true;
	}

	@Override
	public void reduceCount(User user) throws SQLException {
		String sql = "update user set counts = (select a.counts from (select counts from user where uid = ?) as a)+1 where uid = ?";
		qr.update(sql,user.getUid(),user.getUid());
	}

	@Override
	public boolean downloadRecord(int uid, String bid) throws SQLException {
		String sql = "select * from downloads where uid = ? and bid =? ";
		r.lock();
		Object query = qr.query(sql, new ScalarHandler<>(),uid,bid);
		r.unlock();
		return query == null?false:true;
		
	}

	@Override
	public void deductBalance(int uid, double price) throws SQLException {
		String sql ="update user set money = (select a.money from (select money from user where uid =?) as a)-? where uid =?";
		Object[] params = {uid,price,uid};
		qr.update(sql,params);
	}

	@Override
	public void consume(UserConsume consume) throws SQLException {
		w.lock();
		String sql = "INSERT INTO user_consume (uid,created,bid,vip,xc,money) VALUES(?,?,?,?,?,?)";
		Object[] params = { consume.getUid(), consume.getCreated(),consume.getBid(),consume.getVip(), consume.getXc(), consume.getMoney() };
		qr.update(sql, params);
		w.unlock();
	}

	@Override
	public LayuiPage layuipage(LayuiPage layuipage) throws SQLException {
		String count = "select count(*) from user_consume where uid =? and xc =2";
		Object query = qr.query(count, new ScalarHandler<>(),layuipage.getUid());
		layuipage.setCount(query.toString());
		String sql = "select a.*,b.name from user_consume a ,book b where uid = ? and xc = 2 and a.bid =b.bid limit ?,5";
		Object[] params = {layuipage.getUid(),(Integer.valueOf(layuipage.getCurr())-1)*5};
		List<UserConsume> query2 = qr.query(sql, new BeanListHandler<UserConsume>(UserConsume.class),params);
		layuipage.setList(query2);
		return layuipage;
	}

	@Override
	public User isexistPhone(String phone) throws SQLException{
		String sql = "select * from user where telephone = ?";
		User user = qr.query(sql, new BeanHandler<User>(User.class),phone);
		return user;
	}

	@Override
	public User openid(String openid) throws SQLException {
		String sql = "select * from user where openid = ?";
		User query = qr.query(sql, new BeanHandler<User>(User.class),openid);
		return query;
	}

	@Override
	public User QQ(User user) throws SQLException {
		String sql = "INSERT INTO user (uid,username,sex,telephone,email,rid,img,openid) VALUES (null, ?,?,?,?,?,?,?)";
		Object[] params = {user.getUsername(),user.getSex(),user.getTelephone(),user.getEmail(),user.getRid(),user.getImg(),user.getOpenid()};
		int update = qr.update(sql, params);
		return openid(user.getOpenid());
	}

	@Override
	public User openid(String username, String openid) throws SQLException {
		String sql = "select * from user where username =? and openid = ?";
		Object[] params = { username, openid };
		User query = qr.query(sql, new BeanHandler<User>(User.class), params);
		return query;
	}

}
