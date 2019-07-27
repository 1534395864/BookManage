package com.cxbook.utils;

import java.io.IOException;

import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

public class Qcloudsms {

	private static int appid = 1400158171;

	private static String appKey = "150e9b2621887becb0ffb839c8a95e3e";
	// 短信模板ID，需要在短信应用中申请
	private static int templateId = 221435;
	// 签名
	static String smsSign = "纯袖科技";

	public static SmsSingleSenderResult sendsms(String phone, int random) {
		try {
			// 参数,验证码和时间
			String[] params = { random + "", "3" };
			SmsSingleSender ssender = new SmsSingleSender(appid, appKey);
			// 发送
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
