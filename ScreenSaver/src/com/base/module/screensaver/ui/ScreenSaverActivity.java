/****************************************************************************
 *
 * FILENAME:        com.base.module.screensaver.ui.ScreenSaverActivity.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2011-12-31
 *
 * DESCRIPTION:     The class encapsulates the music ring tone operations.
 *
 * vi: set ts=4:
 *
 * Copyright (c) 2009-2011 by Grandstream Networks, Inc.
 * All rights reserved.
 *
 * This material is proprietary to Grandstream Networks, Inc. and,
 * in addition to the above mentioned Copyright, may be
 * subject to protection under other intellectual property
 * regimes, including patents, trade secrets, designs and/or
 * trademarks.
 *
 * Any use of this material for any purpose, except with an
 * express license from Grandstream Networks, Inc. is strictly
 * prohibited.
 *
 ***************************************************************************/
package com.base.module.screensaver.ui;

import com.base.module.screensaver.ScreenSaver;
import com.base.module.screensaver.utils.MyLog;
import com.base.module.screensaver.view.ScreenSaverView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

/**
 * The Class ScreenGuardActivity.
 */
public class ScreenSaverActivity extends Activity {

    private static final String TAG = "ScreenSaverActivity";
    /** The view. */
    private ScreenSaverView mScreenSaverView;

    private static final int CLOSE_SCREEN_SAVER = 1;
    
//    private static final String SCREEN_SAVER_STATE = Settings.System.SCREEN_SAVER_ON;
//    private static final int SCREEN_SAVER_STATE_ON = 1;
//    private static final int SCREEN_SAVER_STATE_OFF = 0;
    
    private static final String EXTRA_KEY_IS_AFTER_BOOT = "isafterboot";
    private static final String EXTRA_KEY_FROM = "from";
    private static final String EXTRA_KEY_REASON = "reason";
    private static final String EXTRA_VALUE_FROM = "ScreenSaver";
    private static final String EXTRA_VALUE_REASON = "ScreenSaver";
    

    /** The Constant MENU_CLOSE_SCREEN_GUARD. */
    // static final int MENU_CLOSE_SCREEN_GUARD = Menu.FIRST;

    /**
     * On create.
     * 
     * @param savedInstanceState
     *            the saved instance state
     * @Description:
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // open the animation
        openPictureAnimation();

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean(EXTRA_KEY_IS_AFTER_BOOT)) {
            doFinish();
        }
    }

    private void openPictureAnimation() {
        // if needed, read durations from Database/SharedPreference/default
        int idleTime = getSharedPreferences(SettingPreference.PREFERENCES, 0).getInt(SettingPreference.ANIM_IDLE_TIME,
                SettingPreference.FALLBACK_ANIM_IDLE_TIME_VALUE);
        mScreenSaverView = new ScreenSaverView(this, idleTime, 4000L, 2000L, 2000L, 1500L, 800L, 700L);
        mScreenSaverView.setClickable(true);
        setContentView(mScreenSaverView);
        // if needed, write durations for default here.

        // read direction from Database/SharedPreference/default
        SharedPreferences pref = getSharedPreferences(SettingPreference.PREFERENCES, 0);
        String localPath = pref.getString(SettingPreference.LOCAL_PATH, "");
        if (localPath == null || localPath.equals("")) {
            localPath = "/system/media/screensaver/";
            pref.edit().putString(SettingPreference.LOCAL_PATH, localPath).commit();
        }

        File dir = new File(localPath);
        mScreenSaverView.setFiles(localPath, dir.listFiles());
    }

    BroadcastReceiver mColseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS) && intent.getExtras() != null) {
                String from = intent.getExtras().getString(EXTRA_KEY_FROM);
                if (from != null) {
                    MyLog.i(TAG, from);
                    if (from.equals(EXTRA_VALUE_FROM)) {
                        return;
                    }
                }
            }

            Log.i(TAG, "close screen saver");
            Message msg = mHandler.obtainMessage(CLOSE_SCREEN_SAVER);
            mHandler.sendMessageDelayed(msg, 10);
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case CLOSE_SCREEN_SAVER:
                doFinish();
                Log.i(TAG, "close screen saver OK");
                break;
            default:
                break;
            }
        }
    };

    public void onStart() {
        super.onStart();
        MyLog.i(TAG, "onStart");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intent.putExtra(EXTRA_KEY_FROM, EXTRA_VALUE_FROM);
        intent.putExtra(EXTRA_KEY_REASON, EXTRA_VALUE_REASON);
        sendBroadcast(intent);
    }

    /**
     * On resume.
     * 
     * @Description:
     */
    public void onResume() {
        MyLog.i(TAG, "onResume");
        super.onResume();
        if (mScreenSaverView != null && !mScreenSaverView.isPlaying())
            mScreenSaverView.play();

//        ContentResolver resolver = getContentResolver();
//        Settings.System.putInt(resolver, SCREEN_SAVER_STATE, SCREEN_SAVER_STATE_ON);

        // Register Receiver in this Activity
        IntentFilter filter = new IntentFilter();
        //filter.addAction(Intent.ACTION_CLOSE_SCREEN_SAVER);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // filter.addAction(Intent.ACTION_ANSWER);
        // filter.addAction(Intent.ACTION_DOCK_EVENT);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        // filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mColseReceiver, filter);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * On pause.
     * 
     * @Description:
     */
    public void onPause() {
        MyLog.i(TAG, "onPause");
        super.onPause();
        mScreenSaverView.stop();

        if (isFinishing() == false) {
            doFinish();
        }
        unregisterReceiver(mColseReceiver);
    }
    
    /**
     * On destroy
     */
    public void onDestroy() {
        MyLog.i(TAG, "onDestroy");
//        ContentResolver resolver = getContentResolver();
//        Settings.System.putInt(resolver, SCREEN_SAVER_STATE, SCREEN_SAVER_STATE_OFF);
        super.onDestroy();
    }

    /**
     * On key down.
     * 
     * @param keyCode
     *            the key code
     * @param event
     *            the event
     * @return true, if successful
     * @Description: just exit Screen Saver
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MyLog.i(TAG, "onKeyDown");
/*
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager == null || !keyguardManager.inKeyguardRestrictedInputMode()){
            if(keyCode >= 7 && keyCode <= 18){
                Intent intent = new Intent("com.base.module.phone.DIAL");
                intent.putExtra("action", "sendkey");
                intent.putExtra("number", String.valueOf(event.getDisplayLabel()));
                sendBroadcast(intent);
            }
        }
*/
        doFinish();
        return true;
    }
    
    private void doFinish() {
        if (mScreenSaverView != null) {
            mScreenSaverView.doFinish();
        } else {
            finish();
        }
    }
}
