package com.sun.binderandservice;

import android.os.RemoteException;

public class ServiceCallbackImp extends ServiceCallback.Stub {


    private UICallback uiCallback;

    public ServiceCallbackImp(UICallback uiCallback) {
        this.uiCallback = uiCallback;
    }

    @Override
    public void onDownloadProcess(double process) throws RemoteException {
        uiCallback.update(process);
    }

    @Override
    public void onDownloadComplete() throws RemoteException {
        uiCallback.onDownloadCompelte();
    }

    @Override
    public void onDownloadError() throws RemoteException {

    }

    @Override
    public void setRemoteService(MyBinder service) throws RemoteException {
        uiCallback.setRemoteService(service);
    }

}
