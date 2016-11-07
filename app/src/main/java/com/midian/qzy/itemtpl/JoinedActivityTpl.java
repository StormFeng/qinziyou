package com.midian.qzy.itemtpl;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.midian.qzy.R;
import com.midian.qzy.bean.MyActivitiesBean;
import com.midian.qzy.ui.home.ActivityContent;

import java.text.DecimalFormat;

import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.view.BaseTpl;

public class JoinedActivityTpl extends BaseTpl<MyActivitiesBean.Content> implements View.OnClickListener {


    private RoundedImageView ivHead;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvOldPrice;
    private TextView tvChildPrice;
    private TextView tvOldCount;
    private TextView tvChildCount;
    private TextView tvAllCount;
    private TextView tvAllMoney;
    private String activity_id;
    private String oldCount;
    private String childCount;
    DecimalFormat df=new DecimalFormat("#.##");

    public JoinedActivityTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoinedActivityTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        ivHead=findView(R.id.iv_Head);
        tvTitle=findView(R.id.tv_Title);
        tvTime=findView(R.id.tv_Time);
        tvOldPrice=findView(R.id.tv_OldPrice);
        tvChildPrice=findView(R.id.tv_ChildPrice);
        tvOldCount=findView(R.id.tv_OldCount);
        tvChildCount=findView(R.id.tv_ChildCount);
        tvAllCount=findView(R.id.tv_AllCount);
        tvAllMoney=findView(R.id.tv_AllMoney);
        root.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_joinedactivity;
    }

    @Override
    public void setBean(MyActivitiesBean.Content bean, int position) {
        if(bean!=null){
            ac.setImage(ivHead, FDDataUtils.getImageUrl(bean.getCover_pic(),100,100));
            tvTitle.setText(bean.getTitle());
            tvTime.setText(bean.getBegin_time());
            tvOldPrice.setText("¥"+bean.getAdult_price()+"×");
            tvChildPrice.setText("¥"+bean.getChild_price()+"×");
            oldCount=bean.getAdult_count();
            childCount=bean.getChild_count();
            tvOldCount.setText(oldCount);
            tvChildCount.setText(childCount);
            tvAllCount.setText(Integer.valueOf(oldCount)+Integer.valueOf(childCount)+"");
            double v = Integer.valueOf(oldCount) * Double.parseDouble(bean.getAdult_price()) +
                    Integer.valueOf(childCount) * Double.parseDouble(bean.getChild_price());
            tvAllMoney.setText("¥"+df.format(v));
            activity_id=bean.getActivity_id();
        }else{
            ac.handleErrorCode(_activity,bean.ret_code);
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        bundle.putString("activity_id",activity_id);
        UIHelper.jump(_activity, ActivityContent.class,bundle);
    }
}
