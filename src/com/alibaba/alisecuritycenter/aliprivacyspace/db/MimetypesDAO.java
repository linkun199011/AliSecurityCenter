package com.alibaba.alisecuritycenter.aliprivacyspace.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MimetypesDAO {
	private static final String TAG = "MimetypsDAO" ;
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	
	public MimetypesDAO(Context context){
		dbHelper = DBHelper.getInstance(context);
		db = dbHelper.getWritableDatabase();
	}
	/**
	 * this function will be invoked when the application first installed.
	 * @throws SQLException
	 */
	public void initTable() throws SQLException{
		Log.i(TAG, "mimetypes table insert");
		synchronized (dbHelper) {
			String addSql = "insert into " + MimetypesTable.TABLE_NAME + "("
					+ MimetypesTable.MIMETYPE
					+ ") values (?);";
			for(int i = 0 ; i < MimetypesTable.RAM_NUMBER ; i++){
			    dbHelper.insertSQL(db, addSql, new Object[]{MimetypesTable.RAW[i]});
			}
		}
	}
	
	public boolean isEmpty() throws SQLException{
		synchronized (dbHelper) {
			String querySql = "select * from "+ MimetypesTable.TABLE_NAME ;
			boolean isEmpty = dbHelper.isEmpty(db, querySql);
			return isEmpty;
		}
	}
}
