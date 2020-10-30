package com.grandstream.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.text.TextUtils;
import android.util.Log;

import com.grandstream.myapplication.listener.DirectActionListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_DISCOVERY_STATE;
import static android.net.wifi.p2p.WifiP2pManager.EXTRA_P2P_DEVICE_LIST;
import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_P2P_GROUP;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED;

public class WifiDirectBroadCastReceiver extends BroadcastReceiver {

    private static String TAG = "xiangsun";
    private DirectActionListener mDirectActionListener;
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        intentFilter.addAction(WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        return intentFilter;
    }

    public WifiDirectBroadCastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel) {
        this.mWifiP2pManager = wifiP2pManager;
        this.mChannel = channel;
    }

    public void setDirectActionListener(DirectActionListener mDirectActionListener) {
        this.mDirectActionListener = mDirectActionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null) {
            return;
        }
        String action = intent.getAction();
        Log.d(TAG, "获取到" + action);
        if(TextUtils.isEmpty(action) || mDirectActionListener == null) {
            return;
        }
        switch(action) {
            // 用于指示 Wifi P2P 是否可用
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION: {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -100);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    mDirectActionListener.wifiP2pEnabled(true);
                } else {
                    mDirectActionListener.wifiP2pEnabled(false);
                    List<WifiP2pDevice> wifiP2pDeviceList = new LinkedList<>();
                    mDirectActionListener.onPeersAvailable(wifiP2pDeviceList);
                }
                break;
            }
            // 对等节点列表发生了变化
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION: {
                /*mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        mDirectActionListener.onPeersAvailable(peers.getDeviceList());
                    }
                });*/
                //获取到设备列表信息
                WifiP2pDeviceList mPeers = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                mDirectActionListener.onPeersAvailable(mPeers.getDeviceList());
                break;
            }
            // Wifi P2P 的连接状态发生了改变
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION: {

                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.isConnected()) {
                    mWifiP2pManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                        @Override
                        public void onConnectionInfoAvailable(WifiP2pInfo info) {
                            mDirectActionListener.onConnectionInfoAvailable(info);
                        }
                    });
                    Log.e(TAG, "已连接p2p设备");
                    mDirectActionListener.onConnected();
                } else {
                    Log.e(TAG, "与p2p设备已断开连接");
                    mDirectActionListener.onDisconnection();
                }
                break;
            }
            //本设备的设备信息发生了变化
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION: {
                WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                mDirectActionListener.onSelfDeviceAvailable(wifiP2pDevice);
                break;
            }
            case WIFI_P2P_DISCOVERY_CHANGED_ACTION: {
                int discoveryState = intent.getIntExtra(EXTRA_DISCOVERY_STATE, WIFI_P2P_DISCOVERY_STOPPED);
                mDirectActionListener.onDiscoverState(discoveryState);
            }
        }
    }
}
