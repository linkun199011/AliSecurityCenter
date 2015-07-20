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
	 * 往Password表中添加记录
	 * @param passwordInfo
	 * 
	 */
	public void insert(PasswordInfo passwordInfo) throws SQLException{
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			// 为了防止SQL注入，输入的参数用?来占位符来指定
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
	 * 更新password表中的记录
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
	 * 查看password表是否为空
	 * @return 为空返回1，不为空返回0
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
	                // password表不为空
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
	 * 获取password表中_id = 1 的记录的password值
	 * @return 返回String类型的password
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
	 * 获取password表中_id = 1 的记录的Answer值
	 * @return 返回String类型的answer
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
	 * 查找数据库记录
	 * @return
	 * @throws SQLException
	 */
	public PasswordInfo find() throws SQLException{
		PasswordInfo passwordInfo = null ;
		synchronized (dbHelper) {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String querySql = "select * from password";
			// 由于该表的特殊性，password最多只有一条记录
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
