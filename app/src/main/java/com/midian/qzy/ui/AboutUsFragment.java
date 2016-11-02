package com.midian.qzy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.midian.qzy.R;
import com.midian.qzy.bean.SysConfListBean;
import com.midian.qzy.utils.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import midian.baselib.api.ApiCallback;
import midian.baselib.base.BaseFragment;
import midian.baselib.bean.NetResult;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class AboutUsFragment extends BaseFragment implements ApiCallback {
    @BindView(R.id.webView)
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_aboutus, null);
        ButterKnife.bind(this, v);
        AppUtil.getPpApiClient(ac).sysConfList(this);
        return v;
    }

    @Override
    public void onApiStart(String tag) {

    }

    @Override
    public void onApiLoading(long count, long current, String tag) {

    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        if(res.isOK()){
            SysConfListBean bean= (SysConfListBean) res;
            webView.loadUrl(bean.getContent().getAbout_us_url());
        }else{
            ac.handleErrorCode(_activity,res.ret_info);
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

    }

    @Override
    public void onParseError(String tag) {

    }
}
