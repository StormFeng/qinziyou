package com.midian.qzy.ui.home;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.midian.qzy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * Created by Administrator on 2016/11/1 0001.
 */

public class ActivityDetail extends BaseActivity {
    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.webView)
    WebView webView;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webdetail);
        ButterKnife.bind(this);
        url=mBundle.getString("url");
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
        topbar.setLeftText("返回", UIHelper.finish(_activity));
        topbar.setTitle("");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                return false;

            }
        });
        webView.loadUrl(url);

    }
}
