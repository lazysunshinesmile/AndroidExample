package com.example.sunxiang.oneframe.net;

import android.util.Log;

import com.example.sunxiang.oneframe.view.NetLoadDialogImpl;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sunxiang on 2018/12/13.
 */
public abstract class NetCallbackImpl implements INetCallback {

  private final String TAG = getClass().getSimpleName();

  @Override
  public void showDialog(String dialogFlag, String dialogMsg) {
    NetLoadDialogImpl.getInstanse().show(dialogFlag, dialogMsg, false);
  }

  @Override
  public void dismissDialog(String dialogFlag) {
    NetLoadDialogImpl.getInstanse().dismiss(dialogFlag);
  }

  @Override
  public void onResponse(Call call, Response response) {
    dismissDialog(call.toString());
    onResponse(response);
  }

  @Override
  public void onFailure(Call call, Throwable t) {
    Log.e(TAG, "onFailure: " + t.getStackTrace());
    dismissDialog(call.toString());
    onFailure(t);
  }

  public abstract void onResponse(Response response);

  public abstract void onFailure(Throwable t);
}
