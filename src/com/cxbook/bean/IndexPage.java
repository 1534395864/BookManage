package com.cxbook.bean;

import java.io.Serializable;
import java.util.List;

public class IndexPage implements Serializable {
	private static final long serialVersionUID = 1L;

	// ��������
	private int countdata;
	// ҳ�������
	private int pagedata;
	// ��ǰҳ
	private int currentpage;
	// ��һҳ
	private int indexpage = 1;
	// βҳ
	private int endpage;
	// ��ҳ����
	private int countpage;
	// ͼ��������
	List<Book> books;

	public int getCountdata() {
		return countdata;
	}

	public void setCountdata(int countdata) {
		this.countdata = countdata;
	}

	public int getPagedata() {
		return pagedata;
	}

	public void setPagedata(int pagedata) {
		this.pagedata = pagedata;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public int getIndexpage() {
		return indexpage;
	}

	public void setIndexpage(int indexpage) {
		this.indexpage = indexpage;
	}

	public int getEndpage() {
		return endpage;
	}

	public void setEndpage(int endpage) {
		this.endpage = endpage;
	}

	public int getCountpage() {
		return countpage;
	}

	public void setCountpage(int countpage) {
		this.countpage = countpage;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
