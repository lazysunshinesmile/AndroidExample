package com.example.sx3861.viewstubtest;

import android.app.ActivityManager;
import android.os.Debug;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private ViewStub viewStub;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取trace文件以供分析
//        Debug.startMethodTracing();
        setContentView(R.layout.activity_main);
        viewStub = findViewById(R.id.viewstub);
        button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewStub.inflate();
                printLog();
                getMemInfo();
                getCurrentProcessMemInfo();
                getAllProcessMemInfo();
                newThreadNullPoint();
            }
        });
//        Debug.stopMethodTracing();

        //拦截所有未截获的异常。与所在线程无关。
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                File file = new File(Environment.getExternalStorageDirectory(), "/sunxiang.hprof");

                try {
                    file.createNewFile();
                    //这边未申请写权限，所以文件未被写进去。
                    Log.d(TAG, "uncaughtException: filepath:" + file.getAbsolutePath());
                    Debug.dumpHprofData(file.getAbsolutePath());
                    Log.e(TAG, "uncaughtException: " + e.getStackTrace());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


    }

    private void printLog() {
        Log.d(TAG, "printLog: printLog");
    }

    private void getMemInfo() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        String memInfo = "getMemInfo: totalMem: " + memoryInfo.totalMem + " lowMem: " + memoryInfo.lowMemory + " availMem " + memoryInfo.availMem
                + " decribeContent: " + memoryInfo.describeContents();
        Log.d(TAG, memInfo);
    }

    private void getCurrentProcessMemInfo() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
//        PSS参数就是进程占用的私有内存加上平分的共享内存
        String memInfo = "getCurrentProcessMemInfo: Pss Total: " + memoryInfo.getTotalPss()
                + " Dalvik pss: " + memoryInfo.dalvikPss
                + " native pss" + memoryInfo.nativePss;
        Log.d(TAG, "getCurrentProcessMemInfo: " + memInfo);
    }

    private void getAllProcessMemInfo() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningAppProcessInfo> processInfos= (ArrayList<ActivityManager.RunningAppProcessInfo>) am.getRunningAppProcesses();
        int[] processInfoPids = new int[processInfos.size()];
        Log.d(TAG, "getAllProcessMemInfo: length:" + processInfos.size());
        for(int i = 0; i < processInfos.size(); i++) {
            processInfoPids[i] = processInfos.get(i).pid;
        }
        Debug.MemoryInfo[] processMemoryInfos = am.getProcessMemoryInfo(processInfoPids);
        for(int i = 0; i < processMemoryInfos.length; i++) {
            Debug.MemoryInfo memInfo = processMemoryInfos[i];
            Log.d(TAG, "getAllProcessMemInfo: pid: " + processInfoPids[i]
                    + " pss:" + memInfo.getTotalPss());
        }
    }
    
    private void newThreadNullPoint() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList al = null;
                al.get(0);
            }
        }).start();
    }
}
