package com.cxbook.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class Root implements Serializable {
	private String date;
	private String curr;
	private String count;
	private String countmoney;
	private List<CountMoney> data;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
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
	public List<CountMoney> getData() {
		return data;
	}
	public void setData(List<CountMoney> data) {
		this.data = data;
	}
	public String getCountmoney() {
		return countmoney;
	}
	public void setCountmoney(String countmoney) {
		this.countmoney = countmoney;
	}
}
