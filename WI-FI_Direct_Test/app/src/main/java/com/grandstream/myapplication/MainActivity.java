package com.grandstream.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grandstream.myapplication.adapter.PeersListAdapter;
import com.grandstream.myapplication.listener.DirectActionListener;
import com.grandstream.myapplication.service.ClientService;
import com.grandstream.myapplication.service.ServerService;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.net.wifi.p2p.WifiP2pDevice.CONNECTED;
import static android.net.wifi.p2p.WifiP2pDevice.INVITED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED;

public class MainActivity extends AppCompatActivity {

    private String TAG = "xiangsun";
    private static final int CODE_REQ_PERMISSIONS = 665;
    private WifiP2pManager mManager;
    private WifiManager mWifiManager;
    private WifiP2pManager.Channel mChannel;
    private WifiDirectBroadCastReceiver mReceiver;
    private WifiP2pDnsSdServiceInfo mDnsSdServiceInfo;
    private WifiP2pUpnpServiceInfo mUpnpServiceInfo;
    private HashMap<String, String> mTxtMap;
    private DirectActionListener mDirectActionListener;
    private WifiP2pManager.DnsSdTxtRecordListener mTxtRecorListener;
    private WifiP2pManager.DnsSdServiceResponseListener mServiceResponserListener;
    private ConnectivityManager mConnectivityManager;

    private Button mCreateHotSpot;
    private Button mCreateGroupBtn;
    private Button mDiscoveryPeers;
    private Button mRemoveGroupBtn;
    private Button mGetConnectedListBtn;
    private RecyclerView mPeersList;
    private TextView mStateTV;
    private PeersListAdapter mAdapter;
    private TextView mMyDevice;

    private LinkedList<WifiP2pDevice> mDevices;
    private MyActionListener mActionListener;

    private static final String INSTANCE_NAME = "my_dnsSdService";
    private static final String SERVICE_TYPE = "_ipp._tcp";

