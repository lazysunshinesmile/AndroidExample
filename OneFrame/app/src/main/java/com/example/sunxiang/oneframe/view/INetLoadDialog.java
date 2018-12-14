package com.example.sunxiang.oneframe.view;

/**
 * Created by sunxiang on 2018/12/13.
 */
public interface INetLoadDialog {
  void show(String dialogFlag, String dialogMsg, boolean cancelable);

  void dismiss(String dialogFlag);

  void cleanup();
}
