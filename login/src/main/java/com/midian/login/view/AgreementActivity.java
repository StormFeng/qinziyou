package com.midian.login.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import com.midian.login.R;
import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

public class AgreementActivity extends BaseActivity{
    private BaseLibTopbarView topbar;
    private WebView webView;
    private final String url="http://app.business.pploc.com/views/protocol";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreementactivity);
        topbar= (BaseLibTopbarView) findViewById(R.id.topbar);
        topbar.setLeftImageButton(R.drawable.icon_back,UIHelper.finish(_activity));
        webView= (WebView) findViewById(R.id.webview);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        if("RegisterTwoActivity".equals(mBundle.getString("url"))) {
            Log.d("wqf","登陆注册跳转");
            webView.loadUrl(url);
            topbar.setTitle("《PP选址用户协议》");
        }else{
            Log.d("wqf","账号升级跳转");
            webView.loadUrl(mBundle.getString("url"));
            topbar.setTitle(mBundle.getString("str"));
        }
    }
}
