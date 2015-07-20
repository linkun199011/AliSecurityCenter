package com.alibaba.alisecuritycenter.aliprivacyspace.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data;
import android.util.Log;

public class DataDAO {
    private static final String TAG = "DataDAO";
    private Context mContext;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DataDAO(Context context){
        mContext = context;
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }
    
    
    /**
     * add phone number into PS' Data table.
     * @param phone
     * @param sysRawContactId
     */
    public void insertPhoneIntoPS(String phone, int sysRawContactId , int psRawCotnactId)
            {
        List<DataTable> list = new ArrayList<>();
        DataTable dt = null;
        boolean found = false;
        list = getSystemDataRowsByRawContactId(sysRawContactId);
        Iterator<DataTable> it = list.iterator();
        while (it.hasNext()) {
            dt = it.next();
            if (dt.getData1() == null) {
                continue;
            }
            if (dt.getData1().equals(phone)) {
                found = true;
                break;
            }
        }
        if(found){
            // change the System's rawContactId into PS' rawContactId
            dt.setRaw_contact_id(psRawCotnactId);
            insertData(dt);
        }
        return;
    }
    
    /**
     * delete phone number from System's Data table.
     * @param phone
     * @param sysRawContactId
     */
    public void deletePhoneFromSys (String phone, int sysRawContactId) throws SQLException{
        Uri uri = android.provider.ContactsContract.Data.CONTENT_URI;
        List<DataTable> list = getSystemDataRowsByRawContactId(sysRawContactId);
        boolean found = false;
        DataTable dt = null;
        Iterator<DataTable> it = list.iterator();
        while(it.hasNext()){
            dt = it.next();
            if(dt.getData1() == null){
                continue;
            }
            if(dt.getData1().equals(phone)){
                found = true;
                break;
            }
        }
        if(found == true){
            String where = "_id = ?";
            String[] selectionArgs = new String[]{dt.get_id()+""};
            try{
                mContext.getContentResolver().delete(uri, where, selectionArgs);
            } catch (Exception e){
                Log.i(TAG, "deletePhoneFromSys : "+e.toString());
            }
        }
        else
            return;
        
    }
    
