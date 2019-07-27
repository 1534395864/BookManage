package com.cxbook.daoImp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.cxbook.bean.AdminInfo;
import com.cxbook.bean.AllAdmin;
import com.cxbook.bean.Book;
import com.cxbook.bean.CountMoney;
import com.cxbook.bean.GetAllBook;
import com.cxbook.bean.Root;
import com.cxbook.dao.AdminDao;
import com.cxbook.utils.JDBC;

public class AdminDaoImp implements AdminDao {
	private static QueryRunner qr = new QueryRunner(JDBC.getc3p0());
	private static ReadWriteLock rw = new ReentrantReadWriteLock();
	private static ReadLock r= (ReadLock) rw.readLock();
	private static WriteLock w=  (WriteLock) rw.writeLock();
	@Override
	public List<String> getUrl(int rid) throws SQLException {
		String sql = "select c.url from role a ,role_function b ,function c where a.rid = ? and a.rid = b.rid and b.fid =c.fid";
		 List<String> query = qr.query(sql, new ColumnListHandler<String>(),rid);
		return query;
	}
	@Override
	public GetAllBook getAll(GetAllBook books) throws SQLException {
		String sql = "select count(*) from book";
		Object count = qr.query(sql, new ScalarHandler<>());
		books.setCount(count.toString());
		String data ="select * from book limit ?,10";
		List<Book> query = qr.query(data, new BeanListHandler<Book>(Book.class),(Integer.valueOf(books.getPage())-1) * 10);
		books.setData(query);
		return books;
	}
	@Override
	public List<Book> getbook(String bookname) throws SQLException {
		String sql ="select * from book  where name like ?";
		List<Book> query = qr.query(sql, new BeanListHandler<Book>(Book.class),"%"+bookname+"%");
		return query;
	}
	@Override
	public void delete_book(int i,String bid) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "update book set status = ? where bid = ?";
		qr.update(sql,i,bid);
	}
	@Override
	public boolean deitbook(Book book) throws SQLException {
		String sql = "update book set name =?,author = ?,price = ?,content = ? where bid = ?";
		Object[] params = {book.getName(),book.getAuthor(),book.getPrice(),book.getContent(),book.getBid()};
		int update = qr.update(sql,params);
		return update >0 ?true:false;
	}
	@Override
	public String addbook(Book book) throws SQLException {
		w.lock();
		String sql = "select count(*) from book";
		Object query = qr.query(sql, new ScalarHandler<Object>());
		String insert ="insert into book(bid,name,author,price,image,content,created) values(?,?,?,?,?,?,?)";
		Object[] params = {Integer.parseInt(query.toString())+1,book.getName(),book.getAuthor(),book.getPrice(),"/book/images/book"+(Integer.parseInt(query.toString())+1)+".jpg",book.getContent(),book.getCreated()};
		int update = qr.update(insert,params);
		return update >0 ?Integer.parseInt(query.toString())+1+"":null;
	}
	@Override
	public void addcategory(String path, String[] classify) throws SQLException {
		for(String cid:classify) {
			String sql ="insert into book_category values(?,?)";
			Object[] parmas = {path,cid};
			qr.update(sql,parmas);
		}
	}
	@Override
	public boolean addAdmin(String name, String password, String md5) throws SQLException {
		String sql = "insert into user(username,password,rid,md5) values(?,?,2,?)";
		Object[] params = {name,password,md5};
		int update = qr.update(sql,params);
		return update >0 ?true:false;
	}
	@Override
	public Root queryMoney(Root root) throws SQLException {
		//所有参数
		Object[] params = {"%"+root.getDate()+"%","%"+root.getDate()+"%",(Integer.parseInt(root.getCurr())-1)*5,5};
		//月份和总金额
		String count ="select count(*) as count,sum(money) as money from user_consume where created like ?";
		Map<String, Object> query = qr.query(count, new MapHandler(),params[0]);
		boolean flag = query.get("count").toString().equals("0");
		if(!flag) {
			root.setCount(query.get("count").toString());
			root.setCountmoney(query.get("money").toString());
		}
		//本页
		String sql = "(select a.username ,c.created,b.name,c.xc,c.money from user a,book b,(select * from user_consume where created like ?) c where c.uid = a.uid and c.bid = b.bid) union (select a.username ,c.created,c.created,c.xc,c.money from user a,book b,(select * from user_consume where created like ?) c where c.uid = a.uid and c.bid is null group by c.id)order by created desc LIMIT ?,?";
		List<CountMoney> query2 = qr.query(sql, new BeanListHandler<CountMoney>(CountMoney.class),params);
		root.setData(query2);
		return root;
	}
	@Override
	public AllAdmin getAllAdmin(AllAdmin aa) throws SQLException {
		String count = "select count(*) from user where rid = 2";
		Object query = qr.query(count, new ScalarHandler<>());
		aa.setCount(query.toString());
		String sql ="select * from user where rid = 2";
		List<AdminInfo> query2 = qr.query(sql, new BeanListHandler<AdminInfo>(AdminInfo.class));
		aa.setData(query2);
		return aa;
	}
	@Override
	public boolean editadmin(String uid, String username, String password, String status, String md5) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "update user set username = ?,password = ?,status = ?,md5 = ? where uid = ?";
		Object[] params = {username,password,status,md5,uid};
		return qr.update(sql,params)  > 0?true:false;
	}
	@Override
	public boolean deleteAdmin(String uid) throws SQLException {
		String sql = "delete from user where uid = ?";
		int update = qr.update(sql,uid);
		return update >0 ?true:false;
	}
	@Override
	public List<AdminInfo> getadmin(String name) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "select * from user where username like ?";
		List<AdminInfo> query = qr.query(sql, new BeanListHandler<AdminInfo>(AdminInfo.class),"%"+name+"%");
		return query;
	}

}
