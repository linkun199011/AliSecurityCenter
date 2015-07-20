package com.alibaba.alisecuritycenter.aliprivacyspace.db;

public class RawContactsTable {
	public static final String TABLE_NAME = "raw_contacts";
	
	public static final String _ID = "_id";
	public static final String CONTACT_ID = "contact_id";
	//public static final String IS_RESTRICTED = "is_restricted";
	public static final String SOURCEID = "sourceid";
	//public static final String RAW_CONTACT_IS_READ_ONLY = "raw_contact_is_read_only";
	public static final String VERSION = "version";
	public static final String DIRTY = "dirty";
	public static final String DELETED = "deleted";
	public static final String AGGREGATION_MODE = "aggregation_mode";
	//public static final String AGGREGATION_NEEDED = "aggregation_needed";
	public static final String CUSTOM_RINGTONE = "custom_ringtone";
	public static final String SEND_TO_VOICEMAIL = "send_to_voicemail";
	public static final String TIMES_CONTACTED = "times_contacted";
	public static final String LAST_TIME_CONTACTED = "last_time_contacted";
	public static final String STARRED = "starred";
	public static final String DISPLAY_NAME = "display_name";
	public static final String DISPLAY_NAME_ALT = "display_name_alt";
	public static final String DISPLAY_NAME_SOURCE = "display_name_source";
	public static final String PHONETIC_NAME = "phonetic_name";
	public static final String PHONETIC_NAME_STYLE = "phonetic_name_style";
	public static final String SORT_KEY = "sort_key";
	public static final String SORT_KEY_ALT = "sort_key_alt";
	//public static final String SORT_KEY_CUSTOM = "sort_key_custom";
	public static final String NAME_VERIFIED = "name_verified";
	public static final String SYNC1 = "sync1";
	public static final String SYNC2 = "sync2";
	public static final String SYNC3 = "sync3";
	public static final String SYNC4 = "sync4";
	
	private int _id;
	private int contact_id;
	//private int is_restricted;
	private String sourceid;
	//private int raw_contact_is_read_only;
	private int version;
	private int dirty;
	private int deleted;
	private int aggregation_mode;
	//private int aggregation_needed;
	private String cumstom_ringtone;
	private int send_to_voicemail;
	private int times_contacted;
	private int last_time_contacted;
	private int starred;
	private String display_name;
	private String display_name_alt;
	private int display_name_source;
	private String phonetic_name;
	private String phonetic_name_style;
	private String sort_key;
	private String sort_key_alt;
	//private String sort_key_custom;
	private int name_verified;
	private String sync1;
	private String sync2;
	private String sync3;
	private String sync4;

	// getter and setter
	public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public int getContact_id() {
        return contact_id;
    }
    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }
    public String getSourceid() {
        return sourceid;
    }
    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public int getDirty() {
        return dirty;
    }
    public void setDirty(int dirty) {
        this.dirty = dirty;
    }
    public int getAggregation_mode() {
        return aggregation_mode;
    }
    public void setAggregation_mode(int aggregation_mode) {
        this.aggregation_mode = aggregation_mode;
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
    public String getDisplay_name() {
        return display_name;
    }
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
    public String getDisplay_name_alt() {
        return display_name_alt;
    }
    public void setDisplay_name_alt(String display_name_alt) {
        this.display_name_alt = display_name_alt;
    }
    public int getDisplay_name_source() {
        return display_name_source;
    }
    public void setDisplay_name_source(int display_name_source) {
        this.display_name_source = display_name_source;
    }
    public String getPhonetic_name() {
        return phonetic_name;
    }
    public void setPhonetic_name(String phonetic_name) {
        this.phonetic_name = phonetic_name;
    }
    public String getPhonetic_name_style() {
        return phonetic_name_style;
    }
    public void setPhonetic_name_style(String phonetic_name_style) {
        this.phonetic_name_style = phonetic_name_style;
    }
    public String getSort_key() {
        return sort_key;
    }
    public void setSort_key(String sort_key) {
        this.sort_key = sort_key;
    }
    public String getSort_key_alt() {
        return sort_key_alt;
    }
    public void setSort_key_alt(String sort_key_alt) {
        this.sort_key_alt = sort_key_alt;
    }
    public int getName_verified() {
        return name_verified;
    }
    public void setName_verified(int name_verified) {
        this.name_verified = name_verified;
    }
    public String getSync1() {
        return sync1;
    }
    public void setSync1(String sync1) {
        this.sync1 = sync1;
    }
    public String getSync2() {
        return sync2;
    }
    public void setSync2(String sync2) {
        this.sync2 = sync2;
    }
    public String getSync3() {
        return sync3;
    }
    public void setSync3(String sync3) {
        this.sync3 = sync3;
    }
    public String getSync4() {
        return sync4;
    }
    public void setSync4(String sync4) {
        this.sync4 = sync4;
    }
    public int getDeleted() {
        return deleted;
    }
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}


/*public static final String[] RAW_CONTACT_STRING_ARRAY = {
    _ID,
    CONTACT_ID,
    IS_RESTRICTED,
    SOURCEID,
    RAW_CONTACT_IS_READ_ONLY,
    VERSION,
    DIRTY,
    DELETED,
    AGGREGATION_MODE,
    AGGREGATION_NEEDED,
    CUSTOM_RINGTONE,
    SEND_TO_VOICEMAIL,
    TIMES_CONTACTED,
    LAST_TIME_CONTACTED,
    STARRED,
    DISPLAY_NAME,
    DISPLAY_NAME_ALT,
    DISPLAY_NAME_SOURCE,
    PHONETIC_NAME,
    PHONETIC_NAME_STYLE,
    SORT_KEY,
    SORT_KEY_ALT,
    SORT_KEY_CUSTOM,
    NAME_VERIFIED,
    SYNC1,
    SYNC2,
    SYNC3,
    SYNC4
    };*/

/*
 * CREATE TABLE raw_contacts 
 * (_id INTEGER PRIMARY KEY AUTOINCREMENT,
 * contact_id INTEGER REFERENCES contacts(_id),
 * is_restricted INTEGER DEFAULT 0,
 * account_id INTEGER REFERENCES accounts(_id),                         >>>>>>>>>>>NOT ADD
 * sourceid TEXT,
 * raw_contact_is_read_only INTEGER NOT NULL DEFAULT 0,
 * version INTEGER NOT NULL DEFAULT 1,
 * dirty INTEGER NOT NULL DEFAULT 0,
 * deleted INTEGER NOT NULL DEFAULT 0,
 * aggregation_mode INTEGER NOT NULL DEFAULT 0,
 * aggregation_needed INTEGER NOT NULL DEFAULT 1,                       >>>>>>>>>>>NOT ADD
 * custom_ringtone TEXT,
 * send_to_voicemail INTEGER NOT NULL DEFAULT 0,
 * times_contacted INTEGER NOT NULL DEFAULT 0,
 * last_time_contacted INTEGER,
 * starred INTEGER NOT NULL DEFAULT 0,
 * display_name TEXT,display_name_alt TEXT,
 * display_name_source INTEGER NOT NULL DEFAULT 0,
 * phonetic_name TEXT,
 * phonetic_name_style TEXT,
 * sort_key TEXT COLLATE PHONEBOOK,
 * sort_key_alt TEXT COLLATE PHONEBOOK,
 * sort_key_custom TEXT COLLATE PHONEBOOK,                              >>>>>>>>>>>NOT ADD
 * name_verified INTEGER NOT NULL DEFAULT 0,
 * sync1 TEXT, sync2 TEXT, sync3 TEXT, sync4 TEXT );
 * 
 * */