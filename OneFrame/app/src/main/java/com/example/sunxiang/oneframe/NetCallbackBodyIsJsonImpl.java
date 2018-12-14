package com.example.sunxiang.oneframe;

import android.util.Log;

import com.example.sunxiang.oneframe.net.NetCallbackImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sunxiang on 2018/12/13.
 */
public abstract class NetCallbackBodyIsJsonImpl extends NetCallbackImpl {
  private final String TAG = getClass().getSimpleName();

  @Override
  public void onResponse(Response response) {
    try {
      String body = ((ResponseBody)response.body()).string();
      Log.d(TAG, "onResponse: " + body);
      JSONObject jsonObject = new JSONObject(body);
      onResponse(jsonObject);
    } catch (JSONException e) {
      e.printStackTrace();
      onFailure(e);
    } catch (IOException e) {
      e.printStackTrace();
      onFailure(e);
    }
  }

  @Override
  public void onFailure(Call call, Throwable t) {
    Log.e(TAG, "onFailure: " + t.getStackTrace());
    dismissDialog(call.toString());
    onFailure(t);
  }

  public abstract void onResponse(JSONObject data) throws JSONException;

  public abstract void onFailure(Throwable t);
}
