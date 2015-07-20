package com.alibaba.alisecuritycenter.aliprivacyspace.entity;

import com.alibaba.alisecuritycenter.aliprivacyspace.db.MimetypesTable;

public class MimetypesInfo implements MimetypesTable {
	private int _id;
	private String mimetype;
	
	public MimetypesInfo() {
		super();
	}

	public MimetypesInfo(int _id, String mimetype) {
		super();
		this._id = _id;
		this.mimetype = mimetype;
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
}
