package com.midian.qzy.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.midian.qzy.R;
import com.midian.qzy.bean.MyMsgsBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * Created by Administrator on 2016/10/22 0022.
 */

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.tv_Time)
    TextView tvTime;
    @BindView(R.id.tv_Intro)
    TextView tvIntro;
    @BindView(R.id.tv_Content)
    TextView tvContent;
    private MyMsgsBean.Content bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        bean = (MyMsgsBean.Content) mBundle.getSerializable("bean");
        topbar.setTitle(bean.getTitle());
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
        topbar.setLeftText("返回", UIHelper.finish(_activity));
        tvContent.setText(bean.getContent());
        tvTime.setText(bean.getTime());
        tvIntro.setText(bean.getIntro());
    }
}
