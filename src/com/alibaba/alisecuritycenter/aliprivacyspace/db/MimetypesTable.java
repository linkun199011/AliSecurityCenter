package com.alibaba.alisecuritycenter.aliprivacyspace.db;

public interface MimetypesTable {
	public static final String TABLE_NAME = "mimetypes";
	
	public static final String _ID = "_id";
	public static final String MIMETYPE = "mimetype";
	
	/*
	 * CREATE TABLE mimetypes 
	 * (_id INTEGER PRIMARY KEY AUTOINCREMENT,
	 * mimetype TEXT NOT NULL);
	 * 
	 * */
	public static final int RAM_NUMBER = 18;
	public static final String[] RAW = {
		"vnd.android.cursor.item/phone_v2", 
		"vnd.android.cursor.item/sip_address",
		"vnd.android.cursor.item/email_v2",
		"vnd.android.cursor.item/im",
		"vnd.android.cursor.item/nickname",
		"vnd.android.cursor.item/organization",
		"vnd.android.cursor.item/name",
		"vnd.android.cursor.item/postal-address_v2",
		"vnd.android.cursor.item/identity",
		"vnd.android.cursor.item/photo",
		"vnd.android.cursor.item/group_membership",
		"vnd.android.cursor.item/note",
		"vnd.android.cursor.item/website",
		"vnd.com.miui.cursor.item/lunarBirthday",
		"vnd.android.cursor.item/vnd.com.tencent.mm.chatting.profile",
		"vnd.android.cursor.item/vnd.com.tencent.mm.chatting.voip.video",
		"vnd.android.cursor.item/vnd.com.tencent.mm.plugin.sns.timeline",
		"vnd.android.cursor.item/vnd.com.tencent.mobileqq.voicecall.profile"
		};
}
