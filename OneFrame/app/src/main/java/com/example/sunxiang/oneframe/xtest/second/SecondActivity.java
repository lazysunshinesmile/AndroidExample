package com.example.sunxiang.oneframe.xtest.second;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.sunxiang.oneframe.R;
import com.example.sunxiang.oneframe.view.SuperActivity;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by sunxiang on 2018/12/14.
 */
public class SecondActivity extends SuperActivity<SecondPresenter> implements SecondView {

  private Button secondButton;

  @Override
  protected SecondPresenter setPresenter() {
    return new SecondPresenter();
  }

  @Override
  protected void initListener() {
    secondButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().getProductHome();
      }
    });
  }

  @Override
  protected void initData() {

  }

  @Override
  protected void initUI() {
    secondButton = findViewById(R.id.second_button);
  }

  @Override
  protected int onCreateViewID() {
    return R.layout.activity_second;
  }

  public static Intent callIntent(Context context) {
    return new Intent(context, SecondActivity.class);
  }

  @Override
  public void getProductHomeSuccess(JSONObject data) {
    try {
      showShortToast("i am successful:" + data.getJSONObject("data").getString("categoryCode"));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void getProductHomeFailure() {
    showShortToast("i am error");
  }
}
