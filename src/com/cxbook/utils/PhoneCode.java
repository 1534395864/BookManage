package com.cxbook.utils;

import java.util.Random;

public class PhoneCode {
	
	public static int getRandom() {
		Random code = new Random();
		return code.nextInt(8999)+1000;
	}
}
