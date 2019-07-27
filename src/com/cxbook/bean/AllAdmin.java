package com.cxbook.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class AllAdmin  implements Serializable{
	private String count;
	private String msg;
	private String code;
	private String page;
	private List<AdminInfo> data;
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public List<AdminInfo> getData() {
		return data;
	}
	public void setData(List<AdminInfo> data) {
		this.data = data;
	}
	
}
