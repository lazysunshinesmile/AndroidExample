package com.grandstream.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.grandstream.myapplication.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ServerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static Handler mHandler = null;

    HashMap<Socket, BufferedReader> clientsReader = new HashMap<>();
    HashMap<Socket, BufferedWriter> clientsWriter = new HashMap<>();
    List<Socket> clients = new LinkedList<>();


    public static void setHandler(Handler mHandler) {
        ServerService.mHandler = mHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("xiangsun_server_service", "onCreate: ServerService");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(Constants.SOCKET_PORT);
                    while (true) {
                        serverSocket.setReuseAddress(true);
                        Socket client = serverSocket.accept();
                        String clientIp = client.getInetAddress().getHostAddress();

                        for(Socket bro:clients) {
                            BufferedWriter bw = clientsWriter.get(bro);
                            sendMesg("多了一个客户端：" + clientIp, bw);
                        }
                        appendLog("多了一个客户端：" + clientIp);

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                        clients.add(client);
                        clientsReader.put(client, bufferedReader);
                        clientsWriter.put(client, bufferedWriter);

                        String line = bufferedReader.readLine();
                        appendLog("客户端发来消息：");
                        appendLog(line);

                        sendMesg("服务端收到了", bufferedWriter);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);

    }

    private void sendMesg(String msg, BufferedWriter bw) throws IOException {
        bw.write(msg);
        bw.newLine();
        bw.flush();
    }

    private void appendLog(String log) {
        if(mHandler != null) {
            Message msg = new Message();
            msg.what = Constants.MSG_WHAT_PRINT_LOG;
            msg.obj = log + "\n";
            mHandler.sendMessage(msg);
        }
    }
}
