package com.alibaba.alisecuritycenter.aliprivacyspace.db;

public class ContactsTable {
	public static final String TABLE_NAME = "contacts";
	
	public static final String _ID = "_id";
	public static final String PHOTO_ID = "photo_id";
	public static final String CUMSTOM_RINGTONE = "custom_ringtone";
	public static final String SEND_TO_VOICEMAIL = "send_to_voicemail";
	public static final String TIMES_CONTACTED = "times_contacted";
	public static final String LAST_TIME_CONTACTED = "last_time_contacted";
	public static final String STARRED = "starred";
	public static final String HAS_PHONE_NUMBER = "has_phone_number";
	public static final String LOOKUP = "lookup";
	//public static final String COMPANY = "company";
	//public static final String NICKNAME = "nickname";
	//public static final String CONTACT_ACCOUNT_TYPE = "contact_account_type";
	//public static final String SINGLE_IS_RESTRICTED = "single_is_restricted";
	public static final String CONTACT_LAST_UPDATED_TIMESTAMP = "contact_last_updated_timestamp";
	
	private int _id;
	// 此处的photo_id是data中的row，但是data中的_id是自增长的，
	// 如果按照顺序应该是data表先添加，然后再把id填入此处。
	// 那么，数据恢复的时候也会有类似的问题存在。
	// 可否这么处理？（data中的表只对有name和phone的进行增加和删除，其它的都不变。不删除带有photo的data行）
	private int photo_id;
	private String cumstom_ringtone;
	private int send_to_voicemail;
	private int times_contacted;
	private int last_time_contacted;
	private int starred;//??
	private int has_phone_number;
	private String lookup;
	//private String company;
	//private String nickname;
	//private String contact_account_type;
	//private int single_is_restricted;
	private int contact_last_updated_timestamp;
	
	// getter and setter
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public int getPhoto_id() {
        return photo_id;
    }
    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }
    public String getCumstom_ringtone() {
        return cumstom_ringtone;
    }
    public void setCumstom_ringtone(String cumstom_ringtone) {
        this.cumstom_ringtone = cumstom_ringtone;
    }
    public int getSend_to_voicemail() {
        return send_to_voicemail;
    }
    public void setSend_to_voicemail(int send_to_voicemail) {
        this.send_to_voicemail = send_to_voicemail;
    }
    public int getTimes_contacted() {
        return times_contacted;
    }
    public void setTimes_contacted(int times_contacted) {
        this.times_contacted = times_contacted;
    }
    public int getLast_time_contacted() {
        return last_time_contacted;
    }
    public void setLast_time_contacted(int last_time_contacted) {
        this.last_time_contacted = last_time_contacted;
    }
    public int getStarred() {
        return starred;
    }
    public void setStarred(int starred) {
        this.starred = starred;
    }
    public int getHas_phone_number() {
        return has_phone_number;
    }
    public void setHas_phone_number(int has_phone_number) {
        this.has_phone_number = has_phone_number;
    }
    public String getLookup() {
        return lookup;
    }
    public void setLookup(String lookup) {
        this.lookup = lookup;
    }
    /*public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getContact_account_type() {
        return contact_account_type;
    }
    public void setContact_account_type(String contact_account_type) {
        this.contact_account_type = contact_account_type;
    }*/
    public int getContact_last_updated_timestamp() {
        return contact_last_updated_timestamp;
    }
    public void setContact_last_updated_timestamp(int contact_last_updated_timestamp) {
        this.contact_last_updated_timestamp = contact_last_updated_timestamp;
    }
	
}

/*
 * CREATE TABLE contacts 
 * (_id INTEGER PRIMARY KEY AUTOINCREMENT,
 * name_raw_contact_id INTEGER REFERENCES raw_contacts(_id),
 * photo_id INTEGER REFERENCES data(_id),                                  >>>>>>>>>> 改成INTERGER 。可以随意插入任意值
 * photo_file_id INTEGER REFERENCES photo_files(_id),                      >>>>>>>>>>>NOT ADD
 * custom_ringtone TEXT,
 * send_to_voicemail INTEGER NOT NULL DEFAULT 0,
 * times_contacted INTEGER NOT NULL DEFAULT 0,
 * last_time_contacted INTEGER,
 * starred INTEGER NOT NULL DEFAULT 0,
 * has_phone_number INTEGER NOT NULL DEFAULT 0,
 * lookup TEXT,
 * company TEXT,
 * nickname TEXT,
 * contact_account_type TEXT,
 * status_update_id INTEGER REFERENCES data(_id),                        >>>>>>>>>>>NOT ADD
 * single_is_restricted INTEGER NOT NULL DEFAULT 0,                         >>>>>>>>>>NOT ADD
 * contact_last_updated_timestamp INTEGER);*/