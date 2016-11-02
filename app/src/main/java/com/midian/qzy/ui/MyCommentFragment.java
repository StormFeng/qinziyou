package com.midian.qzy.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.JsonArray;
import com.midian.qzy.R;
import com.midian.qzy.bean.MyActivityCommentsBean;
//import com.midian.qzy.ui.home.ViewPhotoActivity;
import com.midian.qzy.utils.AppUtil;
import com.midian.qzy.widget.PhotoAdapter;
import com.midian.qzy.widget.RecyclerItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;
import midian.baselib.api.ApiCallback;
import midian.baselib.base.BaseFragment;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
//import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
//import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;

/**
 * 我的评论页
 * Created by Administrator on 2016/7/18 0018.
 */
public class MyCommentFragment extends BaseFragment implements ApiCallback {
    private SwipeMenuCreator creator;
    private SwipeMenuListView listView;
    private MyAdapter myAdapter;
    private GridViewAdapter gridViewAdapter;
    private MyActivityCommentsBean myActivityCommentsBean;
    private List<String> commentIds=new ArrayList<>();
    public static boolean isUpdate=false;
    ArrayList<String>list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comment, null);
        listView = (SwipeMenuListView) v.findViewById(R.id.listView);
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(_activity);
                openItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x00,
                        0x00)));
                openItem.setWidth(150);
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        AppUtil.getPpApiClient(ac).deleteActivityComment(ac.user_id, commentIds.get(position), new ApiCallback() {
                            @Override
                            public void onApiStart(String tag) {

                            }

                            @Override
                            public void onApiLoading(long count, long current, String tag) {

                            }

                            @Override
                            public void onApiSuccess(NetResult res, String tag) {
                                if(res.isOK()){
                                    myActivityCommentsBean.getContent().remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    listView.smoothCloseMenu();
                                }
                            }

                            @Override
                            public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

                            }

                            @Override
                            public void onParseError(String tag) {

                            }
                        });
                        break;
                }
                return false;
            }
        });
        AppUtil.getPpApiClient(ac).getMyActivityCommentsBean(ac.user_id, "1", "500", this);
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
        if (res.isOK()) {
            myActivityCommentsBean = (MyActivityCommentsBean) res;
            if (myActivityCommentsBean != null) {
                myAdapter = new MyAdapter();
                listView.setAdapter(myAdapter);
            }
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

    }

    @Override
    public void onParseError(String tag) {

    }


    class GridViewAdapter extends BaseAdapter{

        private JsonArray mlist;
        private ArrayList<String> list=new ArrayList<>();
        public GridViewAdapter(JsonArray mlist) {
            this.mlist = mlist;
            for(int i=0;i<mlist.size();i++){
                list.add(FDDataUtils.getImageUrl(mlist.get(i).toString().replace("\"",""),1500,1500));
            }
        }

        @Override
        public int getCount() {
            return mlist.size();
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
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(_activity).inflate(R.layout.image_layout,null);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ac.setImage(viewHolder.iv, FDDataUtils.getImageUrl(mlist.get(position).toString().replace("\"",""),200,200));
            viewHolder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoPreview.builder()
                            .setPhotos(list)
                            .setCurrentItem(position)
                            .setShowDeleteButton(false)
                            .start(_activity);
                }
            });
            return convertView;
        }
    }
    private class ViewHolder{
        ImageView iv;
    }
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myActivityCommentsBean.getContent().size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(_activity).inflate(R.layout.item_comment, null);
                viewHolder = new ViewHolder();
                viewHolder.tvContent= (TextView) convertView.findViewById(R.id.tv_Content);
                viewHolder.tvName= (TextView) convertView.findViewById(R.id.tv_Name);
                viewHolder.tvTime= (TextView) convertView.findViewById(R.id.tv_Time);
                viewHolder.tvTitle= (TextView) convertView.findViewById(R.id.tv_Title);
                viewHolder.gridView= (GridView) convertView.findViewById(R.id.gridView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            List<MyActivityCommentsBean.Content> content = myActivityCommentsBean.getContent();
            if (content != null) {
                viewHolder.tvTitle.setText(content.get(position).getActivity_name());
                viewHolder.tvName.setText(content.get(position).getName());
                viewHolder.tvTime.setText(content.get(position).getTime());
                viewHolder.tvContent.setText(content.get(position).getContent());
                commentIds.add(content.get(position).getComment_id());
                JsonArray pics = content.get(position).getPics();
                if(pics.size()>0){
                    gridViewAdapter=new GridViewAdapter(pics);
                    viewHolder.gridView.setAdapter(gridViewAdapter);
                    viewHolder.gridView.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.gridView.setVisibility(View.GONE);
                }
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvTitle;
            TextView tvName;
            TextView tvTime;
            TextView tvContent;
            GridView gridView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isUpdate){
            isUpdate=false;
            AppUtil.getPpApiClient(ac).getMyActivityCommentsBean(ac.user_id, "1", "1000", this);
        }
    }
}
