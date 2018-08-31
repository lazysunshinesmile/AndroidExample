package com.sun.threadpoolexcutoruse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //核心线程数
        int corePoolSize = 5;
        //最大线程数，包括核心线程和非核心线程
        int maximumPoolSize = 12;
        //线程存活时长，默认是非核心线程，如果allowCoreThreadTimeOut = true; 那么对核心线程也有用
        long keepAliveTime = 13;
        //时间单位
        TimeUnit unit = TimeUnit.SECONDS;
        //工作队列
        SynchronousQueue queue = new SynchronousQueue();

        //任务无法执行时，回调handler的rejectedExecution方法来通知调用者.
        //拒绝任务时的处理策略，直接抛异常
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();


        //线程工厂
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        //


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                queue,
                threadFactory,
                rejectedExecutionHandler
        );

        scheduledExecutorServiceUse();



    }

    private void scheduledExecutorServiceUse() {
        final String TAG = "scheduledExecutor";
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: 1111:" + ((ThreadPoolExecutor) scheduledExecutorService).getActiveCount());
            }
        });
        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: 2222" + ((ThreadPoolExecutor) scheduledExecutorService).getActiveCount());
            }
        });
        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: 3333" + ((ThreadPoolExecutor) scheduledExecutorService).getActiveCount());
            }
        });
         scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: 444" + ((ThreadPoolExecutor) scheduledExecutorService).getActiveCount());
            }
        });
         scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: 5555" + ((ThreadPoolExecutor) scheduledExecutorService).getQueue().size());
            }
        });
         scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: 6666" + ((ThreadPoolExecutor) scheduledExecutorService).getActiveCount());
            }
        });
         scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: 7777" + ((ThreadPoolExecutor) scheduledExecutorService).getActiveCount());
            }
        });

    }
}
