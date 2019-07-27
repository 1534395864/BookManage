package com.cxbook.bean;

import java.io.Serializable;
import java.util.List;

public class LayuiPage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String curr;
	private String  count;
	private int uid;
	private List<UserConsume> list;
	public String getCurr() {
		return curr;
	}
	public void setCurr(String curr) {
		this.curr = curr;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<UserConsume> getList() {
		return list;
	}
	public void setList(List<UserConsume> list) {
		this.list = list;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	
}
