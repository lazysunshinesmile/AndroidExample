package com.sun.lockuse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    ReentrantLock lock = new ReentrantLock();
    Condition condition= lock.newCondition();
    String TAG = "sunxiang";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      基本使用
//        baseUse();

//        newConditionUse
        newConditionUse();
        try {
            //这边是为了缓解主线程运行速度，让新线程先运行
            Thread.sleep(5);

            //主线程开始运行
            loopLog();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

        }

    }

    private void newConditionUse() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                loopLog();
            }
        }).start();
    }

    private void loopLog() {
        try {
            lock.lock();
            for(int i = 0; i<20;i++) {
                Log.e(TAG, Thread.currentThread().getName() + ": loopLog: i = " + i);
                if(i == 10 && !Thread.currentThread().getName().equals("main")) {
                    //新线程放弃锁，主线程开始运行
                    condition.await();
                }
            }
            if(Thread.currentThread().getName().equals("main")) {
                condition.signalAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();

        }

    }

    public void baseUse() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getName();
            }
        }).start();

        lock.lock();
        Log.d(TAG, "onCreate: " + Thread.currentThread().getName());
        Log.d(TAG, "onCreate: "+getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

    public String getName() {
        lock.lock();
        Log.d(TAG, "getName: " + Thread.currentThread().getName());
        String name = "nihao";
        lock.unlock();
        return "nihao";
    }
}
