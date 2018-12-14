package com.example.sunxiang.oneframe.presenter;

import com.example.sunxiang.oneframe.model.SuperModel;
import com.example.sunxiang.oneframe.view.SuperView;

/**
 * Created by sunxiang on 2018/12/12.
 */
public abstract class SuperPresenter<V extends SuperView, M extends SuperModel> {
  private V superView;
  private M superModel;


  public SuperPresenter() {
    if(superModel == null && setSuperModel() != null) {
      this.superModel = setSuperModel();
    } else if(setSuperModel() == null) {
      throw new IllegalStateException("Please first implement SuperPresenter#setSuperView method");
    }
    superModel.setPresenter(this);
  }

  public void setSuperView(V superView) {
    this.superView = superView;
  }
  protected abstract M setSuperModel();

  protected V getView() {
    return superView;
  }

  protected M getModel() {
    if( superModel == null ) {
      throw new IllegalStateException("Please first implement SuperPresenter#setSuperModel method");
    }
    return superModel;
  }
}
