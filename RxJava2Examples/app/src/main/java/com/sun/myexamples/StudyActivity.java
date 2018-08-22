package com.sun.myexamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class StudyActivity extends AppCompatActivity {
    private String TAG = "StudyActivity" ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                //ObservableCreate的静态内部类，CreateEmitter<T>:
                /*
                public void onNext(T t) {
                    if (t == null) {
                        onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                        return;
                    }
                    if (!isDisposed()) {
                        observer.onNext(t);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    if (t == null) {
                        t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
                    }
                    if (!isDisposed()) {
                        try {
                            observer.onError(t);
                        } finally {
                            dispose();
                        }
                    } else {
                        RxJavaPlugins.onError(t);
                    }
                }

                @Override
                public void onComplete() {
                    if (!isDisposed()) {
                        try {
                            observer.onComplete();
                        } finally {
                            dispose();
                        }
                    }
                }
                */


                Log.d(TAG, "subscribe:");
                e.onNext(12);
                if(!e.isDisposed()) {
                    Log.d(TAG, "subscribe: 未取消订阅");
                }else {
                    Log.d(TAG, "subscribe: 已取消订阅");
                }
                e.onNext(16);
                e.onNext(17);
                e.onComplete();
//                下面一行回报异常，具体原因可查看上面的源码。
//                e.onError(new Throwable("this is an unknown error"));
                if(!e.isDisposed()) {
                    Log.d(TAG, "subscribe: 未取消订阅");
                }else {
                    Log.d(TAG, "subscribe: 已取消订阅");
                }
                e.onNext(14);

            }
        }).subscribe(new Observer<Object>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:");
                mDisposable = d;
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext:" + o);
                if((int)o == 16)
                {
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete:");
            }
        });
    }
}
