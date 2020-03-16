package com.xiangsun.create.aidl;
import com.xiangsun.create.aidl.ServiceListener;
interface ServiceManagerXS {
    String getName();
    void stopService();
    void registerListener(ServiceListener listener);
}