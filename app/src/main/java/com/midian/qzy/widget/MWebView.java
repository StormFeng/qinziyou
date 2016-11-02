package com.midian.qzy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class MWebView extends WebView {

    private OnScrollPosition onScrollPosition;

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MWebView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        Log.e("wqf","l:"+l+"\n"+"t:"+t+"\n"+"oldl:"+oldl+"\n"+"oldt:"+oldt);
        onScrollPosition.onScrollChanged(l, t, oldl, oldt);
    }

    public interface OnScrollPosition{
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void setOnCustomScroolChangeListener(OnScrollPosition t){
        this.onScrollPosition=t;
    }
}
