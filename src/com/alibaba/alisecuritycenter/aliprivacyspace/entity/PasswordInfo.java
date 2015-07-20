package com.alibaba.alisecuritycenter.aliprivacyspace.entity;

import com.alibaba.alisecuritycenter.aliprivacyspace.db.PasswordTable;

/**
 * 存储手势密码的密码表
 * <p><b>数据库名：privacyspace.db
 * <p><b>表名： password
 * @author linkun.lk
 *
 */
public class PasswordInfo implements PasswordTable{
	
	
	public PasswordInfo() {
		super();
	}
	
	public PasswordInfo(int _id, String password, String question,
			String answer, int flag) {
		super();
		this._id = _id;
		this.password = password;
		this.question = question;
		this.answer = answer;
		this.flag = flag;
	}

	/**
	 * 自增长的id
	 */
	private int _id ;
	/**
	 * 手势密码，通过MD5加密所得
	 */
	private String password;
	/**
	 * 密保问题
	 */
	private String question;
	/**
	 * 密保答案
	 */
	private String answer;
	/**
	 * FLAG：扩展用
	 */
	private int flag;
	
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
	
	
	
}
