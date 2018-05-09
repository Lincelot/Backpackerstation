package com.example.sfdoofah.backpackerstation;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Minsheng on 2015/12/14.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TAG = "SQLite";
    //資料庫名稱
    private static final String DATABASE_NAME = "homecare.db";
    //資料庫版本
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private Cursor cursor;

    //region Table_service
    public static final String Table_service = "service";
    public static final String Column_service_uid = "uid";
    public static final String Column_service_user = "user";
    public static final String Column_service_password = "password";
    public static final String Column_service_mac = "mac";
    public static final String Column_service_rssi = "rssi";
    public static final String Column_service_type = "type";
    public static final String Column_service_isdelete = "isdelete";
    //endregion

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    public void open() {
        if (!db.isOpen()) {
            db = this.getWritableDatabase();
        }
    }

    @Override
    public synchronized void close() {
        super.close();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");
        String Create_service = "CREATE TABLE IF NOT EXISTS " + Table_service + " ( " +
                Column_service_uid + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Column_service_user + " VARCHAR, " +
                Column_service_password + " VARCHAR, " +
                Column_service_mac + " VARCHAR, " +
                Column_service_rssi + " INTEGER, " +
                Column_service_type + " INTEGER, " +
                Column_service_isdelete + " TINYINT" +
                ");";

        db.execSQL(Create_service);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String Drop_service = "DROP TABLE IF EXISTS " + Table_service + ";";
        db.execSQL(Drop_service);
        onCreate(db);
    }

    public void execSQL(String sql) {
        db.beginTransaction();
        try {
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, "execSQL() called with: " + "sql = [" + sql + "]");
            Log.d(TAG, "execSQL() called with: " + "error = [" + ex.getMessage() + "]");
        } finally {
            db.endTransaction();
        }
    }

    public void execSQL(String sql, Object[] objects) {
        db.beginTransaction();
        try {
            db.execSQL(sql, objects);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, "execSQL() called with: " + "sql = [" + sql + "], objects = [" + Arrays.toString(objects) + "]");
            Log.d(TAG, "execSQL() called with: " + "error = [" + ex.getMessage() + "]");
        } finally {
            db.endTransaction();
        }
    }

    public void insertMulti(String tableName, List<String[]> data) {
        String sql = "INSERT INTO " + tableName + " VALUES";
        for (String[] strings : data) {
            sql += " ( ";
            for (String str : strings) {
                if (str == null) {
                    sql += "null,";
                } else {
                    sql += "'" + str + "',";
                }
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += " ),";
        }
        sql = sql.substring(0, sql.length() - 1);
        db.beginTransaction();
        try {
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, "insertMulti() called with: " + "sql = [" + sql + "]");
            Log.d(TAG, "insertMulti() called with: " + "error = [" + ex.getMessage() + "]");
        } finally {
            db.endTransaction();
        }
    }

    public Cursor rawQuery(String sql, String[] strings) {
        db.beginTransaction();
        try {
            cursor = db.rawQuery(sql, strings);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, "rawQuery() called with: " + "sql = [" + sql + "], objects = [" + Arrays.toString(strings) + "]");
            Log.d(TAG, "rawQuery() called with: " + "error = [" + ex.getMessage() + "]");
        } finally {
            db.endTransaction();
        }
        return cursor;
    }

    public void getAll(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        rawQuery(sql, null);
        Log.d(TAG, "getAll: " + DatabaseUtils.dumpCursorToString(cursor));
    }

    public void clearTable(String tableName) {
        String sql = "DELETE FROM " + tableName;
        execSQL(sql);
    }

}
