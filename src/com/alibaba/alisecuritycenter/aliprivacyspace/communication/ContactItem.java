package com.alibaba.alisecuritycenter.aliprivacyspace.communication;

import android.graphics.Bitmap;

public class ContactItem {
	private String name;
	private String phone;
	private Bitmap head;
	private int contactId;
	private int rawContactId;
	
	public ContactItem(){
	    
	}
	public ContactItem(String name, String number, Bitmap head, int contactId, int rawContactId) {
		super();
		this.name = name;
		this.phone = number;
		this.head = head;
		this.contactId = contactId;
		this.setRawContactId(rawContactId);
	}

	public ContactItem(String name, String number, int contactId, int rawContactId) {
		super();
		this.name = name;
		this.phone = number;
		this.contactId = contactId;
		this.setRawContactId(rawContactId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String number) {
		this.phone = number;
	}

	public Bitmap getHead() {
		return head;
	}

	public void setHead(Bitmap head) {
		this.head = head;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

    public int getRawContactId() {
        return rawContactId;
    }

    public void setRawContactId(int rawContactId) {
        this.rawContactId = rawContactId;
    }
	
	
}
