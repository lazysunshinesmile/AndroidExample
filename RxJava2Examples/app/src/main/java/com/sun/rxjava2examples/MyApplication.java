package com.sun.rxjava2examples;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Author: sun
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
