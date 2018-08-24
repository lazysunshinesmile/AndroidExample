package com.sun.myexamples;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sun.rxjava2examples.R;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.observable.ObservableCreate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;


public class StudyActivity extends AppCompatActivity {
    private String TAG = "StudyActivity";

    private Observable<String> mObjectObservable;
    private Observable<Integer> mObjectObservable2;
    private Flowable<String> mFlowable;
    private Disposable mDisposable;

    private TextView textView;
    private Button button;
    private ObservableEmitter<String> mObservableEmitter;

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
                mObservableEmitter.onNext("button click");
                mObservableEmitter.onComplete();
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
//        mObservableEmitter.onNext(12);

        //操作符的使用
//        mapUse();
//        concatUse();
//        flatMapUse();
//        zipUse();
//        timeIntervalUse();
//        flowableIntervalUse();
//        distinctUse();
        debounceUse();
    }

    private void debounceUse() {
        final String TAG = "debounceUse";
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(300);
                emitter.onNext(2);
                Thread.sleep(200);
                emitter.onNext(3);
                Thread.sleep(200);
                emitter.onNext(4);
                emitter.onComplete();

            }
        }).debounce(299, TimeUnit.MILLISECONDS)
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: " + integer);
            }
        });

        mObjectObservable.window()
    }


    @SuppressLint("CheckResult")
    private void distinctUse() {
        final String TAG = "distinctUse";
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("nihao");
                String m  = "nihao";
                emitter.onNext(m);
                String n = "nihao";
                emitter.onNext(n);
                Log.d(TAG, "subscribe: m hash " + Integer.toHexString(m.hashCode()));
                Log.d(TAG, "subscribe: n hash " + Integer.toHexString(n.hashCode()));
            }
        }).distinct()
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: s: " + s);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void flowableIntervalUse() {
        final String TAG = "flowableIntervalUse";
//        Flowable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//                Log.d(TAG, "accept: " + aLong);
//            }
//        });


        Observable.interval(2, TimeUnit.SECONDS, Schedulers.computation()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "accept: " + aLong + " " + Thread.currentThread().getName());
            }
        });
    }

    @SuppressLint("CheckResult")
    private void timeIntervalUse() {
        final String TAG = "timeInterval";
        mObjectObservable.timeInterval().subscribe(new Consumer<Timed<String>>() {
            @Override
            public void accept(Timed<String> stringTimed) throws Exception {
                Log.d(TAG, "accept: " + stringTimed);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void zipUse() {
        final String TAG = "zipUse";
        Observable.zip(mObjectObservable, mObjectObservable2, new BiFunction<String, Integer, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(String s, Integer integer) throws Exception {
                Map<String, Object> map = new HashMap<>();
                map.put("name", s);
                map.put("age", integer);
                return map;
            }
        }).subscribe(new Consumer<Map<String, Object>>() {
            @Override
            public void accept(Map<String, Object> stringObjectMap) throws Exception {
                Log.d(TAG, "accept: name:" + stringObjectMap.get("name"));
                Log.d(TAG, "accept: age:" + stringObjectMap.get("age"));
            }
        });
    }

    @SuppressLint("CheckResult")
    private void flatMapUse() {
        final String TAG= "flatMapUse";
        mObjectObservable.flatMap(new Function<Object, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Object o) throws Exception {
                Log.d(TAG, "apply: faltMap 接收的事件内容是：" + o);
                Observable observable = Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                        observableEmitter.onNext("faltmap, 发射的事件");
                    }
                });
                return observable;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Log.d(TAG, "accept: 最终真正处理的字符串是，" + s);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void concatUse() {
        Observable.concat(mObjectObservable, mObjectObservable2)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, "accept: " + o);
                    }
                });
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
//                .observeOn(Schedulers.newThread())
//                .observeOn(Schedulers.computation())
              .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        textView.setText("你好");
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
                if (((String)o).equals("observable1:4")) {
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
        mObjectObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
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
                e.onNext("observable1:1");
                if (!e.isDisposed()) {
                    Log.d(TAG, "subscribe: 未取消订阅");
                } else {
                    Log.d(TAG, "subscribe: 已取消订阅");
                }
                e.onNext("observable1:2");
                e.onNext("observable1:3");
//                e.onComplete();
//               如果调用了e.onComplete 下面一行回报异常，具体原因可查看上面的源码。
//                e.onError(new Throwable("this is an unknown error"));
                if (!e.isDisposed()) {
                    Log.d(TAG, "subscribe: 未取消订阅");
                } else {
                    Log.d(TAG, "subscribe: 已取消订阅");
                }
                e.onNext("observable1:4");
                Log.d(TAG, "subscribesss: ThreadName = " + Thread.currentThread().getName());

            }
        });
        mObjectObservable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(23);
                observableEmitter.onNext(23);
                observableEmitter.onNext(23);
            }
        });

        mFlowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                emitter.onNext("flowable emit");
            }
        }, BackpressureStrategy.BUFFER);


    }




}
