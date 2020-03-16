package com.grandstream.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.leakcanary.RefWatcher;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        RefWatcher refWatche = MyApplication.getRefWatcher(this);
        refWatche.watch(this);
        super.onCreate(savedInstanceState);

        TestLeak.getInstance(this);
    }
}
