package com.grandstream.drawstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.grandstream.drawstudy.widget.MyView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private MyView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        myView = findViewById(R.id.myview);


    }
}