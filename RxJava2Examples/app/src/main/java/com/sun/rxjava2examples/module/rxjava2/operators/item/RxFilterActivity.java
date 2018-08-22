package com.sun.rxjava2examples.module.rxjava2.operators.item;

import android.util.Log;

import com.sun.rxjava2examples.R;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * filter
 * <p>
 * 过滤操作符，取正确的值
 * <p>
 * Author: sun
 */

public class RxFilterActivity extends RxOperatorBaseActivity {
    private static final String TAG = "RxFilterActivity";

    @Override
    protected String getSubTitle() {
        return getString(R.string.rx_filter);
    }

    @Override
    protected void doSomething() {
        Observable.just(1, 20, 65, -5, 7, 19)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer >= 10;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                mRxOperatorsText.append("filter : " + integer + "\n");
                Log.e(TAG, "filter : " + integer + "\n");
            }
        });
    }
}
