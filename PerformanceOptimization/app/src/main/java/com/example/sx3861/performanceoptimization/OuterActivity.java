package com.example.sx3861.performanceoptimization;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class OuterActivity extends AppCompatActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new UIHandler(this);
    }

    public void doSomething() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除所有的消息
        mHandler.removeCallbacksAndMessages(null);
    }

    private static class UIHandler extends Handler {
        private WeakReference<OuterActivity> ref;
        private UIHandler(OuterActivity activity) {
            ref = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            OuterActivity activity = ref.get();
            if (activity == null) {
                return;
            }
            activity.doSomething();
        }
    }
}
