package com.cxbook.utils;

import java.io.IOException;

import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

public class Qcloudsms {

	private static int appid = 1400158171;

	private static String appKey = "150e9b2621887becb0ffb839c8a95e3e";
	// ����ģ��ID����Ҫ�ڶ���Ӧ��������
	private static int templateId = 221435;
	// ǩ��
	static String smsSign = "����Ƽ�";

	public static SmsSingleSenderResult sendsms(String phone, int random) {
		try {
			// ����,��֤���ʱ��
			String[] params = { random + "", "3" };
			SmsSingleSender ssender = new SmsSingleSender(appid, appKey);
			// ����
			SmsSingleSenderResult result = ssender.sendWithParam("86", phone, templateId, params, smsSign, "", "");
			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HTTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
