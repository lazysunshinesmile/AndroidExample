package com.sun.rxjava2examples.module.web;

import android.app.Activity;
import android.content.Intent;

import com.sun.rxjava2examples.module.web.WebContract.IWebPresenter;
import com.sun.rxjava2examples.module.web.WebContract.IWebView;


/**
 * Author: sun
 */

public class WebPresenter implements IWebPresenter {
    private IWebView mWebView;
    private String mGankUrl;
    private Activity mActivity;

    public WebPresenter(IWebView webView){
        this.mWebView = webView;
    }


    @Override
    public void subscribe() {
        mActivity = mWebView.getWebViewContext();
        Intent intent = mActivity.getIntent();
        mWebView.setGankTitle(intent.getStringExtra(WebViewActivity.GANK_TITLE));
        mWebView.initWebView();
        mGankUrl = intent.getStringExtra(WebViewActivity.GANK_URL);
        mWebView.loadGankUrl(mGankUrl);
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public String getGankUrl() {
        return this.mGankUrl;
    }
}
