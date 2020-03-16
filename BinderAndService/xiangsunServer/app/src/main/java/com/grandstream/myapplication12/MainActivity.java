package com.grandstream.myapplication12;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xiangsun.create.aidl.ServiceManagerXS;

public class MainActivity extends Activity {

    private TextView textView;

    private ServiceManagerXS serviceManagerXS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangsun);
        Intent intent = new Intent("com.xiangsun.BIND_SERVICE");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            boolean isOk = bindService(intent,BIND_AUTO_CREATE, null, null);
        Log.d("xiangsun", "onCreate: " + getPackageName());
        intent.setPackage(getPackageName());
        ComponentName name = startService(intent);
        Log.d("xiangsun", "onCreate: packagename:" + name.getPackageName() + ",classname:" + name.getClassName());
            Log.d("xiangsun", "onCreate: isOk:" + name);
//        }
    }

}
