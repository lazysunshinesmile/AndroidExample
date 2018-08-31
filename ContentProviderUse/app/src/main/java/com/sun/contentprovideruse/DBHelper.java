package com.sun.contentprovideruse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = "DBHelper";
    private static final String DB_NAME = "DB_test";
    private static final int DATABASE_VERSION = 2;


    public final static String tableName1 = "user";
    public final static String tableName2 = "user_action";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName1 + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName2 + "(id INTEGER PRIMARY KEY AUTOINCREMENT, user_action TEXT, time LONG)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: 更新");
    }

}
