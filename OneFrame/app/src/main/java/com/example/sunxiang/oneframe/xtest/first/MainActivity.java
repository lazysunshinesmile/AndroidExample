package com.example.sunxiang.oneframe.xtest.first;

import android.view.View;
import android.widget.Button;

import com.example.sunxiang.oneframe.R;
import com.example.sunxiang.oneframe.view.SuperActivity;
import com.example.sunxiang.oneframe.xtest.second.SecondActivity;

public class MainActivity extends SuperActivity<PresenterTest> implements MainView {

  private final String TAG = "MainActivity";
  //view
  private Button button;

  @Override
  protected PresenterTest setPresenter() {
    return new PresenterTest();
  }

  @Override
  protected void initListener() {
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        Log.d(TAG, "onClick: " + getPresenter().requestBaiDu());
//        同步
//        getPresenter().requestBaidu();
//        异步
//        getPresenter().syncRequestBaidu();
        //真正的网络请求
//        getPresenter().getMsgList(1, "activity");
        startActivity(SecondActivity.callIntent(v.getContext()));
      }
    });
  }

  @Override
  protected void initData() {
  }

  @Override
  protected void initUI() {
    button = findViewById(R.id.hello_word);
  }

  @Override
  protected int onCreateViewID() {
    return R.layout.activity_main;
  }


  @Override
  public void requestBaiduSuccess() {
    showShortToast("i am successful");
  }

  @Override
  public void requestBaiduFail() {
    showShortToast("i am error");
  }
}
