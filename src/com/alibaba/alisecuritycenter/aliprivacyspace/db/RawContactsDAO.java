package com.alibaba.alisecuritycenter.aliprivacyspace.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class RawContactsDAO {
    private static final String TAG = "RawContactsDAO";
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    public RawContactsDAO(Context context) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        mContext = context;
    }

    /**
     * get system's raw_contacts table's raw by rawcontactId.
     * 
     * @param rawContactId
     * @return
     */
    public RawContactsTable getSystemRawByRawContactId(int rawContactId) {
        RawContactsTable rct = new RawContactsTable();
        Uri rawContactsUri = ContentUris.withAppendedId(
                RawContacts.CONTENT_URI, rawContactId);
        Cursor c = mContext.getContentResolver().query(rawContactsUri, null,
                null, null, null);
        try {
            while (c.moveToNext()) {
                if (!c.isNull(0)) {
                    // no need to set rawcontactId
                    // rct.set_id(c.getInt(c.getColumnIndex(RawContactsTable._ID)));
                    // no need to set contactId as well
                    //rct.setIs_restricted(c.getInt(c.getColumnIndex(RawContactsTable.IS_RESTRICTED)));
                    rct.setSourceid(c.getString(c
                            .getColumnIndex(RawContactsTable.SOURCEID)));
                    rct.setVersion(c.getInt(c
                            .getColumnIndex(RawContactsTable.VERSION)));
                    rct.setDirty(c.getInt(c
                            .getColumnIndex(RawContactsTable.DIRTY)));
                    rct.setDeleted(c.getInt(c
                            .getColumnIndex(RawContactsTable.DELETED)));
                    rct.setAggregation_mode(c.getInt(c
                            .getColumnIndex(RawContactsTable.AGGREGATION_MODE)));
                    rct.setCumstom_ringtone(c.getString(c
                            .getColumnIndex(RawContactsTable.CUSTOM_RINGTONE)));
                    rct.setSend_to_voicemail(c.getInt(c
                            .getColumnIndex(RawContactsTable.SEND_TO_VOICEMAIL)));
                    rct.setTimes_contacted(c.getInt(c
                            .getColumnIndex(RawContactsTable.TIMES_CONTACTED)));
                    rct.setLast_time_contacted(c.getInt(c
                            .getColumnIndex(RawContactsTable.LAST_TIME_CONTACTED)));
                    rct.setStarred(c.getInt(c
                            .getColumnIndex(RawContactsTable.STARRED)));
                    rct.setDisplay_name(c.getString(c
                            .getColumnIndex(RawContactsTable.DISPLAY_NAME)));
                    rct.setDisplay_name_alt(c.getString(c
                            .getColumnIndex(RawContactsTable.DISPLAY_NAME_ALT)));
                    rct.setDisplay_name_source(c.getInt(c
                            .getColumnIndex(RawContactsTable.DISPLAY_NAME_SOURCE)));
                    rct.setPhonetic_name(c.getString(c
                            .getColumnIndex(RawContactsTable.PHONETIC_NAME)));
                    rct.setPhonetic_name_style(c.getString(c
                            .getColumnIndex(RawContactsTable.PHONETIC_NAME_STYLE)));
                    rct.setSort_key(c.getString(c
                            .getColumnIndex(RawContactsTable.SORT_KEY)));
                    rct.setSort_key_alt(c.getString(c
                            .getColumnIndex(RawContactsTable.SORT_KEY_ALT)));
                    rct.setName_verified(c.getInt(c
                            .getColumnIndex(RawContactsTable.NAME_VERIFIED)));
                    rct.setSync1(c.getString(c
                            .getColumnIndex(RawContactsTable.SYNC1)));
                    rct.setSync2(c.getString(c
                            .getColumnIndex(RawContactsTable.SYNC2)));
                    rct.setSync3(c.getString(c
                            .getColumnIndex(RawContactsTable.SYNC3)));
                    rct.setSync4(c.getString(c
                            .getColumnIndex(RawContactsTable.SYNC4)));
                }
            }
        } finally {
            c.close();
        }
        return rct;
    }

    /**
     * insert one raw into RawContactTable
     * 
     * @param rawContactId system's rawContact id
     * @param insertContactId just inserted contact's id
     * @return return inserted raw_contact's id . if error ,return -1.
     * @throws SQLException
     */
    public int insertRawContact(int rawContactId, int insertContactId)
            throws SQLException {
        int insertRawContactId = -1;
        RawContactsTable rct = getSystemRawByRawContactId(rawContactId);
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                String insertSql = "insert into "
                        + RawContactsTable.TABLE_NAME
                        + "( "
                        + RawContactsTable._ID
                        + ","
                        + RawContactsTable.CONTACT_ID
                        + ","
                        + RawContactsTable.SOURCEID
                        + ","
                        + RawContactsTable.VERSION
                        + ","
                        + RawContactsTable.DIRTY
                        + ","
                        + RawContactsTable.DELETED
                        + ","
                        + RawContactsTable.AGGREGATION_MODE
                        + ","
                        + RawContactsTable.CUSTOM_RINGTONE
                        + ","
                        + RawContactsTable.SEND_TO_VOICEMAIL
                        + ","
                        + RawContactsTable.TIMES_CONTACTED
                        + ","
                        + RawContactsTable.LAST_TIME_CONTACTED
                        + ","
                        + RawContactsTable.STARRED
                        + ","
                        + RawContactsTable.DISPLAY_NAME
                        + ","
                        + RawContactsTable.DISPLAY_NAME_ALT
                        + ","
                        + RawContactsTable.DISPLAY_NAME_SOURCE
                        + ","
                        + RawContactsTable.PHONETIC_NAME
                        + ","
                        + RawContactsTable.PHONETIC_NAME_STYLE
                        + ","
                        + RawContactsTable.SORT_KEY
                        + ","
                        + RawContactsTable.SORT_KEY_ALT
                        + ","
                        + RawContactsTable.NAME_VERIFIED
                        + ","
                        + RawContactsTable.SYNC1
                        + ","
                        + RawContactsTable.SYNC2
                        + ","
                        + RawContactsTable.SYNC3
                        + ","
                        + RawContactsTable.SYNC4
                        + ")"
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                db.execSQL(
                        insertSql,
                        new Object[] {
                                null,
                                insertContactId, // this is important.
                                rct.getSourceid(),
                                rct.getVersion(), 
                                rct.getDirty(),
                                rct.getDeleted(), 
                                rct.getAggregation_mode(),
                                rct.getCumstom_ringtone(),
                                rct.getSend_to_voicemail(),
                                rct.getTimes_contacted(),
                                rct.getLast_time_contacted(), 
                                rct.getStarred(),
                                rct.getDisplay_name(),
                                rct.getDisplay_name_alt(),
                                rct.getDisplay_name_source(),
                                rct.getPhonetic_name(),
                                rct.getPhonetic_name_style(),
                                rct.getSort_key(), 
                                rct.getSort_key_alt(),
                                rct.getName_verified(), 
                                rct.getSync1(),
                                rct.getSync2(), 
                                rct.getSync3(), 
                                rct.getSync4() });
                // get just inserted raw's id
                insertRawContactId = DBUtils.getLastInsertId(db);
            } catch (Exception e) {
                Log.i(TAG, "insertContact Exception :" + e.toString());
            } finally {
                db.close();
            }
        }
        // get just inserted id
        return insertRawContactId;
    }
    
    /**
     * get rawContactId by name
     * @param name the contact's name
     * @return rawContactId
     */
    public int getContactIdByRawContactId(int rawContactId){
        int contactId = -1;
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;
            try{
                String[] column = new String[]{RawContactsTable.CONTACT_ID};
                String selection = RawContactsTable._ID + "=?";
                String[] selectionArgs = new String[]{rawContactId+""};
                cursor = db.query(RawContactsTable.TABLE_NAME, column , selection, selectionArgs, null, null, null);
                if(cursor.moveToNext()){
                    contactId = cursor.getInt(0);
                }
            } catch(Exception e)
            {
                Log.i(TAG, "getRawContactIdByName :"+e.toString());
            } finally{
                cursor.close();
                db.close();
            }
        }
        return contactId;
    }

    /**
     * delete specific contact's all information.(System's contact info)
     * because of the Trigger, once delete contact's rawContacts table, 
     * all it's related raws in which --data table and contacts table -- will be deleted.
     * @param rawContactId
     */
    public void deleteAllInfo(int rawContactId) {
        // TODO Auto-generated method stub
        Uri url = RawContacts.CONTENT_URI;
        String where = "_id = ?";
        String[] selectionArgs = new String[]{rawContactId+""};
        try{
            mContext.getContentResolver().delete(url, where, selectionArgs);
        } catch (Exception e){
            Log.i(TAG, "deleteAllInfo :"+e.toString());
        }
    }
    
    /**
     * get total number of raw_contacts table.
     * @return
     * @throws SQLiteException
     */
    public int getAllContactsCount() throws SQLiteException{
        int total = 0;
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "select count(*) from " + RawContactsTable.TABLE_NAME;
            Cursor cursor = null;
            try{
                cursor = db.rawQuery(sql, null); 
                if(cursor.moveToNext()){
                    total = cursor.getInt(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                db.close();
                cursor.close();
            }
        }
        return total;
    }
    
    /**
     * get list of all contacts
     * @return
     * @throws SQLiteException
     */
    public List<RawContactsTable> getAllContacts() throws SQLiteException{
        List<RawContactsTable> list = new ArrayList<>();
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            RawContactsTable rct = null;
            Cursor cursor = null;
            try{
                cursor = db.query(RawContactsTable.TABLE_NAME, null, null, null, null, null, null);
                while(cursor.moveToNext()){
                    //can I new This object outside the while
                    rct = new RawContactsTable();
                    rct.set_id(cursor.getInt(cursor.getColumnIndex(RawContactsTable._ID)));
                    rct.setDisplay_name(cursor.getString(cursor.getColumnIndex(RawContactsTable.DISPLAY_NAME)));
                    rct.setContact_id(cursor.getInt(cursor.getColumnIndex(RawContactsTable.CONTACT_ID)));
                    
                    list.add(rct);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                db.close();
                cursor.close();
            }
        }
        return list;
    }
    
    
}
