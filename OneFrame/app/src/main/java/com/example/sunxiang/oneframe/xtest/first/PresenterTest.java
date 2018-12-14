package com.example.sunxiang.oneframe.xtest.first;

import android.util.Log;

import com.example.sunxiang.oneframe.presenter.SuperPresenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sunxiang on 2018/12/13.
 */
public class PresenterTest extends SuperPresenter<MainActivity, ModelTest> {
  private final String TAG = getClass().getSimpleName();

  //同步获取
  public void requestBaidu() {
    String str = getModel().querySomething();

    getView().requestBaiduSuccess();
  }

  //异步获取
  public void syncRequestBaidu() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        getView().requestBaiduSuccess();
      }
    }).start();
  }

  public void getMsgList(int pageNo, String msgType) {
    getModel().querySomething(pageNo, msgType,  new NetCallbackBodyIsJsonImpl() {
      @Override
      public void onResponse(JSONObject data) throws JSONException {

        Log.d(TAG, "onResponse: " + data.getJSONObject("data").get("hasUnreadNotice"));
        getView().requestBaiduSuccess();
      }

      @Override
      public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage());
        getView().requestBaiduFail();
      }
    });
  }

  @Override
  public ModelTest setSuperModel() {
    return new ModelTest();
  }
}
