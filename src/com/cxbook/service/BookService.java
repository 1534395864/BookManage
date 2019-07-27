package com.cxbook.service;

import java.sql.SQLException;
import java.util.List;

import com.cxbook.bean.Book;
import com.cxbook.bean.Category;
import com.cxbook.bean.DownloadList;
import com.cxbook.bean.IndexPage;

public interface BookService {
	// 获取下载排行榜
	public List<DownloadList> getDownloadlist() throws SQLException;
	// 分页
	public IndexPage indepage(String currentpage) throws SQLException;
	//获取详情
	public Book getBookinfo(String bid) throws SQLException;
	//图书分类
	public List<Category> category() throws SQLException;
	//分类
	public IndexPage categoryinfo(String cid, String currentpage) throws SQLException;
	//增加用户图书下载记录
	public void Downloadrecord(Integer valueOf, Integer valueOf2, String string) throws SQLException;
	//增加图书下载次数
	public void addBookCount(Integer valueOf) throws SQLException;
	//新书排行榜
	public List<DownloadList> getNewBook() throws SQLException;
}
