package com.grandstream.myapplication12;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.xiangsun.create.aidl.ServiceListener;
import com.xiangsun.create.aidl.ServiceManagerXS;

public class XSService extends Service {
    private ServiceListener mListener;
    private final static String TAG = "xiangsun_456";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: be binded");
        Log.d(TAG, "onBind: compnent:" + getPackageName() + ",classname:" + getClass().getName());
        startDoSomething();
        return new ServiceManagerImpl();
    }

    private int num = 1;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                mListener.succeed(num);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            startDoSomething();
        }
    };
    private void startDoSomething() {
        myHandler.sendEmptyMessageDelayed(12, 1000);
        Log.d("xiangsun_456", "startDoSomething: num:" + num);
        num++;
    }
    public String getNameInter() {
        return "xiangsun_456";
    }


    public class ServiceManagerImpl extends ServiceManagerXS.Stub {
        @Override
        public String getName() {
            return getNameInter();
        }
        @Override
        public void stopService() {

        }
        @Override
        public void registerListener(ServiceListener listener) {
            mListener = listener;
        }
    }
}
