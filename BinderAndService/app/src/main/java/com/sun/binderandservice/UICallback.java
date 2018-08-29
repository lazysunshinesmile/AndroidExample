package com.sun.binderandservice;

public interface UICallback {
    void update(double process);
    void onDownloadCompelte();
    void setRemoteService(MyBinder remoteService);
}
