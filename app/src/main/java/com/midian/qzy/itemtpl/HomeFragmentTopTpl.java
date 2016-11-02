package com.midian.qzy.itemtpl;
/**
 * 首页顶部
 */

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.midian.qzy.R;
import com.midian.qzy.ui.home.ActivityContent;
import com.midian.qzy.ui.home.ActivityDetail;
import com.midian.qzy.ui.home.ActivitySearchResult;
import com.midian.qzy.widget.Banner;
import java.util.ArrayList;
import java.util.List;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.view.BaseTpl;

public class HomeFragmentTopTpl extends BaseTpl<HomeFragmentMulBean> implements View.OnClickListener, Banner.OnBannerClickListener {
    private Banner bannerView;
    private EditText etSearch;
    private View vSearch;
    private List<String> images=new ArrayList<>();
    private List<String> activityIds=new ArrayList<>();
    private List<String> urls=new ArrayList<>();
    private int flag=0;

    public HomeFragmentTopTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeFragmentTopTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        bannerView=findView(R.id.bannerView);
        etSearch=findView(R.id.et_Search);
        vSearch=findView(R.id.v_Search);
        bannerView.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        etSearch.setFocusable(true);

        bannerView.setBannerStyle(Banner.CIRCLE_INDICATOR);//设置圆形指示器
        bannerView.setIndicatorGravity(Banner.CENTER);
        bannerView.isAutoPlay(true);
        bannerView.setDelayTime(5000);//设置轮播间隔时间
        bannerView.setOnBannerClickListener(this);

        findView(R.id.iv_Job).setOnClickListener(this);
        findView(R.id.iv_Week).setOnClickListener(this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    String keyWord=etSearch.getText().toString();
                    Bundle bundle=new Bundle();
                    bundle.putString("keyWord",keyWord);
                    bundle.putString("flag","HomeFragmentTopTpl");
                    UIHelper.jump(_activity,ActivitySearchResult.class,bundle);
                    return true;
                }
                return false;
            }
        });
        vSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord=etSearch.getText().toString();
                if("".equals(keyWord)){
                    UIHelper.t(_activity,"请输入关键字搜索");
                }else{
                    Bundle bundle=new Bundle();
                    bundle.putString("keyWord",keyWord);
                    bundle.putString("flag","HomeFragmentTopTpl");
                    UIHelper.jump(_activity,ActivitySearchResult.class,bundle);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_homefragment_top;
    }

    @Override
    public void setBean(HomeFragmentMulBean bean, int position) {
        if(bean!=null){
            if(bean.getItemViewType()==0) {
                if(flag==0){
                    flag++;
                    for(int i=0;i<bean.topBean.getContent().getBanner().size();i++){
                        images.add(FDDataUtils.getImageUrl(bean.topBean.getContent().getBanner().get(i).getBanner_pic_id(),300,500));
                        activityIds.add(bean.topBean.getContent().getBanner().get(i).getActivity_id());
                        urls.add(bean.topBean.getContent().getBanner().get(i).getUrl());
                    }
                    bannerView.setImages(images.toArray());
                }else{

                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        Bundle bundle=new Bundle();
        switch (view.getId()) {
            case R.id.et_Search:

                break;
            case R.id.iv_Job:
                bundle.putString("time","weekday");
                bundle.putString("distance","");
                bundle.putString("type","");
                bundle.putString("age","");
                UIHelper.jump(_activity, ActivitySearchResult.class,bundle);
                break;
            case R.id.iv_Week:
                bundle.putString("time","weekend");
                bundle.putString("distance","");
                bundle.putString("type","");
                bundle.putString("age","");
                UIHelper.jump(_activity, ActivitySearchResult.class,bundle);
                break;
        }
    }

    @Override
    public void OnBannerClick(View view, int position) {
        Bundle bundle=new Bundle();
        Log.d("wqf",position+"");
        Log.d("wqf",activityIds.get(position-1));
        if("".equals(activityIds.get(position-1))){
            bundle.putString("url",urls.get(position-1));
            UIHelper.jump(_activity, ActivityDetail.class,bundle);
        }else{
            bundle.putString("activity_id",activityIds.get(position-1));
            UIHelper.jump(_activity, ActivityContent.class,bundle);
        }
    }
}
