package com.example.sunxiang.oneframe.xtest.second;

import com.example.sunxiang.oneframe.view.SuperView;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by sunxiang on 2018/12/14.
 */
public interface SecondView extends SuperView {
  void getProductHomeSuccess(JSONObject data);
  void getProductHomeFailure();
}
