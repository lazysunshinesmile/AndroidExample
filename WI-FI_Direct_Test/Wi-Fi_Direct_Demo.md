[TOC]

# Wi-Fi 直连分享

## 概述

[使用 WLAN 直连](https://www.wi-fi.org/discover-wi-fi/wi-fi-direct) (P2P) 技术，可以让具备相应硬件的 Android 4.0（API 级别 14）或更高版本设备在没有中间接入点的情况下，通过 WLAN 进行直接互联。使用这些 API，您可以实现支持 WLAN P2P 的设备间相互发现和连接，从而获得比蓝牙连接更远距离的高速连接通信效果。对于多人游戏或照片共享等需要在用户之间共享数据的应用而言，这一技术非常有用。

主要使用的api都是[`WifiP2pManager`](https://developer.android.google.cn/reference/android/net/wifi/p2p/WifiP2pManager?hl=zh-cn) 类提供。

## 普通连接，代码实现

### 初始化

```java
private WifiP2pManager.Channel mChannel; //app与framework联系的纽带
private WifiP2pManager mManager;
...
mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
mChannel = mManager.initialize(this, getMainLooper(), null);
```

### 创建群组

由服务端设备发起，客户端设备跳过这一步骤。不创建群组，也能进行下面的发现与连接。连接成功之后，这两台设备自动形成一个群组。两台设备会商议并随机确定一位为群主。*在创建群组之后，会有一个名称为`DIRECT_**_DeviceName`*的wifi，这个是为了没有wifi p2p功能的设备准备的。这些设备也可以通过连接wifi的方式加入组内。但这种加入方式不会有`WIFI_P2P_CONNECTION_CHANGED_ACTION`广播发送。但在[`mManager.requestGroupInfo`](#获取群组相关信息)的结果返回中是包含了这些设备的。

```java
mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
    @Override
    public void onSuccess() {
        appendLog("创建群组成功");
        //在这里就可以创建ServerSocket 并等待客户端的接入了。
        ...
    }

    @Override
    public void onFailure(int reason) {
        appendLog("创建群组失败");
        ...
    }
});
```

很多地方都会使用到`WifiP2pManager.ActionListener`回调，下面使用的都是`mActionListener`，是实现`WifiP2pManager.ActionListener`接口的全局变量。错误的原因有以下几个：

```java
/**
     * Passed with {@link ActionListener#onFailure}.
     * 内部原因导致操作失败。
     */
public static final int ERROR               = 0;

/**
     * Passed with {@link ActionListener#onFailure}.
     * 不支持P2P功能导致失败。
     */
public static final int P2P_UNSUPPORTED     = 1;

/**
     * Passed with {@link ActionListener#onFailure}.
     * Indicates that the operation failed because the framework is busy and
     * unable to service the request
     * 框架繁忙，不能处理该操作请求导致失败
     */
public static final int BUSY                = 2;

/**
     * Passed with {@link ActionListener#onFailure}.
     * Indicates that the {@link #discoverServices} failed because no service
     * requests are added. Use {@link #addServiceRequest} to add a service
     * request.
     * 针对 discoverServices 的错误玛，没有添加任何服务。
     */
public static final int NO_SERVICE_REQUESTS = 3;

```

### 发现设备

```java
mManager.discoverPeers(mChannel, mActionListener);
```

客户端直接发现设备，不创建群组。必须两台设备同时发现设备，才能查找到对方的存在。**

### 停止发现设备

```java
mManager.stopPeerDiscovery(mChannel, mActionListener);
```

### 连接设备

这一操作可以由服务端发起，也可以由客户端发起。这个device是发现到的对端设备，会在下文的广播中介绍。

```java
WifiP2pConfig config = new WifiP2pConfig();
config.deviceAddress = device.deviceAddress;
mManager.connect(mChannel, config, mActionListener);
```

### 取消连接

连接之后也可以取消连接。**注意：取消只能取消所有正在发起连接邀请的设备，不能针对某一个设备进行操作**

```java
mManager.cancelConnect(mChannel, mActionListener);
```

### 解散群组

连接之后，想要取消某个单一连接，只能从客户端取消对服务端的连接。服务端取消连接只能解散群组，也就取消了所有的连接。

```java
mManager.removeGroup(mChannel, mActionListener);
```

### 获取群组相关信息

获取到群组里面的所有信息。

```java
mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
    @Override
    public void onGroupInfoAvailable(WifiP2pGroup group) {
        appendLog("已连接的设备：");
        Collection<WifiP2pDevice> devices = group.getClientList();
        int i=1;
        for(WifiP2pDevice d: devices) {
            appendLog((i++) +
                      ": ip:" +
                      d.deviceAddress+
                      ", name:" +
                      d.deviceName +
                      ", isGroupOwner:" +
                      d.isGroupOwner() );
        }
    }
});
```

### 广播实现

自身设备、发现设备和连接设备的状态变化都是通过广播告知`app`层的。

| Intent                                | 说明                                                         |
| :------------------------------------ | :----------------------------------------------------------- |
| `WIFI_P2P_CONNECTION_CHANGED_ACTION`  | 当设备的 WLAN 连接状态更改时广播。                           |
| `WIFI_P2P_PEERS_CHANGED_ACTION`       | 当您调用 `discoverPeers()` 时广播。如果您在应用中处理此 Intent，则通常需要调用 `requestPeers()` 以获取对等设备的更新列表。 |
| `WIFI_P2P_STATE_CHANGED_ACTION`       | 当 WLAN P2P 在设备上启用或停用时广播。                       |
| `WIFI_P2P_THIS_DEVICE_CHANGED_ACTION` | 当设备的详细信息（例如设备名称）更改时广播。                 |
| `WIFI_P2P_DISCOVERY_CHANGED_ACTION`   | 当设备开始或者停止发现设备时广播                             |

```java
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    
    public WiFiDirectBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction;

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, WIFI_P2P_STATE_DISABLED);
            //wifi p2p 能不能使用取决于你的wifi是否打开。
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //wifi p2p 可以使用
            } else if (state == WIFI_P2P_STATE_DISABLED) {
                // wifi p2p 不可以使用
			}
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            //获取所有扫描到的设备。
            WifiP2pDeviceList mPeers = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            / // Respond to new connection or disconnections
            // wifi p2p 连接状态发生变化，在创建组成功也会进入到这个广播
                            
            //获取wifip2p网络状态
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            
            // 关于这个群组的连接信息。有三个属性：
            // 1. groupFormed 群组是否已经形成，作为群主，创建群组之后，获得的该属性即为true
            // 2. isGroupOwner 本设备是否为群主
            // 3. groupOwnerAddress 群主的ip地址。
            WifiP2pInfo p2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
            
            // 群组的相关信息。网络名称，密码，所有已连接的设备等。
            WifiP2pGroup p2pGroup = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
            
            
            if (networkInfo != null && networkInfo.isConnected()) {
                //表明连接上了,创建组成功也会进入到这个判断
                if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                	//group已经形成，且本设备是群主，
                    
                } else if (wifiP2pInfo.groupFormed) {
                	//group已经形成，但本设备不是群主
                    //获取群主IP
                    String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();
                    //建立Socket连接。
                }
                
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            //本设备信息发生变化
			WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
        } else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {
            //发现设备操作状态变化
            //WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED 停止状态
            //WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED 开始状态
            int discoveryState = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE,
                        WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED);
        }
    }
}
```

### 获取连接信息

```java
mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if(wifiP2pInfo == null || wifiP2pInfo.groupOwnerAddress == null) {
            return;
        }
        if(wifiP2pInfo.groupFormed) {
        	//获取群主的ip地址。
        	String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();
        	Log.d(TAG, "onConnectionInfoAvailable: groupOwnerAddress:" + groupOwnerAddress);
        }
        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a group owner thread and accepting
            // incoming connections.
           	//群已建立，且是群主，建立群成功后
            //在这里就可以获取群信息，获取到创建群组中提到的wifi密码。这样没有wifip2p功能的设备就可以通过连接热点进入群组当中。代码如下。
            mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    String groupPassword = group.getPassphrase();
                    Log.d(TAG, "onGroupInfoAvailable: wifi热点密码:" + groupPassword);
                }
            });
        } else if (wifiP2pInfo.groupFormed) {
            // The other device acts as the peer (client). In this case,
            // you'll want to create a peer thread that connects
            // to the group owner.
           	// 客户端会进入这个判断
        }
       	
    }
});
```

## 针对性wifi p2p连接

以上这种方式，是最为普通的方式，能搜索到就能连接成功。另一种搜索方式是`WifiP2pManager.discoverServices`。这种搜索方式依旧会搜索到所有正在搜索的设备，但在连接的时候会根据服务中提供的一些字段和协议去判断是否符合连接要求。

### 服务创建

1. Bonjour

    ```java
    Map<String, String> txtRecord = new HashMap<String, String>();
    txtRecord.put("txtvers", "1");
    txtRecord.put("pdl", "application/postscript");
    mServiceInfo = WifiP2pDnsSdServiceInfo.newInstance(instanceName,
                    serviceType, txtRecord);
    ```

    > serviceType：android文档提供了一些信息，可以从指南[Using Network Service Discovery](http://developer.android.com/training/connect-devices-wirelessly/nsd.html)开始。根据它，服务类型指定应用程序使用的协议和传输层。语法是`_<protocol>._<transportlayer>`。所以应该只在第二部分中使用现有的传输。当然，最常用的是`tcp`和`udp`。
    > 至于第一部分，有很多应用程序级协议。例如，`presence`是xmpp.org的消息传递协议，`ipp`是打印机服务。您可以在[dns-sd.org site](http://www.dns-sd.org/ServiceTypes.html)找到注册协议的大致完整列表。您还可以查看[the service names registry at iana.org](http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xhtml)。
    > 由于基于dns的服务发现是[Zero-configuration networking](http://en.wikipedia.org/wiki/Zero-configuration_networking)的一部分，它有许多实现，所以可以在特定的供应商站点上找到附加的协议列表。

    

2. UPnP

   ```java
   List<String> services = new ArrayList<String>();
   services.add("urn:schemas-upnp-org:service:AVTransport:1");
   services.add("urn:schemas-upnp-org:service:ConnectionManager:1");
   mServiceInfo = WifiP2pUpnpServiceInfo.newInstance(
       "6859dede-8574-59ab-9332-123456789011",
       "urn:schemas-upnp-org:device:MediaRenderer:1",
       services);
   ```
   
   | Parameters |                                                              |
   | :--------- | ------------------------------------------------------------ |
   | `uuid`     | `String`: a string representation of this UUID in the following format, as per [RFC 4122](https://www.ietf.org/rfc/rfc4122.txt). e.g) 6859dede-8574-59ab-9332-123456789012 |
   | `device`   | `String`: a string representation of this device in the following format, as per [UPnP Device Architecture1.1](http://www.upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.1.pdf) e.g) urn:schemas-upnp-org:device:MediaServer:1 |
   | `services` | `List`: a string representation of this service in the following format, as per [UPnP Device Architecture1.1](http://www.upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.1.pdf) e.g) urn:schemas-upnp-org:service:ContentDirectory:1 |

### 添加服务

```java
mManager.addLocalService(mChannel, mServiceInfo, mActionListener);
```

### 创建群组

[同上](#创建群组[)

### 创建服务监听

```java
//使用的时Bonjour服务
WifiP2pManager.DnsSdServiceResponseListener ptrListener = new WifiP2pManager.DnsSdServiceResponseListener() {
     @Override
     public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {

     }
 }

WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
    @Override
    public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {

    }
}
mManager.setDnsSdResponseListeners(mChannel, ptrListener, txtListener);

//使用UPnP服务
private WifiP2pManager.UpnpServiceResponseListener upnpListener = new  WifiP2pManager.UpnpServiceResponseListener() {

    @Override
    public void onUpnpServiceAvailable(List<String> uniqueServiceNames, WifiP2pDevice srcDevice) {

    }
}
mManager.setUpnpServiceResponseListener(mChannel, upnpListener);

```

这些方法会在搜索到设备时回调，至于回调哪个监听，是由于你在搜索时传入的服务request 时哪个。

### 发现服务

```java
//Bonjour类型的TXT服务，对应上面 txtListener
WifiP2pDnsSdServiceRequest txtRequest = WifiP2pDnsSdServiceRequest.newInstance(INSTANCE_NAME, SERVICE_TYPE);
//Bonjour类型的PTR服务，对应上面 ptrListener
WifiP2pDnsSdServiceRequest ptrRequest = WifiP2pDnsSdServiceRequest.newInstance(SERVICE_TYPE);
//UPnP服务，对应上面的upnpListener
WifiP2pUpnpServiceRequest upnpRequest = WifiP2pUpnpServiceRequest.newInstance(searchTarget);

mManager.addServiceRequest(mChannel, txtRequest, mActionListener);
mManager.addServiceRequest(mChannel, ptrRequest, mActionListener);
mManager.addServiceRequest(mChannel, upnpRequest, mActionListener);
```

### 其余步骤与上面相同

## 测试样图

1. 服务端

   ![image-20201112095806426](../../../../.config/joplin-desktop/resources/image-20201112095806426.png)

2. 客户端1，2，3

   ![image-20201112095838113](../../../../.config/joplin-desktop/resources/image-20201112095838113.png)

   ![image-20201112095857755](../../../../.config/joplin-desktop/resources/image-20201112095857755.png)

   ![image-20201112095920292](../../../../.config/joplin-desktop/resources/image-20201112095920292.png)

## 参考

- [android 原生代码](https://github.com/lazysunshinesmile/AndroidExample/tree/master/CtsVerifier)
- [android 说明文档](https://developer.android.google.cn/guide/topics/connectivity/wifip2p?hl=zh-cn)