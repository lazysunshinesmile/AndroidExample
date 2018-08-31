package com.sun.contentprovideruse.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private final static String TAG = "DBHelper xiang";
    private static final String DATABASE_NAME = "SX_Test";
    public static final String TABLE_NAME_USER = "user";
    public static final String TABLE_NAME_JOB = "job";
    private static final int DB_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_USER + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_JOB + "(id INTEGER PRIMARY KEY AUTOINCREMENT, job_name TEXT)");
        db.execSQL("INSERT INTO " + TABLE_NAME_USER + " VALUES(1, \"sunxiang\", 27)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
