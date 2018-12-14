package com.example.sunxiang.oneframe.model;

import com.example.sunxiang.oneframe.BuildConfig;
import com.example.sunxiang.oneframe.net.INetCallback;
import com.example.sunxiang.oneframe.presenter.SuperPresenter;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by sunxiang on 2018/12/13.
 */
public abstract class SuperModel<P extends SuperPresenter, A> {
  private P superPresenter;
  private A apiService;
  private Retrofit retrofit;

  public SuperModel() {
    retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASIC_URL).build();
    if(setAPIService() == null) {
      throw new IllegalStateException("Please first implement SuperModel#setAPIService method");
    }
    apiService = retrofit.create(setAPIService());
  }

  protected abstract Class<A> setAPIService();

  public void setPresenter(P presenter) {
    this.superPresenter = presenter;
  }

  protected P getPresenter() {
    if(superPresenter == null) {
      throw new IllegalStateException("Please first implement SuperModel#setPresenter method");
    }
    return superPresenter;
  }

  protected A getApiService() {
    return apiService;
  }

  protected void startRequest(Call call, INetCallback callback) {
    callback.showDialog(call.toString(), "");
    call.enqueue(callback);
  }

}
