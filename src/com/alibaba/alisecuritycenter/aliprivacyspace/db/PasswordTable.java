package com.alibaba.alisecuritycenter.aliprivacyspace.db;
/**
 * �洢��������������
 * <p> �����˸ñ���ֶ���
 * <p><b>������ password
 * @author linkun.lk
 *
 */
public interface PasswordTable {
	public static final String TABLE_NAME = "password";
	
	/**
	 * ��������id
	 */
	public static final String _ID = "_id";
	/**
	 * �������룬ͨ��MD5��������
	 */
	public static final String PASSWORD = "password";
	/**
	 * �ܱ�����
	 */
	public static final String QUESTION = "question";
	/**
	 * �ܱ���
	 */
	public static final String ANSWER = "answer";
	/**
	 * FLAG����չ��
	 */
	public static final String FLAG = "flag";
	
	
}
