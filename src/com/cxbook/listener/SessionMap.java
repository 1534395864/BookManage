package com.cxbook.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

public class SessionMap {
	private static SessionMap instance;
	private static Map<String, HttpSession> map;

	// 构造私有化
	private SessionMap() {
		map = new HashMap<String, HttpSession>();
	}

	// 获取实例
	public static SessionMap getinstance() {
		if (instance == null) {
			instance = new SessionMap();
		}
		return instance;
	}

	// 添加
	public synchronized void addSession(HttpSession session) {
		if (session != null) {
			map.put(session.getId(), session);
		}
	}

	// 删除
	public synchronized void deleSession(HttpSession session) {
		if (session != null) {
			map.remove(session.getId());
		}
	}

	// 获取
	public synchronized HttpSession getSession(String id) {
		if (id == null)
			return null;
		return map.get(id);
	}
}
