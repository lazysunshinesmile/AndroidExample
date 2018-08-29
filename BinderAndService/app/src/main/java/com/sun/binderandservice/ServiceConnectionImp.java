package com.sun.binderandservice;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ServiceConnectionImp implements ServiceConnection {
    private final String TAG = "ServiceConnectionImp sun";
    private MyBinder mMyBinder;
    private ServiceCallback serviceCallback;

    public ServiceConnectionImp(ServiceCallback serviceCallback) {
        this.serviceCallback = serviceCallback;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected: ");
        mMyBinder = MyBinder.Stub.asInterface(service);
        try {
            mMyBinder.registerCallback(serviceCallback);
            serviceCallback.setRemoteService(mMyBinder);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected: ");
    }

    @Override
    public void onBindingDied(ComponentName name) {

    }

    @Override
    public void onNullBinding(ComponentName name) {

    }
}
