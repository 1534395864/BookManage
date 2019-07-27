package com.cxbook.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

public class SessionMap {
	private static SessionMap instance;
	private static Map<String, HttpSession> map;

	// ����˽�л�
	private SessionMap() {
		map = new HashMap<String, HttpSession>();
	}

	// ��ȡʵ��
	public static SessionMap getinstance() {
		if (instance == null) {
			instance = new SessionMap();
		}
		return instance;
	}

	// ���
	public synchronized void addSession(HttpSession session) {
		if (session != null) {
			map.put(session.getId(), session);
		}
	}

	// ɾ��
	public synchronized void deleSession(HttpSession session) {
		if (session != null) {
			map.remove(session.getId());
		}
	}

	// ��ȡ
	public synchronized HttpSession getSession(String id) {
		if (id == null)
			return null;
		return map.get(id);
	}
}
