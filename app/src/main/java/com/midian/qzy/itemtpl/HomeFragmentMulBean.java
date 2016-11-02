package com.midian.qzy.itemtpl;

import android.content.Context;
import android.util.AttributeSet;

import com.midian.qzy.R;
import com.midian.qzy.bean.ActivitiesBean;
import com.midian.qzy.bean.ActivityCommentsBean;
import com.midian.qzy.bean.BannerBean;

import midian.baselib.bean.NetResult;
import midian.baselib.view.BaseTpl;

public class HomeFragmentMulBean extends NetResult{
    public BannerBean topBean;
    public ActivitiesBean.Content botBean;
}
