package com.example.sx3861.performanceoptimization;

import android.app.Application;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        //实际调用的是下面的方法
        LeakCanary.install(this);

        //可以使用上传服务。将内存泄漏文件上传。
        LeakCanary.refWatcher(this).listenerServiceClass(UploadDumpService.class)
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .buildAndInstall();

    }
}
