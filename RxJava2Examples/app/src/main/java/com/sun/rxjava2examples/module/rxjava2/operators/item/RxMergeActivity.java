package com.sun.rxjava2examples.module.rxjava2.operators.item;

import android.util.Log;

import com.sun.rxjava2examples.R;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * merge
 * <p>
 * 将多个Observable合起来，接受可变参数，也支持使用迭代器集合
 * <p>
 * Author: sun
 */

public class RxMergeActivity extends RxOperatorBaseActivity {

    private static final String TAG = "RxMergeActivity";
    @Override
    protected String getSubTitle() {
        return getString(R.string.rx_merge);
    }

    @Override
    protected void doSomething() {
        Observable.merge(Observable.just(1, 2), Observable.just(3, 4, 5))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        mRxOperatorsText.append("merge :" + integer + "\n");
                        Log.e(TAG, "accept: merge :" + integer + "\n" );
                    }
                });
    }
}
