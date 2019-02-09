package com.example.sunxiang.oneframe.application;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.example.sunxiang.oneframe.view.NetLoadDialogImpl;
import com.example.sunxiang.oneframe.view.SuperActivity;

import java.util.HashMap;

/**
 * Created by sunxiang on 2018/12/14.
 */
public class OneFrameApplication extends Application {

  private HashMap<String, Activity> mActivity;
  private final String TAG = getClass().getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();
    mActivity = new HashMap<>();
  }

  public void addActivity(SuperActivity superActivity) {
    Log.d(TAG, "addActivity1: " + mActivity.size());
    mActivity.put(superActivity.getLocalClassName(), superActivity);
    Log.d(TAG, "addActivity2: " + mActivity.size());
  }

  public void removeActivity(SuperActivity superActivity) {
    Log.d(TAG, "addActivity3: " + mActivity.size());
    mActivity.remove(superActivity.getLocalClassName());
    Log.d(TAG, "addActivity4: " + mActivity.size());
    if(mActivity.size() == 0) {
      NetLoadDialogImpl.getInstanse().cleanup();
    }

  }

  @Override
  public void onTerminate() {
    super.onTerminate();

  }
}
