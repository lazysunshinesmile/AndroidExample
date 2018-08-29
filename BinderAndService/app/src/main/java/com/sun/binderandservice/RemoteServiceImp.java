package com.sun.binderandservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class RemoteServiceImp extends Service {

    private final String TAG = "RemoteServiceImp sun";
    private ServiceCallback mCallbcack;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: start");
        startDownload();

        return mBinder;
    }

    private void startDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i < 101; i++) {
                    try {
                        Thread.sleep(1000);
                        if(mCallbcack != null)
                            mCallbcack.onDownloadProcess(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "run: download:"+i+"%");
                }

            }
        }).start();
    }

    private Binder mBinder = new MyBinder.Stub() {
        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public void registerCallback(ServiceCallback callback) throws RemoteException {
            Log.d(TAG, "registerCallback: ");
            mCallbcack = callback;
        }

        @Override
        public void unregisterCallback() throws RemoteException {
            mCallbcack = null;
        }

        @Override
        public void downloadOver() throws RemoteException {
            mCallbcack.onDownloadComplete();
        }
    };



}
