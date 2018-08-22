package com.sun.rxjava2examples.module.rxjava2.operators.item;

import android.util.Log;

import com.sun.rxjava2examples.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * map
 * <p>
 * 基本是RxJava 最简单的操作符了
 * 作用是对上游发送的每一个事件应用一个函数，使得每一个事件都按照指定的函数去变化
 * <p>
 * Author: sun
 */

public class RxMapActivity extends RxOperatorBaseActivity {
    private static final String TAG = "RxMapActivity";

    @Override
    protected String getSubTitle() {
        return getString(R.string.rx_map);
    }

    @Override
    protected void doSomething() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                mRxOperatorsText.append("accept : " + s +"\n");
                Log.e(TAG, "accept : " + s +"\n" );
            }
        });
    }

}
