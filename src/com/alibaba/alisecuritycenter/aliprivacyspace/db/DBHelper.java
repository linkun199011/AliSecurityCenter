package com.alibaba.alisecuritycenter.aliprivacyspace.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private Context mContext;
	private static final String TAG = "DBHelper";
	private static final String DATABASE_NAME = "privacyspace.db";
	private static final int DATABASE_VERSION = 1;
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null,DATABASE_VERSION);
		mContext = context;
		Log.i(TAG, "dbHelper: "+this.toString());
	}

	private static DBHelper mInstance;  
	  
    public synchronized static DBHelper getInstance(Context context) {  
        if (mInstance == null) {  
            mInstance = new DBHelper(context);  
        }
        return mInstance;
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		synchronized (this) {
			createPasswordTable(db);
			// the order of inserting data into the following four tables should also be : mime->raw->contacts->data
			createMimetypesTable(db);    // First init
			createRawContactsTable(db);  // Second init
			createContactsTable(db);     // Third init
			createDateTable(db);         // Fourth init
			// �õ��ûᵼ��DBHelper�ݹ����
			//initMimetypesTable();
		}
		
	}

	/**
	 * ����password �����ڴ洢��˽�ռ�����������һ����롣
	 * @param db
	 */
	private void createPasswordTable(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + PasswordTable.TABLE_NAME + "(" 
				+ PasswordTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ PasswordTable.PASSWORD + " TEXT," 
				+ PasswordTable.QUESTION + " TEXT,"
				+ PasswordTable.ANSWER + " TEXT,"
				+ PasswordTable.FLAG + " TEXT);";
		db.execSQL(sql);
	}

	/**
	 * ����data ��
	 * @param db
	 */
	private void createDateTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + DataTable.TABLE_NAME + "(" 
				+ DataTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ DataTable.MIMETYPE + " TEXT,"
				+ DataTable.RAW_CONTACT_ID + " INTEGER REFERENCES raw_contacts(_id) NOT NULL,"
				+ DataTable.IS_PRIMARY + " INTEGER NOT NULL DEFAULT 0,"
				+ DataTable.IS_SUPER_PRIMARY + " INTEGER NOT NULL DEFAULT 0,"
				+ DataTable.DATA_VERSION + " INTEGER NOT NULL DEFAULT 0,"
				+ DataTable.DATA1 + " TEXT,"
				+ DataTable.DATA2 + " TEXT,"
				+ DataTable.DATA3 + " TEXT,"
				+ DataTable.DATA4 + " TEXT,"
				+ DataTable.DATA5 + " TEXT,"
				+ DataTable.DATA6 + " TEXT,"
				+ DataTable.DATA7 + " TEXT,"
				+ DataTable.DATA8 + " TEXT,"
				+ DataTable.DATA9 + " TEXT,"
				+ DataTable.DATA10 + " TEXT,"
				+ DataTable.DATA11 + " TEXT,"
				+ DataTable.DATA12 + " TEXT,"
				+ DataTable.DATA13 + " TEXT,"
				+ DataTable.DATA14 + " TEXT,"
				+ DataTable.DATA15 + " TEXT,"
				+ DataTable.DATA_SYNC1 + " TEXT,"
				+ DataTable.DATA_SYNC2 + " TEXT,"
				+ DataTable.DATA_SYNC3 + " TEXT,"
				+ DataTable.DATA_SYNC4 + " TEXT);";
		db.execSQL(sql);
	}

	/**
	 * ���� raw_contacts ��
	 * @param db
	 */
	private void createRawContactsTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + RawContactsTable.TABLE_NAME + "(" 
				+ RawContactsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ RawContactsTable.CONTACT_ID + " INTEGER REFERENCES contacts(_id)," 
				+ RawContactsTable.SOURCEID + " TEXT," 
				+ RawContactsTable.VERSION + " INTEGER NOT NULL DEFAULT 1," 
				+ RawContactsTable.DIRTY + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.DELETED + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.AGGREGATION_MODE + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.CUSTOM_RINGTONE + " TEXT," 
				+ RawContactsTable.SEND_TO_VOICEMAIL + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.TIMES_CONTACTED + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.LAST_TIME_CONTACTED + " INTEGER," 
				+ RawContactsTable.STARRED + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.DISPLAY_NAME + " TEXT," 
				+ RawContactsTable.DISPLAY_NAME_ALT + " TEXT," 
				+ RawContactsTable.DISPLAY_NAME_SOURCE + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.PHONETIC_NAME + " TEXT," 
				+ RawContactsTable.PHONETIC_NAME_STYLE + " TEXT," 
				+ RawContactsTable.SORT_KEY + " TEXT," 
				+ RawContactsTable.SORT_KEY_ALT + " TEXT," 
				+ RawContactsTable.NAME_VERIFIED + " INTEGER NOT NULL DEFAULT 0," 
				+ RawContactsTable.SYNC1 + " TEXT," 
				+ RawContactsTable.SYNC2 + " TEXT," 
				+ RawContactsTable.SYNC3 + " TEXT," 
				+ RawContactsTable.SYNC4 + " TEXT);";
		db.execSQL(sql);
	}

	/**
	 * ����contacts��
	 * @param db
	 */
	private void createContactsTable(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + ContactsTable.TABLE_NAME + "(" 
				+ ContactsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ ContactsTable.PHOTO_ID + " INTEGER," 
				+ ContactsTable.CUMSTOM_RINGTONE + " TEXT," 
				+ ContactsTable.SEND_TO_VOICEMAIL + " INTEGER NOT NULL DEFAULT 0," 
				+ ContactsTable.TIMES_CONTACTED + " INTEGER NOT NULL DEFAULT 0," 
				+ ContactsTable.LAST_TIME_CONTACTED + " INTEGER," 
				+ ContactsTable.STARRED + " INTEGER NOT NULL DEFAULT 0," 
				+ ContactsTable.HAS_PHONE_NUMBER + " INTEGER NOT NULL DEFAULT 0," 
				+ ContactsTable.LOOKUP + " TEXT," 
				+ ContactsTable.CONTACT_LAST_UPDATED_TIMESTAMP + " INTEGER);";
		db.execSQL(sql);
	}

	/**
	 * ���� mimetypes ��
	 * @param db
	 */
	private void createMimetypesTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + MimetypesTable.TABLE_NAME + "(" 
				+ MimetypesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ MimetypesTable.MIMETYPE + " TEXT NOT NULL);";
		db.execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		/*String sql = "DROP TABLE IF EXISTS " + PasswordTable.TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);*/
	}
	
	/**
     * ִ��1��sqlָ��
     * ֻ��һ����������������Դ�һ�������ȥ
     * @param db    SQLiteDatabase 
     * @param sqlStr    ָ����sql��䣬String����
     * @param obj    ����ռλ�����ڵ�ֵ
     */
	public void insertSQL(SQLiteDatabase db , String sqlStr , Object[] obj){
	    if (!db.isOpen()) {
            Log.i(TAG, "db.isOpne() == false");
            db = this.getWritableDatabase();
        }
        db.beginTransaction();
        try {
            db.execSQL(sqlStr, obj);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) { 
            e.printStackTrace();
        } finally {
            db.endTransaction();  
            db.close();
        }
	}
	/**
	 * ִ��1��sqlָ��,
	 * ֻ��һ����������������Դ�һ�������ȥ
	 * @param db    SQLiteDatabase 
	 * @param sqlStr    ָ����sql��䣬String����
	 * @param obj    ����ռλ�����ڵ�ֵ
	 */
	public void updateSQL(SQLiteDatabase db , String sqlStr , Object[] obj){
	    if (!db.isOpen()) {
	        Log.i(TAG, "db.isOpne() == false");
	        db = this.getWritableDatabase();
	    }
	    db.beginTransaction();
	    try {
	        db.execSQL(sqlStr, obj);
	        db.setTransactionSuccessful();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) { 
	        e.printStackTrace();
	    } finally {
	        db.endTransaction();  
	        db.close();
	    }
	}

	/**
	 * ��ѯĳ��table�Ƿ�Ϊ��
	 * @param db SQLiteDatabase���ݿ�
	 * @param querySql ��ѯsql���
	 * @return �գ�����true�����򷵻�false
	 */
	public boolean isEmpty(SQLiteDatabase db , String querySql){
		boolean isEmpty = false;
		if (!db.isOpen()) {
			Log.i(TAG, "db.isOpne() == false");
			db = this.getReadableDatabase();
		}
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(querySql, null);
			if(cursor.moveToNext()){
				isEmpty = false;
			}
			else{
				isEmpty = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		} finally {
			db.close();
			cursor.close();
		}
		return isEmpty;
	}
	
	
}
