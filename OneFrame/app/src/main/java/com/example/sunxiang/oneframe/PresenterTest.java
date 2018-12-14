package com.example.sunxiang.oneframe;

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
    String str = getSuperModel().querySomething();

    getSuperView().requestBaiduSuccess();
  }

  //异步获取
  public void syncRequestBaidu() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        getSuperView().requestBaiduSuccess();
      }
    }).start();
  }

  public void getMsgList(int pageNo, String msgType) {
    getSuperModel().querySomething(pageNo, msgType,  new NetCallbackBodyIsJsonImpl() {
      @Override
      public void onResponse(JSONObject data) throws JSONException {

        Log.d(TAG, "onResponse: " + data.getJSONObject("data").get("hasUnreadNotice"));
        getSuperView().requestBaiduSuccess();
      }

      @Override
      public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage());
        getSuperView().requestBaiduFail();
      }
    });
  }

  @Override
  public ModelTest setSuperModel() {
    return new ModelTest();
  }
}
