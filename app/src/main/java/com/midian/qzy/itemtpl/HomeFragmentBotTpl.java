package com.midian.qzy.itemtpl;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.midian.login.view.LoginActivity;
import com.midian.qzy.R;
import com.midian.qzy.ui.home.ActivityContent;
import com.midian.qzy.widget.tagflowlayout.FlowLayout;
import com.midian.qzy.widget.tagflowlayout.TagAdapter;
import com.midian.qzy.widget.tagflowlayout.TagFlowLayout;

import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.view.BaseTpl;

public class HomeFragmentBotTpl extends BaseTpl<HomeFragmentMulBean> implements View.OnClickListener {

    private TextView tvTitle;
    private RoundedImageView ivBg;
    private TextView tvName;
    private TextView tvOldPrice;
    private TextView tvChildPrice;
    private TextView tvType;
    private String activity_id;
    private TagFlowLayout tagAge;

    public HomeFragmentBotTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeFragmentBotTpl(Context context) {
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
    public void setBean(HomeFragmentMulBean bean, int position) {
        if (bean != null) {
            if (bean.getItemViewType() == 1) {
                ac.setImage(ivBg, FDDataUtils.getImageUrl(bean.botBean.getCover_pic(),800,1000));
                tvTitle.setText(bean.botBean.getTitle());
                tvType.setText(bean.botBean.getType_name());
                tvName.setText(bean.botBean.getOrganization_name());
                tvOldPrice.setText(bean.botBean.getAdult_price());
                tvChildPrice.setText(bean.botBean.getChild_price());
                activity_id=bean.botBean.getActivity_id();
                String age=bean.botBean.getAges();
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
    }

    @Override
    public void onClick(View v) {
//        if(!ac.isUserIdExsit()){
//            UIHelper.jump(_activity, LoginActivity.class);
//        }else{
            Bundle bundle=new Bundle();
            bundle.putString("activity_id",activity_id);
            UIHelper.jump(_activity, ActivityContent.class,bundle);
//        }
    }
}
