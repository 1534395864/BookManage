package com.cxbook.serviceImp;

import java.sql.SQLException;
import java.util.List;

import com.cxbook.bean.Book;
import com.cxbook.bean.Category;
import com.cxbook.bean.DownloadList;
import com.cxbook.bean.IndexPage;
import com.cxbook.dao.BookDao;
import com.cxbook.daoImp.BookDaoImp;
import com.cxbook.service.BookService;

public class BookServiceImp implements BookService {
	private static final BookDao dao = new BookDaoImp();

	// 下载榜
	@Override
	public List<DownloadList> getDownloadlist() throws SQLException {
		return dao.get_download_list();
	}
	// 分页
	@Override
	public IndexPage indepage(String currentpage) throws SQLException {
		IndexPage indexpage = new IndexPage();
		// 当前页
		int currpage = Integer.parseInt(currentpage);
		indexpage.setCurrentpage(currpage - 1);
		// 显示几本
		indexpage.setPagedata(12);
		// 调用数据库
		dao.indexpage(indexpage);
		indexpage.setIndexpage(1);
		indexpage.setCountpage(indexpage.getCountdata() % 12 == 0 ? indexpage.getCountdata() / 12 : indexpage.getCountdata() / 12 + 1);
		return indexpage;
	}

	// 图书详情页
	@Override
	public Book getBookinfo(String bid) throws SQLException {
		return dao.get_book(bid);
	}
	//页面导航
	@Override
	public List<Category> category() throws SQLException {
		return dao.cetegory();
	}
	//首页分页
	@Override
	public IndexPage categoryinfo(String cid, String currentpage) throws SQLException {
		IndexPage indexpage = new IndexPage();
		// 当前页
		int currpage = Integer.parseInt(currentpage);
		indexpage.setCurrentpage(currpage - 1);
		// 显示几本
		indexpage.setPagedata(12);
		// 调用数据库
		dao.indexpage(cid,indexpage);
		indexpage.setIndexpage(1);
		indexpage.setCountpage(indexpage.getCountdata() % 12 == 0 ? indexpage.getCountdata() / 12 : indexpage.getCountdata() / 12 + 1);
		return indexpage;
	}
	//
	@Override
	public void Downloadrecord(Integer uid, Integer bid, String time) throws SQLException {
			dao.downloadrecord(uid,bid,time);
	}
	//
	@Override
	public void addBookCount(Integer bid) throws SQLException {
		dao.addBookCount(bid);
	}
	@Override
	public List<DownloadList> getNewBook() throws SQLException {
		return dao.get_newBook();
	}
		
	
}