    /**
     * get rawContactId by name
     * @param name the contact's name
     * @return rawContactId
     */
    public int getRawContactIdByName(String name) throws SQLException{
        int rawContactId = -1;
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;
            try{
                String[] column = new String[]{DataTable.RAW_CONTACT_ID};
                String selection = DataTable.DATA1 + "=?";
                String[] selectionArgs = new String[]{name};
                cursor = db.query(DataTable.TABLE_NAME, column , selection, selectionArgs, null, null, null);
                if(cursor.moveToNext()){
                    rawContactId = cursor.getInt(0);
                }
            } catch(Exception e)
            {
                Log.i(TAG, "getRawContactIdByName :"+e.toString());
            } finally{
                cursor.close();
                db.close();
            }
        }
        return rawContactId;
    }
    
    public int getIdByPhone(){
        return 0;
    }
    
    /**
     * get system's raws from data table by raw_contact_id.
     * @param rawContactId
     * @return return the <b>List</b> of DataTable which contains the specific rawContactId.
     */
    public List<DataTable> getSystemDataRowsByRawContactId(int rawContactId) throws SQLException{
        List <DataTable> list = new ArrayList<>();
        Uri rawContactsUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
        Uri dataUri = Uri.withAppendedPath(rawContactsUri,Data.CONTENT_DIRECTORY);
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(
                    dataUri,
                    new String[] {
                            DataTable._ID,
                            DataTable.MIMETYPE,
                            DataTable.RAW_CONTACT_ID,
                            DataTable.IS_PRIMARY,
                            DataTable.IS_SUPER_PRIMARY,
                            DataTable.DATA_VERSION,
                            DataTable.DATA1,
                            DataTable.DATA2,
                            DataTable.DATA3,
                            DataTable.DATA4,
                            DataTable.DATA5,
                            DataTable.DATA6,
                            DataTable.DATA7,
                            DataTable.DATA8,
                            DataTable.DATA9,
                            DataTable.DATA10,
                            DataTable.DATA11,
                            DataTable.DATA12,
                            DataTable.DATA13,
                            DataTable.DATA14,
                            DataTable.DATA15,
                            DataTable.DATA_SYNC1,
                            DataTable.DATA_SYNC2,
                            DataTable.DATA_SYNC3,
                            DataTable.DATA_SYNC4
                            },
                    null, null, null);
            while (c.moveToNext()) {
                if (!c.isNull(0)) {
                    DataTable dataTable = new DataTable();
                    dataTable.set_id(c.getInt(c.getColumnIndex(DataTable._ID)));
                    dataTable.setMimetype(c.getString(c.getColumnIndex(DataTable.MIMETYPE)));
                    dataTable.setIs_primary(c.getInt(c.getColumnIndex(DataTable.IS_PRIMARY)));
                    dataTable.setIs_super_primary(c.getInt(c.getColumnIndex(DataTable.IS_SUPER_PRIMARY)));
                    dataTable.setData_version(c.getInt(c.getColumnIndex(DataTable.DATA_VERSION)));
                    dataTable.setData1(c.getString(c.getColumnIndex(DataTable.DATA1)));
                    dataTable.setData2(c.getString(c.getColumnIndex(DataTable.DATA2)));
                    dataTable.setData3(c.getString(c.getColumnIndex(DataTable.DATA3)));
                    dataTable.setData4(c.getString(c.getColumnIndex(DataTable.DATA4)));
                    dataTable.setData5(c.getString(c.getColumnIndex(DataTable.DATA5)));
                    dataTable.setData6(c.getString(c.getColumnIndex(DataTable.DATA6)));
                    dataTable.setData7(c.getString(c.getColumnIndex(DataTable.DATA7)));
                    dataTable.setData8(c.getString(c.getColumnIndex(DataTable.DATA8)));
                    dataTable.setData9(c.getString(c.getColumnIndex(DataTable.DATA9)));
                    dataTable.setData10(c.getString(c.getColumnIndex(DataTable.DATA10)));
                    dataTable.setData11(c.getString(c.getColumnIndex(DataTable.DATA11)));
                    dataTable.setData12(c.getString(c.getColumnIndex(DataTable.DATA12)));
                    dataTable.setData13(c.getString(c.getColumnIndex(DataTable.DATA13)));
                    dataTable.setData14(c.getString(c.getColumnIndex(DataTable.DATA14)));
                    dataTable.setData15(c.getBlob(c.getColumnIndex(DataTable.DATA15)));
                    dataTable.setData_sync1(c.getString(c.getColumnIndex(DataTable.DATA_SYNC1)));
                    dataTable.setData_sync2(c.getString(c.getColumnIndex(DataTable.DATA_SYNC2)));
                    dataTable.setData_sync3(c.getString(c.getColumnIndex(DataTable.DATA_SYNC3)));
                    dataTable.setData_sync4(c.getString(c.getColumnIndex(DataTable.DATA_SYNC4)));
                    
                    /*Log.d("for test >>>>>>", "raw_contact_id :" + rawContactId 
                            +"_ID : " + dataTable.get_id() + "\n"
                            + " mimeType :"+ dataTable.getMimetype() + "\n"
                            + " raw_contact_id :"+ dataTable.getRaw_contact_id() + "\n"
                            + " is_primary :"+ dataTable.getIs_primary() + "\n"
                            + " is_super_primary :"+ dataTable.getIs_super_primary() + "\n"
                            + " data1 :"+ dataTable.getData1() + "\n"
                            + " data2 :"+ dataTable.getData2() + "\n"
                            + " data3 :"+ dataTable.getData3() + "\n"
                            + " data4 :"+ dataTable.getData4() + "\n"
                            + " data5 :"+ dataTable.getData5() + "\n"
                            + " data6 :"+ dataTable.getData6() + "\n"
                            + " data7 :"+ dataTable.getData7() + "\n"
                            + " data8 :"+ dataTable.getData8() + "\n"
                            + " data9 :"+ dataTable.getData9() + "\n"
                            + " data10 :"+ dataTable.getData10() + "\n"
                            + " data11 :"+ dataTable.getData11() + "\n"
                            + " data12 :"+ dataTable.getData12() + "\n"
                            + " data13 :"+ dataTable.getData13() + "\n"
                            + " data14 :"+ dataTable.getData14() + "\n"
                            + " data15 :" + dataTable.getData15());*/
                    
                    list.add(dataTable);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list;
    }
    /**
     * Look up the PrivasySpace's db , to check if the database contains this specific contact's name
     * @param name
     * @return return the object of DataTable.
     */
    public DataTable findPSContactByName(String name) throws SQLException{
        DataTable dt = new DataTable();
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.query(DataTable.TABLE_NAME, new String[] {
                        DataTable._ID, DataTable.RAW_CONTACT_ID,
                        DataTable.DATA1 }, DataTable.DATA1 + "=?",
                        new String[] { name }, null, null, null);
                if (cursor.moveToNext()) {
                    dt.set_id(cursor.getInt(cursor
                            .getColumnIndex(DataTable._ID)));
                    dt.setRaw_contact_id(cursor.getInt(cursor
                            .getColumnIndex(DataTable.RAW_CONTACT_ID)));
                    dt.setData1(cursor.getString(cursor
                            .getColumnIndex(DataTable.DATA1)));
                    Log.i(TAG, "data_table's name : "+dt.getData1());
                }
                else{
                    dt = null;
                    Log.i(TAG, "data_table's name is NULL ");
                }
            } catch (Exception e) {
                Log.i(TAG, "find PS contact By Name :" + e.toString());
            } finally {
                cursor.close();
                db.close();
            }
        }
        return dt;
    }
    
    /**
     * insert into PrivacySapce's Data table
     * @param value if value == null , insert all raws.
     * @param sysRawContactId system's rawContactId
     * @param psRawContactId PrivacySpace's rawContactId
     */
    public int insertData(String value , int sysRawContactId , int psRawContactId) throws SQLException{
        int insertDataId = -1;
        List<DataTable> list = getSystemDataRowsByRawContactId(sysRawContactId);
        Iterator<DataTable> it = list.iterator();
        DataTable dt = null;
        while(it.hasNext()){
            dt = it.next();
            // check name or phone or email etc.
            if(dt.getData1() == null){
                continue;
            }
            if (dt.getData1().equals(value) || value == null){
                dt.setRaw_contact_id(psRawContactId);
                try{
                    insertDataId = insertData(dt);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return insertDataId;
    }
    
    /**
     * insert object of DataTable into Data table in PS.
     * @param dt object of DataTable
     * @return
     */
    public int insertData(DataTable dt) throws SQLException{
        int insertDataId = -1;
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try{
                String insertSql = "insert into " + DataTable.TABLE_NAME
                        + "("
                        + DataTable._ID + ","
                        + DataTable.MIMETYPE + ","
                        + DataTable.RAW_CONTACT_ID + ","
                        + DataTable.IS_PRIMARY + ","
                        + DataTable.IS_SUPER_PRIMARY + ","
                        + DataTable.DATA_VERSION + ","
                        + DataTable.DATA1 + ","
                        + DataTable.DATA2 + ","
                        + DataTable.DATA3 + ","
                        + DataTable.DATA4 + ","
                        + DataTable.DATA5 + ","
                        + DataTable.DATA6 + ","
                        + DataTable.DATA7 + ","
                        + DataTable.DATA8 + ","
                        + DataTable.DATA9 + ","
                        + DataTable.DATA10 + ","
                        + DataTable.DATA11 + ","
                        + DataTable.DATA12 + ","
                        + DataTable.DATA13 + ","
                        + DataTable.DATA14 + ","
                        + DataTable.DATA15 + ","
                        + DataTable.DATA_SYNC1 + ","
                        + DataTable.DATA_SYNC2 + ","
                        + DataTable.DATA_SYNC3 + ","
                        + DataTable.DATA_SYNC4 + ")"
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                db.execSQL(
                        insertSql, 
                        new Object[]{
                                null,
                                dt.getMimetype(),
                                dt.getRaw_contact_id(),
                                dt.getIs_primary(),
                                dt.getIs_super_primary(),
                                dt.getData_version(),
                                dt.getData1(),
                                dt.getData2(),
                                dt.getData3(),
                                dt.getData4(),
                                dt.getData5(),
                                dt.getData6(),
                                dt.getData7(),
                                dt.getData8(),
                                dt.getData9(),
                                dt.getData10(),
                                dt.getData11(),
                                dt.getData12(),
                                dt.getData13(),
                                dt.getData14(),
                                dt.getData15(),
                                dt.getData_sync1(),
                                dt.getData_sync2(),
                                dt.getData_sync3(),
                                dt.getData_sync4() });
                // get just inserted data id
                insertDataId = DBUtils.getLastInsertId(db);
            } catch(Exception e){
                Log.i(TAG, "insertData : " + e.toString());
            } finally{
                db.close();
            }
            
        }
        return insertDataId;
    }
    
    /**
     * To check if the specific contact has one or more phone number.
     * @return return true if the contact has more than one phone number.
     */
    public boolean hasMorePhoneNumber(int rawContactId) {
        int phoneCount = 0;
        List<DataTable> list = getSystemDataRowsByRawContactId(rawContactId);
        Iterator<DataTable> it = list.iterator();
        while(it.hasNext()){
            DataTable dt = it.next();
            if(dt.getMimetype() == null){
                continue;
            }
            if(dt.getMimetype().equals("vnd.android.cursor.item/phone_v2")){
                phoneCount++;
            }
        }
        Log.i(TAG, "phoneCount = " + phoneCount);
        if (phoneCount > 1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * insert contact's other information into PrivacySpace's data table , except contact's name raw.
     * because contact's name had been added earlier.
     * @param sysRawContactId
     * @param rawContactId
     */
    public void insertOtherInfoIntoPS(int sysRawContactId , int rawContactId) {
        // TODO Auto-generated method stub
        List<DataTable> list = getSystemDataRowsByRawContactId(sysRawContactId);
        Iterator<DataTable> it = list.iterator();
        DataTable dt = null;
        while(it.hasNext()){
            dt = it.next();
            dt.setRaw_contact_id(rawContactId);
            if(dt.getMimetype() == null){
                continue;
            }
            if(dt.getMimetype().equals("vnd.android.cursor.item/name")){
                continue;
            }
            insertData(dt);
        }
    }
}
