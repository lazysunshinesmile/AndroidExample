package com.example.sunxiang.oneframe.net;

import retrofit2.Callback;

/**
 * Created by sunxiang on 2018/12/13.
 */
public interface INetCallback extends Callback {
  void showDialog(String dialogFlag, String dialogMsg);
  void dismissDialog(String dialogFlag);

}
