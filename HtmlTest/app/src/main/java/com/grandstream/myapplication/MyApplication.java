package com.grandstream.myapplication;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApplication extends Application {

    public RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();


        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        mRefWatcher = LeakCanary.install(this);

    }

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication launcherApplication = (MyApplication) context.getApplicationContext();
        return launcherApplication.mRefWatcher;
    }
}
