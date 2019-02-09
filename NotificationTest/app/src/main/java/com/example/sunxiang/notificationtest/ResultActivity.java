package com.example.sunxiang.notificationtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by sunxiang on 2018/9/26.
 */

public class ResultActivity extends AppCompatActivity {

    private TextView mTextView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        mTextView.setText(content);
    }
}
