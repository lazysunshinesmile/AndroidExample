package com.sun.contentprovideruse;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity xiang";

    private TextView textView;
    private Button insert, delete, update, query;

    private ContentResolver mContentResolver;
    private String userUri = "content://com.sun.contentprovider.use/user";
    private String jobUri = "content://com.sun.contentprovider.use/job";

    private ContentObserver mUserObserver;
    private ContentObserver mJobObserver;

    private Handler mHandler = new UIHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        insert = findViewById(R.id.insert);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        query = findViewById(R.id.query);
        initListner();

        mContentResolver = getContentResolver();

        //内存泄漏的可能。匿名内部类
        mUserObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                Log.d(TAG, "onChange: 数据库发生了改动，uri=" + uri +" threadName : " + Thread.currentThread().getName());
                updateUI("observer onchange 更改成功");

            }
        };
//        这边的false,初步理解为只有当你指定的这个uri，变化了才会通知。如果为true，那么包URI即其下面的所有变化都会被通知。
        mContentResolver.registerContentObserver(Uri.parse(userUri), false, mUserObserver);
    }

    private void initListner() {
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(userUri);
                ContentValues contentValues = new ContentValues();
                String name = Calendar.getInstance().getTimeInMillis() + "";
                contentValues.put("name", name);
                contentValues.put("age", 12);
                mContentResolver.insert(Uri.parse(userUri), contentValues);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mContentResolver.delete(Uri.parse(userUri), "age=?", new String[]{"12"});
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", "sunxiang");
                mContentResolver.update(Uri.parse(userUri), contentValues, "age=?", new String[]{"12"} );
            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//             public final @Nullable Cursor query(@RequiresPermission.Read @NonNull Uri uri,
//            @Nullable String[] projection, @Nullable String selection,
//            @Nullable String[] selectionArgs, @Nullable String sortOrder)
                Cursor res = mContentResolver.query(Uri.parse(userUri),null, null, null, null );
                String resToShow = "";
                if(res != null) {
                    while (res.moveToNext()) {
                        resToShow += "name = " + res.getString(1) + ", age = " + res.getInt(2) + "\n";
                    }
                }
                Log.d(TAG, "onClick: " + resToShow);
                updateUI(resToShow);

            }
        });

    }

    private void updateUI(String text) {
        textView.setText(text);
    }



    private static class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: msg: " + msg + " threadName : " + Thread.currentThread().getName());
        }
    }
}
