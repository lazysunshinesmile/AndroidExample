package com.sun.rxjava2examples.module.rxjava2.operators.item;

import android.util.Log;

import com.sun.rxjava2examples.R;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * distinct
 * <p>
 * 去重操作符，其实就是简单的去重
 * <p>
 * Author: sun
 */

public class RxDistinctActivity extends RxOperatorBaseActivity {
    private static final String TAG = "RxDistinctActivity";
    @Override
    protected String getSubTitle() {
        return getString(R.string.rx_distinct);
    }

    @Override
    protected void doSomething() {
        Observable.just(1, 1, 1, 2, 2, 3, 4, 5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        mRxOperatorsText.append("distinct : " + integer + "\n");
                        Log.e(TAG, "distinct : " + integer + "\n");
                    }
                });
    }
}
