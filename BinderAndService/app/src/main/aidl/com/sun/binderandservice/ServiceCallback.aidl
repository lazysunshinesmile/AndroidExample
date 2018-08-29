// ServiceCallback.aidl
package com.sun.binderandservice;

// Declare any non-default types here with import statements
import com.sun.binderandservice.MyBinder;
interface ServiceCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onDownloadProcess(double process);
    void onDownloadComplete();
    void onDownloadError();
    void setRemoteService(MyBinder service);
}
