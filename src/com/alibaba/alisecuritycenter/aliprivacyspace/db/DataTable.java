package com.alibaba.alisecuritycenter.aliprivacyspace.db;

public class DataTable {
	public static final String TABLE_NAME = "data";
	
	public static final String _ID = "_id";
	public static final String MIMETYPE = "mimetype";
	public static final String RAW_CONTACT_ID = "raw_contact_id";
	//public static final String IS_READ_ONLY = "is_read_only";
	public static final String IS_PRIMARY = "is_primary";
	public static final String IS_SUPER_PRIMARY = "is_super_primary";
	public static final String DATA_VERSION = "data_version";
	public static final String DATA1 = "data1";
	public static final String DATA2 = "data2";
	public static final String DATA3 = "data3";
	public static final String DATA4 = "data4";
	public static final String DATA5 = "data5";
	public static final String DATA6 = "data6";
	public static final String DATA7 = "data7";
	public static final String DATA8 = "data8";
	public static final String DATA9 = "data9";
	public static final String DATA10 = "data10";
	public static final String DATA11 = "data11";
	public static final String DATA12 = "data12";
	public static final String DATA13 = "data13";
	public static final String DATA14 = "data14";
	public static final String DATA15 = "data15";
	public static final String DATA_SYNC1 = "data_sync1";
	public static final String DATA_SYNC2 = "data_sync2";
	public static final String DATA_SYNC3 = "data_sync3";
	public static final String DATA_SYNC4 = "data_sync4";
	
	private int _id;
	private String mimetype;
	private int raw_contact_id;
	//private int is_read_only;
	private int is_primary;
	private int is_super_primary;
    private int data_version;
	private String data1;
	private String data2;
	private String data3;
	private String data4;
	private String data5;
	private String data6;
	private String data7;
	private String data8;
	private String data9;
	private String data10;
	private String data11;
	private String data12;
	private String data13;
	private String data14;
	private byte[] data15;
	private String data_sync1;
	private String data_sync2;
	private String data_sync3;
	private String data_sync4;

	// getter and setter 
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
    public int getRaw_contact_id() {
        return raw_contact_id;
    }
    public void setRaw_contact_id(int raw_contact_id) {
        this.raw_contact_id = raw_contact_id;
    }
    public int getIs_primary() {
        return is_primary;
    }
    public void setIs_primary(int is_primary) {
        this.is_primary = is_primary;
    }
    public int getIs_super_primary() {
        return is_super_primary;
    }
    public void setIs_super_primary(int is_super_primary) {
        this.is_super_primary = is_super_primary;
    }
    public int getData_version() {
        return data_version;
    }
    public void setData_version(int data_version) {
        this.data_version = data_version;
    }
    public String getData1() {
        return data1;
    }
    public void setData1(String data1) {
        this.data1 = data1;
    }
    public String getData2() {
        return data2;
    }
    public void setData2(String data2) {
        this.data2 = data2;
    }
    public String getData3() {
        return data3;
    }
    public void setData3(String data3) {
        this.data3 = data3;
    }
    public String getData4() {
        return data4;
    }
    public void setData4(String data4) {
        this.data4 = data4;
    }
    public String getData5() {
        return data5;
    }
    public void setData5(String data5) {
        this.data5 = data5;
    }
    public String getData6() {
        return data6;
    }
    public void setData6(String data6) {
        this.data6 = data6;
    }
    public String getData7() {
        return data7;
    }
    public void setData7(String data7) {
        this.data7 = data7;
    }
    public String getData8() {
        return data8;
    }
    public void setData8(String data8) {
        this.data8 = data8;
    }
    public String getData9() {
        return data9;
    }
    public void setData9(String data9) {
        this.data9 = data9;
    }
    public String getData10() {
        return data10;
    }
    public void setData10(String data10) {
        this.data10 = data10;
    }
    public String getData11() {
        return data11;
    }
    public void setData11(String data11) {
        this.data11 = data11;
    }
    public String getData12() {
        return data12;
    }
    public void setData12(String data12) {
        this.data12 = data12;
    }
    public String getData13() {
        return data13;
    }
    public void setData13(String data13) {
        this.data13 = data13;
    }
    public String getData14() {
        return data14;
    }
    public void setData14(String data14) {
        this.data14 = data14;
    }
    public byte[] getData15() {
        return data15;
    }
    public void setData15(byte[] bs) {
        this.data15 = bs;
    }
    public String getData_sync1() {
        return data_sync1;
    }
    public void setData_sync1(String data_sync1) {
        this.data_sync1 = data_sync1;
    }
    public String getData_sync2() {
        return data_sync2;
    }
    public void setData_sync2(String data_sync2) {
        this.data_sync2 = data_sync2;
    }
    public String getData_sync3() {
        return data_sync3;
    }
    public void setData_sync3(String data_sync3) {
        this.data_sync3 = data_sync3;
    }
    public String getData_sync4() {
        return data_sync4;
    }
    public void setData_sync4(String data_sync4) {
        this.data_sync4 = data_sync4;
    }
	
}

/* data±í
 * 
 * CREATE TABLE data 
 * (_id INTEGER PRIMARY KEY AUTOINCREMENT,
 * package_id INTEGER REFERENCES package(_id),                              >>>>>>>>>>>NOT ADD
 * mimetype_id INTEGER REFERENCES mimetype(_id) NOT NULL,
 * raw_contact_id INTEGER REFERENCES raw_contacts(_id) NOT NULL,
 * is_read_only INTEGER NOT NULL DEFAULT 0,
 * is_primary INTEGER NOT NULL DEFAULT 0,
 * is_super_primary INTEGER NOT NULL DEFAULT 0,
 * data_version INTEGER NOT NULL DEFAULT 0,
 * data1 TEXT,data2 TEXT,data3 TEXT,data4 TEXT,data5 TEXT,
 * data6 TEXT,data7 TEXT,data8 TEXT,data9 TEXT,data10 TEXT,
 * data11 TEXT,data12 TEXT,data13 TEXT,data14 TEXT,data15 TEXT,
 * data_sync1 TEXT, data_sync2 TEXT, data_sync3 TEXT, data_sync4 TEXT );
 * 
 * */