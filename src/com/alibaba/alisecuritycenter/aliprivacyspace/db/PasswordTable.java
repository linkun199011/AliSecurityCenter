package com.alibaba.alisecuritycenter.aliprivacyspace.db;
/**
 * 存储手势密码的密码表
 * <p> 定义了该表的字段名
 * <p><b>表名： password
 * @author linkun.lk
 *
 */
public interface PasswordTable {
	public static final String TABLE_NAME = "password";
	
	/**
	 * 自增长的id
	 */
	public static final String _ID = "_id";
	/**
	 * 手势密码，通过MD5加密所得
	 */
	public static final String PASSWORD = "password";
	/**
	 * 密保问题
	 */
	public static final String QUESTION = "question";
	/**
	 * 密保答案
	 */
	public static final String ANSWER = "answer";
	/**
	 * FLAG：扩展用
	 */
	public static final String FLAG = "flag";
	
	
}
