package com.sun.study.life_cycle.utils;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static ThreadPool mInstance;
    ThreadPoolExecutor mPool;

    public ThreadPool() {
        mPool = new ThreadPoolExecutor(4, 4* 4, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
    }

    public static ThreadPool getInstance() {
        if(mInstance == null) {
            synchronized (ThreadPool.class) {
                if(mInstance == null) {
                    return new ThreadPool();
                }
            }
        }
        return mInstance;
    }

    public void executeRunnable(Runnable runnable) {
        mPool.execute(runnable);
    }

}
