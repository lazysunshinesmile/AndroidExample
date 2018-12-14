package com.example.sunxiang.oneframe.xtest.second;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sunxiang.oneframe.R;
import com.example.sunxiang.oneframe.view.SuperCustomView;
import com.example.sunxiang.oneframe.view.SuperView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sunxiang on 2018/12/14.
 */
public class SecondSelfDefineView
    extends SuperCustomView<SecondPresenter>  implements SecondView {

  private Button button;
  private TextView textView;


  public SecondSelfDefineView(Context context) {
    super(context);
  }

  public SecondSelfDefineView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }


  @Override
  protected void initData() {

  }

  @Override
  protected void initUI() {
    textView = findViewById(R.id.second_define_tv);
    button = findViewById(R.id.second_define_btn);
  }

  @Override
  protected void initListener() {
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().getProductHome();
      }
    });
  }

  @Override
  protected int onCreateViewId() {
    return R.layout.view_second_define;
  }

  @Override
  protected SecondPresenter setPresenter() {
    return new SecondPresenter();
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
