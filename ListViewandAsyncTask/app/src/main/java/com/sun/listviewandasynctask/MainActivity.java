package com.sun.listviewandasynctask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.sun.listviewandasynctask.listview.shortcomingsolve1.adapter.MyListAdapter;

import static com.sun.listviewandasynctask.listview.shortcoming.entity.Images.imageUrls;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity xiang";
    private ListView mListView;
    private MyListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.listview);
        int checkRet = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (checkRet != PackageManager.PERMISSION_GRANTED) {
            //是否应该展示请求权限的解释。
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (!shouldShow) {
                //不应该展示，用户已经按了never ask again.
                Log.d(TAG, "onClick: have pressed never ask agian");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET}, 10);
            } else {
                // 用户没有按never ask again.
                Log.d(TAG, "onClick: 没有按，do not press never ask again");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET}, 10);
            }
        }else {
            Log.d(TAG, "onCreate: " + checkRet);
        }

//        1、展示ListView的缺陷，滑动较快时，会导致有图片会不停的变动，这个是因为imageview会复用，
// 当某个imageview不显示的时候，会复用这个imageview，这样就会导致一个imageview 绑定了多个下载线程，
// 下载成功后，都会去刷新界面，导致不停的变化
//        mAdapter = new MyListAdapter(this, 0, Images.imageUrls);
//        mListView.setAdapter(mAdapter);


//        注意修改导包

//        解决办法1：给imageview加上特定的Tag，对应每一个url.
        mAdapter = new MyListAdapter(this, 0, imageUrls);
        mListView.setAdapter(mAdapter);


    }
}
