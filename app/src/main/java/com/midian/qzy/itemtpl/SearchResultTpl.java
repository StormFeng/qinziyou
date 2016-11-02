package com.midian.qzy.itemtpl;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.midian.qzy.R;
import com.midian.qzy.bean.ActivitiesBean;
import com.midian.qzy.ui.home.ActivityContent;
import com.midian.qzy.widget.tagflowlayout.FlowLayout;
import com.midian.qzy.widget.tagflowlayout.TagAdapter;
import com.midian.qzy.widget.tagflowlayout.TagFlowLayout;

import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.view.BaseTpl;

public class SearchResultTpl extends BaseTpl<ActivitiesBean.Content> implements View.OnClickListener {


    private TextView tvTitle;
    private RoundedImageView ivBg;
    private TextView tvName;
    private TextView tvOldPrice;
    private TextView tvChildPrice;
    private TextView tvType;
    private String activity_id;
    private TagFlowLayout tagAge;

    public SearchResultTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchResultTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        tvTitle=findView(R.id.tv_Title);
        ivBg=findView(R.id.iv_Bg);
        tvName=findView(R.id.tv_Name);
        tvOldPrice=findView(R.id.tv_OldPrice);
        tvChildPrice=findView(R.id.tv_ChildPrice);
        tvType=findView(R.id.tv_Type);
        tagAge=findView(R.id.tag_Age);
        findView(R.id.ll_item).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_activity;
    }

    @Override
    public void setBean(ActivitiesBean.Content bean, int position) {
        if (bean != null) {
            ac.setImage(ivBg, FDDataUtils.getImageUrl(bean.getCover_pic(),500,300));
            tvTitle.setText(bean.getTitle());
            tvType.setText(bean.getType_name());
            tvName.setText(bean.getOrganization_name());
            tvOldPrice.setText(bean.getAdult_price());
            tvChildPrice.setText(bean.getChild_price());
            activity_id=bean.getActivity_id();
            String age=bean.getAges();
            final String[] ages = age.split(",");
            tagAge.setAdapter(new TagAdapter<String>(ages) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(_activity).inflate(R.layout.tag_age_layout, tagAge, false);
                    tv.setText(ages[position]);
                    return tv;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        bundle.putString("activity_id",activity_id);
        UIHelper.jump(_activity, ActivityContent.class,bundle);
    }
}
