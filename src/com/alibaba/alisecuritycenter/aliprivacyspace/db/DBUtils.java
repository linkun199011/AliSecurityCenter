package com.alibaba.alisecuritycenter.aliprivacyspace.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBUtils {
    private final static String TAG = "DBUtils";
    
    /**
     * get Last Insert raw's Id through "SELECT LAST_INSERT_ROWID() newId"
     * @param db 
     * @return
     */
    public static int getLastInsertId(SQLiteDatabase db) throws SQLException {
        int lastInsertId = -1;
        String sql = "SELECT LAST_INSERT_ROWID() newId";
        Cursor c = null;
        try {
            c = db.rawQuery(sql, null);
            if (c.moveToNext()) {
                lastInsertId = c.getInt(0);
                Log.i(TAG, "getLastInsertId : " + lastInsertId);
            }
        } catch (Exception e) {
            Log.e(TAG, "getLastInsertId :" + e.toString());
        } finally {
            c.close();
        }
        return lastInsertId;
    }
}
