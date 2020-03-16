package com.grandstream.xiangsunclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.xiangsun.create.aidl.ServiceListener;
import com.xiangsun.create.aidl.ServiceManagerXS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "xiangsun123";
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_demo);
        Intent intent = new Intent("com.xiangsun.BIND_SERVICE");
        String packageName = "com.grandstream.myapplication";
        String serviceClassName = "com.grandstream.myapplication12.XSService";
        intent.setComponent(new ComponentName(packageName, serviceClassName));
        boolean isOk = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: isOk:" + isOk);
    }

    private Handler myHandler= new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            textView.setText(String.valueOf(msg.arg1));
        }
    };;

    private ServiceManagerXS serviceMangerXS;
    private ServiceListener serviceListener = new ServiceListener.Stub() {
        public void succeed(int num) {
            Log.d("xiangsun", "succeed: num:" + num);
            Message msg = new Message();
            msg.arg1 = num;
            myHandler.sendMessage(msg);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: connected");
            serviceMangerXS = ServiceManagerXS.Stub.asInterface(service);
            try {
                serviceMangerXS.registerListener(serviceListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            serviceMangerXS = null;
        }
    };
}
