package com.cxbook.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTime {
	public static String get() {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 String format = formatter.format(new Date().getTime());
		 return format;
	}
	public static int time() {
		long time = System.currentTimeMillis()/1000;
		return (int) time;
	}
	public static void main(String[] args) {
		System.out.println(time());
	}
}
