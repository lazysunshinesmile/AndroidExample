package com.example.sunxiang.oneframe.xtest.second;

import com.example.sunxiang.oneframe.model.SuperModel;
import com.example.sunxiang.oneframe.presenter.SuperPresenter;
import com.example.sunxiang.oneframe.xtest.first.NetCallbackBodyIsJsonImpl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sunxiang on 2018/12/14.
 */
public class SecondSelfDefineViewPresenter extends SuperPresenter<SecondSelfDefineView, SecondModel> {
  @Override
  protected SecondModel setSuperModel() {
    return new SecondModel();
  }

  public void getProductHome() {
    getModel().getProductHome(new NetCallbackBodyIsJsonImpl() {

      @Override
      public void onResponse(JSONObject data) throws JSONException {
        getView().getProductHomeSuccess(data);
      }

      @Override
      public void onFailure(Throwable t) {
        getView().getProductHomeFailure();
      }
    });
  }
}
