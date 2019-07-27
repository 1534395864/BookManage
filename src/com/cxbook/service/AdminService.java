package com.cxbook.service;

import java.sql.SQLException;
import java.util.List;

import com.cxbook.bean.AdminInfo;
import com.cxbook.bean.AllAdmin;
import com.cxbook.bean.Book;
import com.cxbook.bean.GetAllBook;
import com.cxbook.bean.Root;

public interface AdminService {

	GetAllBook getAll(GetAllBook books) throws SQLException;

	List<Book> getbook(String bookname) throws SQLException;

	void deleteBook(int i, String bid) throws SQLException;

	boolean editbook(Book book) throws SQLException;

	boolean addbook(Book book, String[] classify, String img) throws SQLException;

	boolean addAdmin(String name, String password, String md5) throws SQLException;

	Root queryMoney(Root root) throws SQLException;

	AllAdmin getAllAdmin(AllAdmin aa) throws SQLException;

	boolean editadmin(String uid, String username, String password, String status, String md5) throws SQLException;

	boolean deleteAdmin(String uid) throws SQLException;

	List<AdminInfo> getadmin(String name) throws SQLException;
	
}
