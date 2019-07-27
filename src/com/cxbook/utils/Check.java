package com.cxbook.utils;

public class Check {
	public static boolean check_token(String session, String user) {
		if (user == null || session == null) {
			return false;
		}
		if (!user.equals(session)) {
			return false;
		}
		return true;
	}

	public static boolean check_unix(int session) {
		int newunix = (int) (System.currentTimeMillis() / 1000);
		if ((newunix - session) > 60*25) {
			return false;
		} else {
			return true;
		}
	}
}
