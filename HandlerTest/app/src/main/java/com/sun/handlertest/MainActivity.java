package com.sun.handlertest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private Handler handler;
    private WeakReference<MainActivity> mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = new WeakReference<>(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new UIHandler(mActivity.get());
                Message msg = new Message();
                msg.what = 12;
                //查看处理消息流程。Looper.loop()后还会处理消息吗。
                handler.sendMessage(msg);
//                不能重复塞消息。
//                handler.sendMessage(msg);
//                不重新new的话，会报错，消息已经在对列中。
                msg = new Message();
                msg.what = 14;
                handler.sendMessage(msg);
                //下面的方法没有调用的话，是不会从MessageQueue的取消息的。
                Looper.loop();
                msg = new Message();
                msg.what = 16;
//              不会到处理消息的地方。在上面loop()方法中，一旦发现messagequeue中的消息为空了，就会退出死循环不等待。
                handler.sendMessage(msg);
            }
        }).start();
    }
    
    private void printLog() {
        Log.d(TAG, "printLog: loglogloglog");
    }
    
    


    private static class UIHandler extends Handler {
        private final String TAG = "UIHandler";

//      下面会调用到Activity中的方法。所以需要持有Activity的引用。
//      防止这个类的实例对象还存在，然后Activity已经没用了，
//      但由于还持有他的引用，所以不能释放内存导致内存泄漏。所以这里用了弱引用。
        WeakReference<MainActivity> mActivity;

        public UIHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mActivity.get().printLog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

    }
}
