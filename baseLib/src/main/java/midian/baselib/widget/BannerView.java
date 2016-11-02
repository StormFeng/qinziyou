package midian.baselib.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midian.baselib.R;

import java.util.ArrayList;
import java.util.List;

import midian.baselib.adapter.CsPagerTabAdapter;
import midian.baselib.app.AppContext;
import midian.baselib.utils.Func;
import midian.baselib.utils.UIHelper;

/**
 * 广告条
 * Created by MIDIAN on 2015/12/22.
 */
public class BannerView extends RelativeLayout {
    ViewPager pager;
    Context context;
    AppContext ac;
    int count;
    boolean isLoop = true;
    private ArrayList<View> list=new ArrayList<View>();
    List<Item> pics;
    BannerListner mBannerListner;
    BannerIndicator mBannerIndicator;
    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        this.context=context;
        ac = (AppContext) context.getApplicationContext();
        pager = new ViewPager(context);
        RelativeLayout.LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(pager, p);
        RelativeLayout.LayoutParams indicator = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicator.bottomMargin=dp2px(context,15);
        mBannerIndicator=new BannerIndicator(context);
        indicator.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicator.addRule(RelativeLayout.CENTER_HORIZONTAL);
        addView(mBannerIndicator, indicator);
//        List<String> listpic=new ArrayList<String>();
//        for(int i=0;i<10;i++)
//        listpic.add("第"+i);
//        setPics(listpic);
    }

    /**
     * 设置点击图片监听器
     *
     * @param mBannerListner
     */
    public void setBannerListner(BannerListner mBannerListner) {
        this.mBannerListner = mBannerListner;
    }

    /**
     * 把dp转为px
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    public void setPics(List<Item> pics) {
        this.pics = pics;
        count = pics.size();
        if (count <4&&count >1) {
            pics.addAll(pics);
        }
        for (Item name : pics) {
//            TextView textView=new TextView(context);
//            textView.setText(name);
//            textView.setTag(name);
//            textView.setBackgroundResource(R.drawable.bg_rectangle_green);
            ImageView img = new ImageView(context);
            ac.setImage(img, name.pic_url);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setBackgroundResource(R.drawable.icon_default);
            img.setTag(name.url);
            img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = (String) view.getTag();
//                    UIHelper.t(context,"url"+url);
                    if(mBannerListner!=null){
                        mBannerListner.onClickItem(url,pager.getCurrentItem()%count);
                    }
                }
            });
            list.add(img);
        }
        pager.setAdapter(new CsPagerTabAdapter(list, null));
        mBannerIndicator.initIndicator(getContext(),pager);
        if (pager.getAdapter() != null
                && pager.getAdapter().getCount() > 1) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    while (isLoop) {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // SystemClock.sleep(3000);
                        if (handler != null)
                            handler.sendEmptyMessage(0);
                    }
                }

            }).start();
        }
    }

    public void ondestry() {
        isLoop = false;
        handler = null;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 设置ViewPager的当前页面
                    if (pager.getAdapter() != null
                            && pager.getAdapter().getCount() > 1) {

                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    }

                    break;

                default:
                    break;
            }
        }
    };

    public interface BannerListner {
        public void onClickItem(String url, int postion);
    }

    public static class Item{
        public Item(String pic_url, String url) {
            this.pic_url = pic_url;
            this.url = url;
        }

        public String pic_url;
        public String url;
    }
    public class BannerIndicator extends LinearLayout {

        private int cResid = R.drawable.pager_indicator_dot_2;
        private int nResid = R.drawable.pager_indicator_dot_1;

        public ArrayList<ImageView> indicators = new ArrayList<ImageView>();

        public BannerIndicator(Context context) {
            super(context);
        }

        public BannerIndicator(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public void configIndicator(int cResid, int nResid) {
            this.cResid = cResid;
            this.nResid = nResid;
        }

        /**
         * 初始化indicator
         *
         * @param context
         * @param size     indicator个数
         * @param curIndex 当前显示位置
         */
        public void initIndicator(Context context, int size, int curIndex) {
            removeAll();
            indicators.clear();
            for (int i = 0; i < size; i++) {
                ImageView indicator = new ImageView(context);
                indicator.setPadding(10, 10, 10, 10);
                indicator.setImageResource(nResid);
                this.addView(indicator);
                indicators.add(indicator);
            }
            if (size == 0) {
                return;
            }
            indicators.get(0).setImageResource(cResid);
            changeIndiccator(curIndex);
        }

        public void initIndicator(Context context, ViewPager pager) {
            removeAll();
            indicators.clear();
            if (count == 1) {
                return;
            }
            for (int i = 0; i < count; i++) {
                ImageView indicator = new ImageView(context);
                int padding = Func.Dp2Px(getContext(), 3);
                indicator.setPadding(padding, padding, padding, padding);
                indicator.setImageResource(nResid);
                this.addView(indicator);
                indicators.add(indicator);
            }
            if (count > 0) {
                indicators.get(0).setImageResource(cResid);
            }
            changeIndiccator(pager.getCurrentItem());
            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int arg0) {
                    if(count!=0)
                        changeIndiccator(arg0%count);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }


        /**
         * 切换当前显示位置
         *
         * @param curIndex 要显示的位置
         */
        public void changeIndiccator(int curIndex) {
            for (int i = 0; i < indicators.size(); i++) {
                if (i == curIndex) {
                    indicators.get(i).setImageResource(cResid);
                } else {
                    indicators.get(i).setImageResource(nResid);
                }
            }
        }

        /**
         * 清空indicator
         */
        public void removeAll() {
            for (int i = 0; i < indicators.size(); i++) {
                this.removeView(indicators.get(i));
            }
        }

    }
}
