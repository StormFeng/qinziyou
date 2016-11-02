package com.midian.qzy.itemtpl;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.midian.qzy.R;
import com.midian.qzy.bean.MyMsgsBean;
import com.midian.qzy.ui.MessageDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.view.BaseTpl;

public class NewsTpl extends BaseTpl<MyMsgsBean.Content> implements View.OnClickListener {


    private TextView tvContent;
    private MyMsgsBean.Content bean;

    public NewsTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        tvContent=findView(R.id.tv_Content);
        tvContent.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_news;
    }

    @Override
    public void setBean(MyMsgsBean.Content bean, int position) {
        if (bean != null) {
            this.bean = bean;
            tvContent.setText(bean.getTitle());
        }
    }

    @OnClick(R.id.tv_Content)
    public void onClick() {

    }

    @Override
    public void onClick(View v) {
        Log.d("message_list", "onClick: ::::::::::"+bean.getTitle());
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        UIHelper.jump(_activity, MessageDetailActivity.class, bundle);
    }
}
