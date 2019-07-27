package com.cxbook.service;

import java.sql.SQLException;
import java.util.List;

import com.cxbook.bean.Book;
import com.cxbook.bean.Category;
import com.cxbook.bean.DownloadList;
import com.cxbook.bean.IndexPage;

public interface BookService {
	// ��ȡ�������а�
	public List<DownloadList> getDownloadlist() throws SQLException;
	// ��ҳ
	public IndexPage indepage(String currentpage) throws SQLException;
	//��ȡ����
	public Book getBookinfo(String bid) throws SQLException;
	//ͼ�����
	public List<Category> category() throws SQLException;
	//����
	public IndexPage categoryinfo(String cid, String currentpage) throws SQLException;
	//�����û�ͼ�����ؼ�¼
	public void Downloadrecord(Integer valueOf, Integer valueOf2, String string) throws SQLException;
	//����ͼ�����ش���
	public void addBookCount(Integer valueOf) throws SQLException;
	//�������а�
	public List<DownloadList> getNewBook() throws SQLException;
}
