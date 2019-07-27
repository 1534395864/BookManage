package com.cxbook.service;

import java.sql.SQLException;

import com.cxbook.bean.LayuiPage;
import com.cxbook.bean.User;

public interface UserService {
	//ע��
	public boolean register(User user) throws SQLException;
	//�ж��Ƿ�ע���
	public boolean isexist(User user) throws SQLException;
	//�����¼
	public User passlogin(String username, String password) throws SQLException;
	//�۳�VIPǮ
	public boolean vip(User user, double d) throws SQLException;
	//����VIP
	public void setVip(String username) throws SQLException;
	//�����û���ֵ��¼��
	public void user_consume(User user, String money, int xc) throws SQLException;
	//�û���ֵ
	public boolean user_recharge(User user, String money) throws SQLException;
	//ͼ���Ƿ����
	public boolean isFree(String bid) throws SQLException;
	//����������ش���
	public void reduceConut(User user) throws SQLException;
	//��ѯ�Ƿ����
	public boolean downloadRecord(int uid, String bid) throws SQLException;
	//�۳����,��¼����
	public void deductBalance(User user, double price,String bid) throws SQLException;
	//��ѯ�������Ѽ�¼
	public LayuiPage layuipage(LayuiPage layuipage) throws SQLException;
	//�ֻ����Ƿ����
	public User isexistPhone(String phone) throws SQLException;
	//QQ�û��Ƿ��¼��
	public User openid(String openid) throws SQLException;
	//ע��
	public User QQ(User user) throws SQLException;
	
	
}
