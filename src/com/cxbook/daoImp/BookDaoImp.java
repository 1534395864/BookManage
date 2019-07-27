package com.cxbook.daoImp;

import com.cxbook.bean.Book;
import com.cxbook.bean.Category;
import com.cxbook.bean.DownloadList;
import com.cxbook.bean.IndexPage;
import com.cxbook.dao.BookDao;
import com.cxbook.utils.JDBC;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class BookDaoImp implements BookDao {
	private static QueryRunner qr = new QueryRunner(JDBC.getc3p0());

	@Override
	public List<DownloadList> get_download_list() throws SQLException {
		String sql = "select * from book where status = 1 order by counts desc  LIMIT 0,10";
		List<DownloadList> booklist = qr.query(sql, new BeanListHandler<DownloadList>(DownloadList.class));
		return booklist;
	}

	@Override
	public void indexpage(IndexPage indexpage) throws SQLException {
		// ��ѯ������
		String countdata = "select count(*) from book where status = 1";
		Object query2 = qr.query(countdata, new ScalarHandler<>());
		String string = query2.toString();
		indexpage.setCountdata(Integer.parseInt(string));
		// ��ѯָ��ҳ
		String sql = "select * from book where status = 1 limit ?,?";
		Object[] params = { indexpage.getCurrentpage() * 12, 12 };
		List<Book> query = qr.query(sql, new BeanListHandler<Book>(Book.class), params);
		indexpage.setBooks(query);
	}

	@Override
	public Book get_book(String bid) throws SQLException {
		String sql = "select * from book where bid = ? and status = 1";
		Book query = qr.query(sql, new BeanHandler<Book>(Book.class), bid);
		return query;
	}

	// ����
	@Override
	public List<Category> cetegory() throws SQLException {
		String sql = "select * from category";
		List<Category> query = qr.query(sql, new BeanListHandler<Category>(Category.class));
		return query;
	}

	@Override
	public void indexpage(String cid, IndexPage indexpage) throws SQLException {
		// ��ѯ������
		String countdata = "select count(*) from category a,book_category b ,book c where a.cid = ? and a.cid=b.cid and b.bid = c.bid and c.status = 1";
		Object query2 = qr.query(countdata, new ScalarHandler<>(),cid);
		String string = query2.toString();
		indexpage.setCountdata(Integer.parseInt(string));
		// ��ѯָ��ҳ
		String sql = "select * from category a,book_category b ,book c where a.cid = ? and a.cid=b.cid and b.bid = c.bid and c.status = 1 limit ?,?";
		Object[] params = {cid,indexpage.getCurrentpage() * 12, 12 };
		List<Book> query = qr.query(sql, new BeanListHandler<Book>(Book.class), params);
		indexpage.setBooks(query);
	}

	@Override
	public boolean isFree(String bid) throws SQLException {
		String sql = "select count(*) from book b,book_category a where b.bid = a.bid and a.cid = 6 and b.bid = ? and b.status = 1";
		Object query = qr.query(sql, new ScalarHandler<>(),bid);
		return Integer.parseInt(query.toString()) == 1?true:false;
	}

	@Override
	public void downloadrecord(Integer uid, Integer bid, String time) throws SQLException {
		String sql = "insert into downloads(uid,bid,created) values(?,?,?)";
		qr.update(sql,uid,bid,time);
	}
	//�������ش���
	@Override
	public void addBookCount(Integer bid) throws SQLException {
		String sql ="update book set counts = (select a.counts from (select counts from book where bid = ?) as a)+1 where bid =?";
		qr.update(sql,bid,bid);
	}

	@Override
	public List<DownloadList> get_newBook() throws SQLException {
		String sql ="select * from book where status = 1 order by created desc  LIMIT 0,10";
		List<DownloadList> query = qr.query(sql, new BeanListHandler<DownloadList>(DownloadList.class));
		return query;
	}

}
