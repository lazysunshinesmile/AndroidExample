package com.example.sunxiang.oneframe.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.sunxiang.oneframe.application.OneFrameApplication;
import com.example.sunxiang.oneframe.presenter.SuperPresenter;


/**
 * Created by sunxiang on 2018/12/12.
 */
public abstract class SuperActivity<T extends SuperPresenter>
    extends AppCompatActivity implements SuperView {

  private T presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(onCreateViewID());

    initSuper();

    initData();
    initUI();
    initListener();
  }

  private void initSuper() {
    NetLoadDialogImpl.initInstance(this);
    if (this.presenter == null && setPresenter() != null) {
      this.presenter = setPresenter();
    } else if(setPresenter() == null) {
      throw new IllegalStateException("Please first implement SuperActivity#setPresenter method");
    }
    presenter.setSuperView(this);
    ((OneFrameApplication)getApplication()).addActivity(this);
  }

  protected abstract T setPresenter();

  protected abstract void initListener();

  protected abstract void initData();

  protected abstract void initUI();

  protected abstract int onCreateViewID();

  protected T getPresenter() {
    if( presenter == null ) {
      throw new IllegalStateException("Please first implement SuperActivity#setPresenter method");
    }
    return presenter;
  }

  protected void showShortToast(final String text) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
      }
    });
  }
  protected void showLongToast(final String text) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
      }
    });
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    ((OneFrameApplication)getApplication()).removeActivity(this);
  }
}