    private boolean isService = true;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case Constants.MSG_WHAT_PRINT_LOG:
                    appendLog((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initObj();
        initView();
        initListener();
        if(isService) {
            appendLog("服务端添加服务");
        }
    }

    private void initObj() {
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBroadCastReceiver(mManager, mChannel);
        mDirectActionListener = new MyDirectActionListener();
        mTxtRecorListener = new MyDnsSdTxtRecordListener();
        mServiceResponserListener = new MyServiceResponserListener();
        mConnectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(isService) {
            mTxtMap = new HashMap<>();
            mTxtMap.put("my_name", "xiangsun");
            mTxtMap.put("my_company", "grandstream");
            mDnsSdServiceInfo = WifiP2pDnsSdServiceInfo.newInstance(INSTANCE_NAME, SERVICE_TYPE, mTxtMap);
            mManager.addLocalService(mChannel, mDnsSdServiceInfo, mActionListener);
            List<String> param = new LinkedList<>();
            param.add("name:xiangsun");
            param.add("company:grandstream");
            mUpnpServiceInfo = WifiP2pUpnpServiceInfo.newInstance("uuid", "device_server", param);
            mManager.addLocalService(mChannel, mUpnpServiceInfo, mActionListener);
        }

        mDevices = new LinkedList<>();
        WifiP2pDevice dev = new WifiP2pDevice();
        dev.deviceAddress ="123";
        dev.deviceName = "23423";
        dev.primaryDeviceType = "aaa";
        dev.secondaryDeviceType = "vvv";
        mDevices.add(dev);
        mAdapter = new PeersListAdapter(device -> {
            if(device.status == INVITED) {
                cancelInVited(device);
            } else if(device.status == CONNECTED) {
                showToast("当前设备已连接，无需连接");
            } else {
                connect(device);
            }
        }, mDevices);
        mAdapter.setData(mDevices);
        mActionListener = new MyActionListener();
    }

    private void initListener() {
        mCreateHotSpot.setOnClickListener(v -> {
            if(mWifiManager.isWifiEnabled()) {
                appendLog("打开wifi热点");
                createHotSpot();
            } else {
                appendLog("请先打开wifi");
                showToast("请先打开wifi");
            }
        });

        mCreateGroupBtn.setOnClickListener(v -> {
            if(mWifiManager.isWifiEnabled()) {
                appendLog("create group");
                createGroup();

            } else {
                appendLog("请先打开wifi");
                showToast("请先打开wifi");
            }
        });

        mRemoveGroupBtn.setOnClickListener(v -> {
           gameOver();
        });

        mDiscoveryPeers.setOnClickListener(v -> {
            if(mWifiManager.isWifiEnabled()) {
                appendLog("发现设备");
                if(isService) {
                    appendLog("服务搜索");
                    discoverServices();
                } else {
                    mManager.discoverPeers(mChannel, mActionListener);
                }
            } else {
                appendLog("请先打开wifi");
                showToast("请先打开wifi");
            }
        });

        mGetConnectedListBtn.setOnClickListener( v -> {
            appendLog("获取所有已连接设备");
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                appendLog("已连接设备：");
                Collection<WifiP2pDevice> devices = group.getClientList();
                int i=1;
                for(WifiP2pDevice d: devices) {
                    appendLog(
                            (i++) +
                                    ": ip:" +
                                    d.deviceAddress+
                                    ", name:" +
                            d.deviceName +
                                    ", isGroupOwner:" +
                                    d.isGroupOwner() );
                }
            }
        });
        });
        mManager.setDnsSdResponseListeners(mChannel, mServiceResponserListener, mTxtRecorListener);
        mManager.setUpnpServiceResponseListener(mChannel, mUpnpListener);
    }

    private void discoverServices() {
        WifiP2pDnsSdServiceRequest txtRequest = WifiP2pDnsSdServiceRequest.newInstance(INSTANCE_NAME, SERVICE_TYPE);
        WifiP2pDnsSdServiceRequest ptrRequest = WifiP2pDnsSdServiceRequest.newInstance(SERVICE_TYPE);
        WifiP2pUpnpServiceRequest upnpRequest = WifiP2pUpnpServiceRequest.newInstance("ssdp:all");
        mManager.addServiceRequest(mChannel, txtRequest, mActionListener);
        mManager.addServiceRequest(mChannel, ptrRequest, mActionListener);
        mManager.addServiceRequest(mChannel, upnpRequest, mActionListener);
        mManager.discoverServices(mChannel, mActionListener);
    }

    private void initView() {
        mCreateHotSpot = findViewById(R.id.create_wifi_hot_spot);
        mCreateGroupBtn = findViewById(R.id.create_group);
        mRemoveGroupBtn = findViewById(R.id.remove_group);
        mGetConnectedListBtn = findViewById(R.id.group_info);
        mDiscoveryPeers = findViewById(R.id.discovery_devices);
        mPeersList = findViewById(R.id.peers_list);
        mStateTV = findViewById(R.id.state);
        mPeersList.setLayoutManager(new LinearLayoutManager(this));
        mPeersList.setAdapter(mAdapter);
        mMyDevice = findViewById(R.id.my_device);
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, WifiDirectBroadCastReceiver.getIntentFilter());
        mHandler.postDelayed(() -> {
                mReceiver.setDirectActionListener(mDirectActionListener);
            }, 0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQ_PERMISSIONS);
    }

    private void connect(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mManager.connect(mChannel, config, mActionListener);
    }

    private void cancelInVited(WifiP2pDevice device) {
        appendLog("取消邀请，device:" + device);
        mManager.cancelConnect(mChannel, mActionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE_REQ_PERMISSIONS) {
            for(int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    appendLog("缺少权限，请先授予权限：" + permissions[i]);
                    return;
                }
            }
            appendLog("已获取所有权限");
        }
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void createHotSpot() {
        if(isTetheringStarted()) {
            startTethering();
        }else {
            startTethering();

        }
    }

    private boolean isTetheringStarted() {
        
        return false;
    }
    
    

    private void startTethering() {
    }

    private void stopTethering() {
    }

    private void createGroup() {
//        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
//        wifiP2pConfig. = "12345678";
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.wps.setup = WpsInfo.KEYPAD;

        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendLog("创建群组成功");
                Intent intent = new Intent(MainActivity.this, ServerService.class);
                ServerService.setHandler(mHandler);
                startService(intent);
            }

            @Override
            public void onFailure(int reason) {
                appendLog("创建群组失败");

            }
        });
    }

    private void gameOver() {
        appendLog("停止搜索");
        mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendLog("停止成功");
                mManager.removeGroup(mChannel, mActionListener);
            }

            @Override
            public void onFailure(int reason) {
                appendLog("停止失败，reason:" + reason);
            }
        });
        Intent intent = new Intent(this, ClientService.class);
        stopService(intent);
        intent = new Intent(this, ServerService.class);
        stopService(intent);
    }

    private void appendLog(String msg) {
        mStateTV.append(msg);
        mStateTV.append("\n");
    }

    private class MyActionListener implements WifiP2pManager.ActionListener {

        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess: create group");
            appendLog("操作成功");
            showToast("操作成功");
        }

        @Override
        public void onFailure(int reason) {
            Log.d(TAG, "onFailure: create group, reason:" + reason);
            appendLog("操作失败");
            showToast("操作失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameOver();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private class MyDirectActionListener implements DirectActionListener {
        @Override
        public void wifiP2pEnabled(boolean enabled) {
//                mStateTV.setText(enabled ? "ON" : "OFF");
            appendLog("成功打开wifi p2p");
        }

        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            Log.d(TAG, "onConnectionInfoAvailable");
            Log.d(TAG, "isGroupOwner：" + wifiP2pInfo.isGroupOwner);
            Log.d(TAG, "groupFormed：" + wifiP2pInfo.groupFormed);
            // InetAddress from WifiP2pInfo struct.
            if(wifiP2pInfo == null || wifiP2pInfo.groupOwnerAddress == null) {
                Log.d(TAG, "onConnectionInfoAvailable: wifiP2pInfo == null || wifiP2pInfo.groupOwnerAddress == null");
                return;
            }
            String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();
            Log.d(TAG, "onConnectionInfoAvailable: groupOwnerAddress:" + groupOwnerAddress);
            // After the group negotiation, we can determine the group owner
            // (server).
            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                // Do whatever tasks are specific to the group owner.
                // One common case is creating a group owner thread and accepting
                // incoming connections.
                mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                    @Override
                    public void onGroupInfoAvailable(WifiP2pGroup group) {
                        String groupPassword = group.getPassphrase();
                        mMyDevice.append("\n 群组密码：" + groupPassword);
                        Log.d(TAG, "onGroupInfoAvailable: grouppassword:" + groupPassword);
                    }
                });
            } else if (wifiP2pInfo.groupFormed) {
                // The other device acts as the peer (client). In this case,
                // you'll want to create a peer thread that connects
                // to the group owner.
                appendLog("打开客户端socket");
                Log.d(TAG, "onConnectionInfoAvailable: 打开客户端socket");
                Intent intent = new Intent(MainActivity.this, ClientService.class);
                ClientService.setServerAddress(wifiP2pInfo.groupOwnerAddress.getHostAddress());
                ClientService.setHandler(mHandler);
                startService(intent);
            }



        }

        @Override
        public void onDisconnection() {
            appendLog("disconnected");
        }

        @Override
        public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {
            Log.d(TAG, "onSelfDeviceAvailable: 本设备信息发生变化, wifiP2pDevice:" + wifiP2pDevice);
//                        appendLog("本设备信息发生变化, wifiP2pDevice:" + wifiP2pDevice);
            mMyDevice.setText(wifiP2pDevice.toString());
        }

        @Override
        public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
            Log.d(TAG, "onPeersAvailable: wifiP2pDeviceList size:" + wifiP2pDeviceList.size());
            appendLog("获取到"+wifiP2pDeviceList.size()+"个设备");
            mDevices.clear();
            mDevices.addAll(wifiP2pDeviceList);
            mAdapter.setData(mDevices);
        }

        @Override
        public void onDiscoverState(int discoveryState) {
            if(discoveryState == WIFI_P2P_DISCOVERY_STOPPED) {
                appendLog("发现设备完成");
            } else {
                appendLog("发现设备中...");
            }
        }

        @Override
        public void onChannelDisconnected() {
            showToast("onChannelDisconnected");
            mDevices.clear();
            mAdapter.setData(mDevices);
            appendLog("channel disconnect");
        }
    }

    private class MyServiceResponserListener implements WifiP2pManager.DnsSdServiceResponseListener {

        @Override
        public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
            Log.d(TAG, "onDnsSdServiceAvailable: instanceName:" + instanceName);
            Log.d(TAG, "onDnsSdServiceAvailable: registrationType:" + registrationType);
            Log.d(TAG, "onDnsSdServiceAvailable: srcDevice:" + srcDevice);
        }
    }

    private class MyDnsSdTxtRecordListener implements WifiP2pManager.DnsSdTxtRecordListener {

        @Override
        public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {
            Log.d(TAG, "onDnsSdServiceAvaila" +
                    "ble: fullDomainName:" + fullDomainName);
            for(String key:txtRecordMap.keySet()) {
                Log.d(TAG, "onDnsSdTxtRecordAvailable: key:" + key + ", value:" + txtRecordMap.get(key));
            }

            Log.d(TAG, "onDnesSdServiceAvailable: srcDevice:" + srcDevice);
        }
    }

    private WifiP2pManager.UpnpServiceResponseListener mUpnpListener = new  WifiP2pManager.UpnpServiceResponseListener() {

        @Override
        public void onUpnpServiceAvailable(List<String> uniqueServiceNames, WifiP2pDevice srcDevice) {
            Log.d(TAG, "onUpnpServiceAvailable: uniqueddServiceNames:" + uniqueServiceNames);
            Log.d(TAG, "onUpnpServiceAvailabld: srcDevice:" + srcDevice);
        }
    };
}
