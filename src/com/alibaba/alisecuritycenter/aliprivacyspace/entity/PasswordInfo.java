package com.alibaba.alisecuritycenter.aliprivacyspace.entity;

import com.alibaba.alisecuritycenter.aliprivacyspace.db.PasswordTable;

/**
 * �洢��������������
 * <p><b>���ݿ�����privacyspace.db
 * <p><b>������ password
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
	 * ��������id
	 */
	private int _id ;
	/**
	 * �������룬ͨ��MD5��������
	 */
	private String password;
	/**
	 * �ܱ�����
	 */
	private String question;
	/**
	 * �ܱ���
	 */
	private String answer;
	/**
	 * FLAG����չ��
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
