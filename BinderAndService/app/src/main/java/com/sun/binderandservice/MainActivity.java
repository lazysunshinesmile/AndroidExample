package com.sun.binderandservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity sun";

    private Button bindService;
    private Button unBindService;
    private Button killService;
    private Context mContext = this;

    private TextView textView;
    private ServiceConnection serviceConnection;
    private MyBinder mMyBinder;
    private ServiceCallbackImp serviceCallbackImp;

    private UICallback uiCallback = new UICallback() {
        @Override
        public void update(final double process) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("已下载："+process +"%" );
                }
            });
        }

        @Override
        public void onDownloadCompelte() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("下载完成");
                }
            });
        }

        @Override
        public void setRemoteService(MyBinder remoteService) {
            mMyBinder = remoteService;
        }



    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService = findViewById(R.id.btn);
        unBindService = findViewById(R.id.unbindservice);
        killService = findViewById(R.id.killservice);
        textView = findViewById(R.id.process);

        serviceCallbackImp = new ServiceCallbackImp(uiCallback);
        serviceConnection = new ServiceConnectionImp(serviceCallbackImp);

        bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RemoteServiceImp.class);
                intent.setAction(MyBinder.class.getName());
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        unBindService.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
            }
        });

        killService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Process.killProcess(mMyBinder.getPid());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
