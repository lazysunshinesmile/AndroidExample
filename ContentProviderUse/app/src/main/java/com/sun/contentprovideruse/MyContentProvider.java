package com.sun.contentprovideruse;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sun.contentprovideruse.DB.DBHelper;

import java.lang.ref.WeakReference;

public class MyContentProvider extends ContentProvider {
    private final static String TAG = "MyContentProvider xiang";

    private WeakReference<Context> mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    private UriMatcher mUriMatcher;
    private String AUTHORITY = "com.sun.contentprovider.use";
    private final static int USER_CODE = 1;
    private final static int JOB_CODE = 2;

    @Override
    public boolean onCreate() {
        //下面两行在getTableName中有用，对应着看
        Log.d(TAG, "onCreate: ");
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "user", USER_CODE);
        mUriMatcher.addURI(AUTHORITY, "job", JOB_CODE);
        mContext = new WeakReference<>(getContext());
        mDBHelper = new DBHelper(mContext.get());
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        public Cursor query(String table, String[] columns, String selection,
//                String[] selectionArgs, String groupBy, String having,
//                String orderBy) {
        String tableName = getTableName(uri);
        return mSQLiteDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //TODO
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        long rowid = mSQLiteDatabase.insert(tableName, null, values);
        if(rowid != -1) {
            mContext.get().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
//        "age=? and name=?", new String[]{"12", "sunxiang"}
        int affectRows = mSQLiteDatabase.delete(tableName, selection, selectionArgs);
        mContext.get().getContentResolver().notifyChange(uri, null);
        return affectRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int affectRows = mSQLiteDatabase.update(tableName, values, selection, selectionArgs);
        mContext.get().getContentResolver().notifyChange(uri, null);
        return affectRows;
    }

    private String getTableName(Uri uri) {
        int code = mUriMatcher.match(uri);
        Log.d(TAG, "getTableName: code:" + code);
        switch (code){
            case USER_CODE:
                return DBHelper.TABLE_NAME_USER;
            case JOB_CODE:
                return DBHelper.TABLE_NAME_JOB;
        }
        return null;
    }
}
