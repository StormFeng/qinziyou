package com.midian.qzy.itemtpl;

/**
 * 活动评论列表
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.midian.qzy.R;
import com.midian.qzy.bean.ActivityCommentsBean;
import com.midian.qzy.ui.home.PhotoViewActivity;
//import com.midian.qzy.ui.home.ViewPhotoActivity;
import com.midian.qzy.utils.AppUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import me.iwf.photopicker.PhotoPreview;
import midian.baselib.api.ApiCallback;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.view.BaseTpl;
//import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
//import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;

public class CommentTpl extends BaseTpl<ActivityCommentsBean.Content>{


    private TextView tvName;
    private TextView tvFuck;
    private TextView tvContent;
    private TextView tvTime;
    private GridView gridView;
    private ArrayList<String> images;
    private ArrayList<String> imagess;
    private MyAdapter myAdapter;
    private Dialog dialog;

    public CommentTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        tvName=findView(R.id.tv_Name);
        tvFuck=findView(R.id.tv_Fuck);
        tvContent=findView(R.id.tv_Content);
        tvTime=findView(R.id.tv_Time);
        gridView=findView(R.id.gridView);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_comment;
    }

    @Override
    public void setBean(final ActivityCommentsBean.Content bean, int position) {
//        Log.d("wqf",bean.toString());
        if(bean!=null){

            if (bean.getItemViewType() == 1) {
                images=new ArrayList<>();
                imagess=new ArrayList<>();
                tvName.setText(bean.getName());
                tvContent.setText(bean.getContent());
                tvTime.setText(bean.getTime());
                for(int i=0;i<bean.getPics().size();i++){
                    String name = bean.getPics().get(i).toString().replace("\"", "");
                    images.add(FDDataUtils.getImageUrl(name,250,250));
                    imagess.add(FDDataUtils.getImageUrl(name,1500,1500));
                }
                if(images.size()>0){
                    myAdapter=new MyAdapter();
                    gridView.setAdapter(myAdapter);
                    gridView.setVisibility(VISIBLE);
                }else{
                    gridView.setVisibility(GONE);
                }
                tvFuck.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtil.getPpApiClient(ac).report(null, "activity_comment", bean.getComment_id(), ac.user_id, new ApiCallback() {
                            @Override
                            public void onApiStart(String tag) {

                            }

                            @Override
                            public void onApiLoading(long count, long current, String tag) {

                            }

                            @Override
                            public void onApiSuccess(NetResult res, String tag) {
                                if(res.isOK()){
                                    UIHelper.t(_activity,res.ret_info);
                                }
                            }

                            @Override
                            public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

                            }

                            @Override
                            public void onParseError(String tag) {

                            }
                        });
                    }
                });
            }
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView= LayoutInflater.from(_activity).inflate(R.layout.image_layout,null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
//            ac.setImage(iv, FDDataUtils.getImageUrl(images.get(position),500,500));
            ac.setImage(iv, images.get(position));
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoPreview.builder()
                            .setPhotos(imagess)
                            .setCurrentItem(position)
                            .setShowDeleteButton(false)
                            .start(_activity);
                }
            });
            return convertView;
        }
    }
}
