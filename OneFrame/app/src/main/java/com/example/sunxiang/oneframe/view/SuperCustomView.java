package com.example.sunxiang.oneframe.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sunxiang.oneframe.presenter.SuperPresenter;


/**
 * Created by sunxiang on 2018/12/14.
 */
public abstract class SuperCustomView<P extends SuperPresenter> extends LinearLayout implements SuperView{

  private P presenter;

  private Thread mUiThread = Thread.currentThread();
  final Handler mHandler = new Handler();

  public SuperCustomView(Context context) {
    super(context);
    initViewGroup();
  }

  public SuperCustomView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initViewGroup();
  }

  public SuperCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initViewGroup();
  }

  private void initViewGroup() {
    initSuper();
    initData();
    initUI();
    initListener();
  }

  private void initSuper() {
    LayoutInflater.from(getContext()).inflate(onCreateViewId(), this, true);
    if (this.presenter == null && setPresenter() != null) {
      this.presenter = setPresenter();
    } else if(setPresenter() == null) {
      throw new IllegalStateException("Please first implement SuperActivity#setPresenter method");
    }
    presenter.setSuperView(this);
  }

  protected P getPresenter() {
    if( presenter == null ) {
      throw new IllegalStateException("Please first implement SuperCustomView#setPresenter method");
    }
    return presenter;
  }

  protected void showShortToast(final String text) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
      }
    });
  }
  protected void showLongToast(final String text) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
      }
    });
  }




  protected abstract void initData();

  protected abstract void initUI();

  protected abstract void initListener();

  protected abstract int onCreateViewId();

  protected abstract P setPresenter();



  public final void runOnUiThread(Runnable action) {
    if (Thread.currentThread() != mUiThread) {
      mHandler.post(action);
    } else {
      action.run();
    }
  }


}
