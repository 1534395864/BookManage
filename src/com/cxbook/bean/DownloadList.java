package com.cxbook.bean;

import java.io.Serializable;

public class DownloadList implements Serializable {

	private static final long serialVersionUID = 1L;
	private String bid;
	private String name;

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
