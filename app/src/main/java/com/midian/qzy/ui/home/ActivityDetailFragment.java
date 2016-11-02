package com.midian.qzy.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ScrollView;

import com.midian.qzy.R;
import com.midian.qzy.app.Constant;
import com.midian.qzy.widget.MWebView;

import midian.baselib.base.BaseFragment;
import midian.baselib.utils.UIHelper;

/**
 * Created by Administrator on 2016/7/18 0018.
 * 活动详情页Fragment
 */
@SuppressLint("ValidFragment")
public class ActivityDetailFragment extends BaseFragment{
    private MWebView webView;
    private String url;
    private ScrollView scrollView;
    private int flag=0;

    public ActivityDetailFragment() {
    }
    public ActivityDetailFragment(String url) {
        this.url = url;
        Log.e("wqf","ActivityDetailFragment初始化");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activitydetail, null);
        webView = (MWebView) v.findViewById(R.id.webView);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.scrollTo(0,5);
                    }
                },100);
            }
        };

        webView.setWebViewClient(webViewClient);
        webView.setOnCustomScroolChangeListener(new MWebView.OnScrollPosition() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                float webContent = webView.getContentHeight()*webView.getScale();
                float webNow = webView.getHeight()+ webView.getScrollY();
                if(webContent-webNow==0){
//                    UIHelper.t(_activity,"滑动到底部");
//                    webView.scrollTo(0, (int) (webContent-0));
                }
                if(t==0){
//                    UIHelper.t(_activity,"滑动到顶部");
                    webView.scrollTo(0,0);
                }
            }
        });
        webView.loadUrl(Constant.BBASEURL+url);
    }
}
