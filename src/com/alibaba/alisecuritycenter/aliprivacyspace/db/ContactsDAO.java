package com.alibaba.alisecuritycenter.aliprivacyspace.db;


import com.alibaba.alisecuritycenter.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

public class ContactsDAO {
    private static final String TAG = "ContactsDAO";
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    public ContactsDAO(Context context) {
        mContext = context;
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * get system's contacts table's raw by contactId.
     * 
     * @param contactId
     * @return
     */
    public ContactsTable getSystemRawByContactId(int contactId) {
        ContactsTable ct = new ContactsTable();
        Uri contactsUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
                contactId);
        Cursor c = mContext.getContentResolver().query(contactsUri,null, null,null, null);
        try {
            while (c.moveToNext()) {
                if (!c.isNull(0)) {
                    // _ID is Primary key , auto increase.
                    ct.set_id(c.getInt(c.getColumnIndex(ContactsTable._ID)));
                    ct.setPhoto_id(c.getInt(c
                            .getColumnIndex(ContactsTable.PHOTO_ID)));
                    ct.setCumstom_ringtone(c.getString(c
                            .getColumnIndex(ContactsTable.CUMSTOM_RINGTONE)));
                    ct.setSend_to_voicemail(c.getInt(c
                            .getColumnIndex(ContactsTable.SEND_TO_VOICEMAIL)));
                    ct.setTimes_contacted(c.getInt(c
                            .getColumnIndex(ContactsTable.SEND_TO_VOICEMAIL)));
                    ct.setLast_time_contacted(c.getInt(c
                            .getColumnIndex(ContactsTable.LAST_TIME_CONTACTED)));
                    ct.setStarred(c.getInt(c
                            .getColumnIndex(ContactsTable.STARRED)));
                    ct.setHas_phone_number(c.getInt(c
                            .getColumnIndex(ContactsTable.HAS_PHONE_NUMBER)));
                    ct.setLookup(c.getString(c
                            .getColumnIndex(ContactsTable.LOOKUP)));
                    ct.setContact_last_updated_timestamp(c.getInt(c
                            .getColumnIndex(ContactsTable.CONTACT_LAST_UPDATED_TIMESTAMP)));
                }
            }
        } finally {
            c.close();
        }
        return ct;
    }

    /**
     * insert one raw into DataTable according to system's contactId
     * @param contactId system's contactId
     * @return the inserted raw's id . if error , return -1. 
     */
    public int insertContact(int contactId) throws SQLException {
        int insertContactId = -1;
        ContactsTable ct = getSystemRawByContactId(contactId);
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                // no need to insert contact's id , it's auto increase , but
                // should insert NULL instead.
                String insertSql = "insert into " + ContactsTable.TABLE_NAME
                        + "( " 
                        + ContactsTable._ID + ","
                        + ContactsTable.PHOTO_ID + ","
                        + ContactsTable.CUMSTOM_RINGTONE + ","
                        + ContactsTable.SEND_TO_VOICEMAIL + ","
                        + ContactsTable.TIMES_CONTACTED + ","
                        + ContactsTable.LAST_TIME_CONTACTED + ","
                        + ContactsTable.STARRED + ","
                        + ContactsTable.HAS_PHONE_NUMBER + ","
                        + ContactsTable.LOOKUP + "," 
                        + ContactsTable.CONTACT_LAST_UPDATED_TIMESTAMP + ")"
                        + "values (?,?,?,?,?,?,?,?,?,?);";
                db.execSQL(
                        insertSql,
                        new Object[] {
                                null, 
                                ct.getPhoto_id(),
                                ct.getCumstom_ringtone(),
                                ct.getSend_to_voicemail(),
                                ct.getTimes_contacted(),
                                ct.getLast_time_contacted(), 
                                ct.getStarred(),
                                ct.getHas_phone_number(), 
                                ct.getLookup(),
                                ct.getContact_last_updated_timestamp() });
                // get just inserted contact id.
                insertContactId = DBUtils.getLastInsertId(db);
            } catch (Exception e) {
                Log.i(TAG, "insertContact Exception :" + e.toString());
            } finally {
                db.close();
            }
        }
        return insertContactId;
    }

}
