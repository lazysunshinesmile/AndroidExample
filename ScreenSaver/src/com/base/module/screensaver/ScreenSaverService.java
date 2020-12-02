package com.base.module.screensaver;

import com.base.module.screensaver.view.ScreenSaverView;
import com.base.module.screensaver.ui.SettingPreference;

import android.content.res.Configuration;
import android.content.SharedPreferences;
import android.service.dreams.DreamService;
import android.util.Log;

import java.io.File;

public class ScreenSaverService extends DreamService {
    static final boolean DEBUG = false;
    static final String TAG = "ScreenSaver";
    private ScreenSaverView mScreenSaverView;

    @Override
    public void onCreate() {
        if (DEBUG) Log.d(TAG, "Screensaver created");
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) Log.d(TAG, "Screensaver configuration changed");
        super.onConfigurationChanged(newConfig);
        // layoutClockSaver();
    }

    @Override
    public void onAttachedToWindow() {
        if (DEBUG) Log.d(TAG, "Screensaver attached to window");
        super.onAttachedToWindow();

        // We want the screen saver to exit upon user interaction.
        setInteractive(false);

        setFullscreen(true);

        layoutClockSaver();
    }

    @Override
    public void onDetachedFromWindow() {
        if (DEBUG) Log.d(TAG, "Screensaver detached from window");
        super.onDetachedFromWindow();

    }

    @Override
    public void onDreamingStarted() {
        if (DEBUG) Log.d(TAG, "onDreamingStarted");
        if (mScreenSaverView != null && !mScreenSaverView.isPlaying()) {
            mScreenSaverView.play();
        }
        super.onDreamingStarted();
    }

    @Override
    public void onDreamingStopped() {
        if (DEBUG) Log.d(TAG, "onDreamingStopped");
        mScreenSaverView.stop();
        super.onDreamingStopped();
    }

    private void layoutClockSaver() {
        openPictureAnimation();
    }

    private void openPictureAnimation() {
        // if needed, read durations from Database/SharedPreference/default
        int idleTime = getSharedPreferences(SettingPreference.PREFERENCES, 0).getInt(SettingPreference.ANIM_IDLE_TIME,
                SettingPreference.FALLBACK_ANIM_IDLE_TIME_VALUE);
        mScreenSaverView = new ScreenSaverView(this, idleTime, 4000L, 2000L, 2000L, 1500L, 800L, 700L);
        mScreenSaverView.setClickable(true);
        setContentView(mScreenSaverView);
        setScreenBright(true);
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
}
