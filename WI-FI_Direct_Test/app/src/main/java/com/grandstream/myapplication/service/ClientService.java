package com.grandstream.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.grandstream.myapplication.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ClientService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static Handler mHandler = null;
    private static String mServerAddress ;
    public static void setHandler(Handler mHandler) {
        ClientService.mHandler = mHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            sendMsg(Constants.SOCKET_PORT);
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean sendMsg(int port) {
        try {
            Socket socket = new Socket();
            socket.bind(null);
            socket.connect(new InetSocketAddress(mServerAddress, port));

            appendLog("sendMsg:mServerAddress:" + mServerAddress + " port:" + port);
            appendLog("client sendMsg:mServerAddress:" + mServerAddress + " port:" + port);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            appendLog("客户端发送：ip:" + mServerAddress + ", port:" + port);
            String log = "我的ip是：" + socket.getLocalAddress().getHostAddress() + ", 我连接的端口是:" + port;
            appendLog(log);
            bufferedWriter.write(log);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = "";
            while (!(line = bufferedReader.readLine()).equals("over")) {
                appendLog("服务端回复：" + line);
            }
            return true;

        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void appendLog(String log) {
        if(mHandler != null) {
            Message msg = new Message();
            msg.what = Constants.MSG_WHAT_PRINT_LOG;
            msg.obj = log + "\n";
            mHandler.sendMessage(msg);
        }
    }

    public static void setServerAddress(String hostAddress) {
        mServerAddress = hostAddress;
    }

}
