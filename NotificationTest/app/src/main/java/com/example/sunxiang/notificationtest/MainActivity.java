package com.example.sunxiang.notificationtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private WeakReference<Context> mContext;
    private int mRequestCode = 10;

    private NotificationChannel mNotificationChannel;

    private NotificationManager mNotificationManager;

    private Button mCommonNotification;

    private Button mClearNotification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCommonNotification = findViewById(R.id.common_notification);
        mClearNotification = findViewById(R.id.clear_notification);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mContext = new WeakReference<Context>(this);

        initListener();
    }

    private void initListener() {
        mCommonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext.get());
                //设置小图标
                builder.setSmallIcon(R.mipmap.ic_launcher);
                //设置大图标
                builder.setLargeIcon(bitmap);
                //设置标题
                builder.setContentTitle("这是标题");
                //设置通知正文
                builder.setContentText("这是正文，当前ID是：" + id);
                //设置摘要
                builder.setSubText("这是摘要");
                //设置是否点击消息后自动clean
                builder.setAutoCancel(true);
                //显示指定文本
                builder.setContentInfo("Info");
                //与setContentInfo类似，但如果设置了setContentInfo则无效果
                //用于当显示了多个相同ID的Notification时，显示消息总数
                builder.setNumber(2);
                //通知在状态栏显示时的文本
                builder.setTicker("在状态栏上显示的文本");
                //设置优先级
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                //自定义消息时间，以毫秒为单位，当前设置为比系统时间少一小时
                builder.setWhen(System.currentTimeMillis() - 3600000);
                //设置为一个正在进行的通知，此时用户无法清除通知
                builder.setOngoing(true);
                //设置消息的提醒方式，震动提醒：DEFAULT_VIBRATE     声音提醒：NotificationCompat.DEFAULT_SOUND
                //三色灯提醒NotificationCompat.DEFAULT_LIGHTS     以上三种方式一起：DEFAULT_ALL
                builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                //设置震动方式，延迟零秒，震动一秒，延迟一秒、震动一秒
                builder.setVibrate(new long[]{0, 1000, 1000, 1000});

                Intent intent = new Intent(mContext.get(), ResultActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext.get(), mRequestCode, intent)



            }
        });
    }


}
