package com.sun.myexamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sun.rxjava2examples.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class StudyActivity extends AppCompatActivity {
    private String TAG = "StudyActivity";

    private Observable<Object> mObjectObservable;
    private Disposable mDisposable;

    private TextView textView;
    private Button button;
    private ObservableEmitter<Object> mObservableEmitter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_study_layout);
        textView = findViewById(R.id.text);
        button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果已经调用了onComplete就无效了。
                mObservableEmitter.onNext(15);
            }
        });

        //初始化被观察者
        initObservable();
//        订阅，绑定被观察者和观察者
//        subscribeOberver();

//        简单订阅
//        simpleSubscribe();
        Log.d(TAG, "onCreate: ThreadName = " + Thread.currentThread().getName());
//        切换线程
//        switchThread();

        //平时使用
        mObservableEmitter.onNext(12);

        //操作符的使用
        mapUse();
        concatUse();
    }

    private void concatUse() {
//        Observable.concat()
    }

    private void mapUse() {
//        类型转换从Object 转换为Integer。
        mDisposable = mObjectObservable
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        return new Integer((int) o);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: map method, int = " + integer);
                    }
                });
    }

    private void switchThread() {
        //subscribeOn只有再第一次设置的时候会有用
//        改变的是ObservableOnSubscribe.subscribe的执行线程，在initObservable的方法里面，如果不调用observeOn()，那么也会改变onNext 或者accept方法。
//        mDisposable =
        mObjectObservable
//                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())


//              这个影响的是Consumer 的accept方法或者 observer 的onNext方法。
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Log.d(TAG, "accept: ThreadName = " + Thread.currentThread().getName());
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) {
//                Log.d(TAG, "accept: ThreadName = " + Thread.currentThread().getName());
//                textView.setText("chenggong");
//            }
//        });
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ThreadName = " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onNext: ThreadName = " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void simpleSubscribe() {
        mDisposable = mObjectObservable.subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) {
                Log.d(TAG, "accept: simple subscirbe o = " + (int) o);
            }
        });

//        简单订阅可以将Observer的四个方法，分别赋值进来，顺序如下。
//        mObjectObservable.subscribe(onNext, onError, onComplete, onSubscribe);
//        参数有多种形式，分别是
//       （1）onNext,
//       （2）onNext 和 onError
//       （3）onNext onError 和 onComplete
//       （4）onNext onError onComplete 和 onSubscribe。
    }

    private void subscribeOberver() {
        mObjectObservable.subscribe(new Observer<Object>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ThreadName = " + Thread.currentThread().getName());
                mDisposable = d;
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext:" + o);
                if ((int) o == 16) {
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

    private void initObservable() {
        mObjectObservable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mObservableEmitter = e;

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
                if (!e.isDisposed()) {
                    Log.d(TAG, "subscribe: 未取消订阅");
                } else {
                    Log.d(TAG, "subscribe: 已取消订阅");
                }
                e.onNext(16);
                e.onNext(17);
//                e.onComplete();
//                下面一行回报异常，具体原因可查看上面的源码。
//                e.onError(new Throwable("this is an unknown error"));
                if (!e.isDisposed()) {
                    Log.d(TAG, "subscribe: 未取消订阅");
                } else {
                    Log.d(TAG, "subscribe: 已取消订阅");
                }
                e.onNext(14);
                Log.d(TAG, "subscribesss: ThreadName = " + Thread.currentThread().getName());

            }
        });
    }


}
