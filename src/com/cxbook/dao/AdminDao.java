package com.cxbook.dao;

import java.sql.SQLException;
import java.util.List;

import com.cxbook.bean.AdminInfo;
import com.cxbook.bean.AllAdmin;
import com.cxbook.bean.Book;
import com.cxbook.bean.GetAllBook;
import com.cxbook.bean.Root;

public interface AdminDao {
	public List<String> getUrl(int rid) throws SQLException;

	public GetAllBook getAll(GetAllBook books) throws SQLException;

	public List<Book> getbook(String bookname) throws SQLException;

	void delete_book(int i, String bid) throws SQLException;

	public boolean deitbook(Book book) throws SQLException;

	public String addbook(Book book) throws SQLException;

	public void addcategory(String path, String[] classify) throws SQLException;

	public boolean addAdmin(String name, String password, String md5) throws SQLException;

	public Root queryMoney(Root root) throws SQLException;

	public AllAdmin getAllAdmin(AllAdmin aa) throws SQLException;

	public boolean editadmin(String uid, String username, String password, String status, String md5) throws SQLException;

	public boolean deleteAdmin(String uid) throws SQLException;

	public List<AdminInfo> getadmin(String name) throws SQLException;

}
