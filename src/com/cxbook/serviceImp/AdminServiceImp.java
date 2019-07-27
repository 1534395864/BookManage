package com.cxbook.serviceImp;


import java.sql.SQLException;
import java.util.List;

import com.cxbook.bean.AdminInfo;
import com.cxbook.bean.AllAdmin;
import com.cxbook.bean.Book;
import com.cxbook.bean.GetAllBook;
import com.cxbook.bean.Root;
import com.cxbook.dao.AdminDao;
import com.cxbook.daoImp.AdminDaoImp;
import com.cxbook.service.AdminService;
import com.cxbook.utils.Base64TOFile;

public class AdminServiceImp implements AdminService {
	private static final AdminDao dao =new AdminDaoImp();
	@Override
	public GetAllBook getAll(GetAllBook books) throws SQLException {
		return dao.getAll(books);
	}
	@Override
	public List<Book> getbook(String bookname) throws SQLException {
		return dao.getbook(bookname);
	}
	@Override
	public void deleteBook(int i,String bid) throws SQLException {
		// TODO Auto-generated method stub
		dao.delete_book(i, bid);
	}
	@Override
	public boolean editbook(Book book) throws SQLException {
		return dao.deitbook(book);
	}
	@Override
	public boolean addbook(Book book, String[] classify, String img) throws SQLException {
		String path = dao.addbook(book);
		if(path != null) {
			Base64TOFile.createBase64File("D://BOOK/images/book"+path+".jpg",Base64TOFile.decode(img));
			dao.addcategory(path,classify);
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public boolean addAdmin(String name, String password, String md5) throws SQLException {
		return dao.addAdmin(name,password,md5);
	}
	@Override
	public Root queryMoney(Root root) throws SQLException {
		// TODO Auto-generated method stub
		return dao.queryMoney(root);
	}
	@Override
	public AllAdmin getAllAdmin(AllAdmin aa) throws SQLException {
		return dao.getAllAdmin(aa);
	}
	@Override
	public boolean editadmin(String uid, String username, String password, String status, String md5) throws SQLException {
		// TODO Auto-generated method stub
		return dao.editadmin(uid,username,password,status,md5);
	}
	@Override
	public boolean deleteAdmin(String uid) throws SQLException {
		// TODO Auto-generated method stub
		return dao.deleteAdmin(uid);
	}
	@Override
	public List<AdminInfo> getadmin(String name) throws SQLException {
		// TODO Auto-generated method stub
		return dao.getadmin(name);
	}

}
