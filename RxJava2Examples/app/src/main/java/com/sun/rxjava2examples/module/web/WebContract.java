package com.sun.rxjava2examples.module.web;

import android.app.Activity;

import com.sun.rxjava2examples.base.BasePresenter;
import com.sun.rxjava2examples.base.BaseView;


/**
 * WebContract
 *
 * Author: sun
 */

public interface WebContract {

    interface IWebView extends BaseView {
        Activity getWebViewContext();

        void setGankTitle(String title);

        void loadGankUrl(String url);

        void initWebView();
    }

    interface IWebPresenter extends BasePresenter {
        String getGankUrl();
    }
}
