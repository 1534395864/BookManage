package com.cxbook.dao;

import java.sql.SQLException;
import java.util.List;

import com.cxbook.bean.Book;
import com.cxbook.bean.Category;
import com.cxbook.bean.DownloadList;
import com.cxbook.bean.IndexPage;

public interface BookDao {
	public List<DownloadList> get_download_list() throws SQLException;

	public void indexpage(IndexPage indexpage) throws SQLException;

	public Book get_book(String bid) throws SQLException;

	public List<Category> cetegory() throws SQLException;

	public void indexpage(String cid, IndexPage indexpage) throws SQLException;

	public boolean isFree(String bid) throws SQLException;

	public void downloadrecord(Integer uid, Integer bid, String time) throws SQLException;
	
	public void addBookCount(Integer bid) throws SQLException;

	public List<DownloadList> get_newBook() throws SQLException;
}
