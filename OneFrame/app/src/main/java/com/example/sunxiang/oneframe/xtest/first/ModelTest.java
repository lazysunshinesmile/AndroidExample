package com.example.sunxiang.oneframe.xtest.first;

import com.example.sunxiang.oneframe.model.SuperModel;
import com.example.sunxiang.oneframe.net.INetCallback;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by sunxiang on 2018/12/13.
 */
public class ModelTest extends SuperModel<PresenterTest, MainApiServiceTest> {
  private final static String TAG = "ModelTest";

  @Override
  protected Class<MainApiServiceTest> setAPIService() {
    return MainApiServiceTest.class;
  }

  public String  querySomething() {
    return "nihao";
  }

  public void querySomething(int pageNo, String msgType, INetCallback presenterCallback) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("pageNo", pageNo);
    jsonObject.addProperty("msgType", msgType);
    String basicParams = "{\"appKey\":\"1010426\",\"appVersion\":\"1.3.7\",\"channel\":\"cashloan\",\"appClient\":\"android\",\"openId\":\"b4d6d91b-854e-40ca-b919-a7f8ad7633a4\",\"from\":\"cashloan\",\"clientId\":\"\",\"appSign\":\"581f8e2983f86c419f47e9c6c7be93c8\",\"timestamp\":\"1544681627918\",\"net\":\"wifi\",\"versionCode\":25,\"token\":\"a3ba228e3b8b768e60da895fcf6aa04f\",\"guestId\":\"8c46088452644a3271d7c0fec44b939e\",\"appCode\":\"0000002\",\"categoryCode\":\"\"}";
    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

    startRequest(getApiService().getMsgList(basicParams, body), presenterCallback);
  }


}
