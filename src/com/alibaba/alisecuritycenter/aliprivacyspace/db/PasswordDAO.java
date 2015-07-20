package com.alibaba.alisecuritycenter.aliprivacyspace.db;

import com.alibaba.alisecuritycenter.aliprivacyspace.entity.PasswordInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PasswordDAO {
	private String TAG = "PasswordDAO";
	private DBHelper dbHelper;
	
	public PasswordDAO(Context context){
		dbHelper = DBHelper.getInstance(context);
		Log.i(TAG, "dbHelper: "+dbHelper.toString());
	}
	
	/**
	 * ��Password������Ӽ�¼
	 * @param passwordInfo
	 * 
	 */
	public void insert(PasswordInfo passwordInfo) throws SQLException{
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			// Ϊ�˷�ֹSQLע�룬����Ĳ�����?��ռλ����ָ��
			String addSql = "insert into password (password , question , answer , flag) values (?,?,?,?)";
			try{
			    db.execSQL(addSql,new Object[] {passwordInfo.getPassword(),passwordInfo.getQuestion(),passwordInfo.getAnswer(),passwordInfo.getFlag()});
			} catch (Exception e){
			    e.printStackTrace();
			} finally {
			    db.close();
			}
			Log.i(TAG, "insert");
		}
	}
	/**
	 * ����password���еļ�¼
	 * @throws SQLException
	 */
	public void update(PasswordInfo passwordInfo) throws SQLException{
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			passwordInfo.set_id(1);
			String updateSql = "update " + PasswordTable.TABLE_NAME + " SET " 
					+ PasswordTable.PASSWORD + " = '" + passwordInfo.getPassword() + "',"
					+ PasswordTable.QUESTION + " = '" + passwordInfo.getQuestion() + "',"
					+ PasswordTable.ANSWER + " = '" + passwordInfo.getAnswer() + "',"
					+ PasswordTable.FLAG + " = " + passwordInfo.getFlag()+ " WHERE " 
					+ PasswordTable._ID + " = " + passwordInfo.get_id();
			try{
			    db.execSQL(updateSql);
			} catch (Exception e){
			    e.printStackTrace();
			} finally {
			    db.close();
			}
			Log.i(TAG, "update");
		}
	}
	/**
	 * �鿴password���Ƿ�Ϊ��
	 * @return Ϊ�շ���1����Ϊ�շ���0
	 * @throws SQLException
	 */
	public boolean isEmpty() throws SQLException{
		boolean isEmpty = false;
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String querySql = "select * from " + PasswordTable.TABLE_NAME;
			Cursor cursor = null;
			try{
			    cursor = db.rawQuery(querySql, null);
			    if(cursor.moveToNext()){
	                // password��Ϊ��
	                isEmpty = false;
	            }
	            else{
	                isEmpty = true;
	            }
			} catch (Exception e){
			    Log.i(TAG, "isEmpty() :" + e.toString());
			} finally{
			    cursor.close();
			    db.close();
			}
			
		}
		return isEmpty;
	}
	/**
	 * ��ȡpassword����_id = 1 �ļ�¼��passwordֵ
	 * @return ����String���͵�password
	 * @throws SQLException
	 */
	public String getMD5Password() throws SQLException{
		String MD5Password = null;
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String querySql = "select password from password where _id = 1";
			Cursor cursor = null;
			try{
			    cursor = db.rawQuery(querySql, null);
			    if(cursor.moveToNext()){
	                MD5Password = cursor.getString(cursor.getColumnIndex("password"));
	            }
	            else{
	                MD5Password = null;
	            }
			} catch (Exception e){
			    e.printStackTrace();
			}
			finally {
			    cursor.close();
			    db.close();
			}
			
		}
		return MD5Password;
	}
	/**
	 * ��ȡpassword����_id = 1 �ļ�¼��Answerֵ
	 * @return ����String���͵�answer
	 * @throws SQLException
	 */
	public String getMD5Answer() throws SQLException{
		String MD5Answer = null;
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String querySql = "select answer from password where _id = 1";
			Cursor cursor = null;
			try{
			    cursor = db.rawQuery(querySql, null);
			    if(cursor.moveToNext()){
	                MD5Answer = cursor.getString(cursor.getColumnIndex("answer"));
	            }
	            else{
	                MD5Answer = null;
	            }
			} catch (Exception e){
			    e.printStackTrace();
			} finally {
			    cursor.close();
			    db.close();
			}
		}
		return MD5Answer;
	}
	/**
	 * �������ݿ��¼
	 * @return
	 * @throws SQLException
	 */
	public PasswordInfo find() throws SQLException{
		PasswordInfo passwordInfo = null ;
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String querySql = "select * from password";
			// ���ڸñ�������ԣ�password���ֻ��һ����¼
			Cursor cursor = null;
			try{
			    cursor = db.rawQuery(querySql, null);
	            if(cursor.moveToNext()){
	                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
	                String password = cursor.getString(cursor.getColumnIndex("password"));
	                String question = cursor.getString(cursor.getColumnIndex("question"));
	                String answer = cursor.getString(cursor.getColumnIndex("answer"));
	                int flag = cursor.getInt(cursor.getColumnIndex("flag"));
	                passwordInfo = new PasswordInfo(_id, password, question, answer, flag);
	            }
	            else{
	                passwordInfo = null;
	            }
			} catch (Exception e){
			    e.printStackTrace();
			} finally {
			    cursor.close();
	            db.close();
			}
			
		}
		return passwordInfo;
	}
	
}
