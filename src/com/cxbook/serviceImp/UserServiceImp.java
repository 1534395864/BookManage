package com.cxbook.serviceImp;

import java.sql.SQLException;

import com.cxbook.bean.LayuiPage;
import com.cxbook.bean.User;
import com.cxbook.bean.UserConsume;
import com.cxbook.dao.BookDao;
import com.cxbook.dao.UserDao;
import com.cxbook.daoImp.BookDaoImp;
import com.cxbook.daoImp.UserDaoImp;
import com.cxbook.service.UserService;
import com.cxbook.utils.GetTime;

public class UserServiceImp implements UserService {
	private static final UserDao dao = new UserDaoImp();

	@Override
	public boolean register(User user) throws SQLException {
		if (user.getEmail() == "") {
			user.setEmail("ÔÝÎ´ÌîÐ´");
		}
		if(user.getSex() ==1) {
			user.setImg("./userico/man.png");
		}else {
			user.setImg("./userico/woman.png");
		}
		user.setRid(1);
		return dao.register(user);
	}

	@Override
	public boolean isexist(User user) throws SQLException {
		return dao.isexist(user);
	}

	@Override
	public User passlogin(String username, String password) throws SQLException {
		return dao.password(username, password);
	}

	@Override
	public boolean vip(User user, double d) throws SQLException {
		return dao.vip(user, d);
	}

	@Override
	public void setVip(String username) throws SQLException {
		dao.setvip(username);
	}

	@Override
	public void user_consume(User user, String money, int xc) throws SQLException {
			UserConsume consume = new UserConsume();
			consume.setUid(user.getUid());
			consume.setCreated(GetTime.get());
			consume.setVip(Integer.parseInt(user.getVip()));
			consume.setXc(xc);
			consume.setMoney(money);
			dao.user_consume(consume);
	}

	@Override
	public boolean user_recharge(User user, String money) throws SQLException {
		double d = Double.valueOf(money);
		return dao.user_recharge(user.getUid(),d);
	}

	@Override
	public boolean isFree(String bid) throws SQLException {
		BookDao dao = new BookDaoImp();
		return dao.isFree(bid);
	}

	@Override
	public void reduceConut(User user) throws SQLException {
		dao.reduceCount(user);
	}

	@Override
	public boolean downloadRecord(int uid, String bid) throws SQLException {
		return dao.downloadRecord(uid,bid);
	}

	@Override
	public void deductBalance(User user, double price,String bid) throws SQLException {
		//¿ÛÓà¶î
		dao.deductBalance(user.getUid(),price);
		//´æÏû·Ñ¼ÇÂ¼
		UserConsume consume = new UserConsume();
		consume.setUid(user.getUid());
		consume.setCreated(GetTime.get());
		consume.setVip(Integer.parseInt(user.getVip()));
		consume.setXc(2);
		consume.setBid(Integer.valueOf(bid));
		consume.setMoney(String.valueOf(price));
		dao.consume(consume);
	}

	@Override
	public LayuiPage layuipage(LayuiPage layuipage) throws SQLException {
		return dao.layuipage(layuipage);
	}

	@Override
	public User isexistPhone(String phone) throws SQLException {
		// TODO Auto-generated method stub
		return dao.isexistPhone(phone);
	}

	@Override
	public User openid(String openid) throws SQLException {
		// TODO Auto-generated method stub
		return dao.openid(openid);
	}

	@Override
	public User QQ(User user) throws SQLException {
		user.setRid(1);
		return dao.QQ(user);
	}

}
