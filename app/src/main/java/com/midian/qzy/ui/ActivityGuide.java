package com.midian.qzy.ui;


import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.midian.qzy.R;

import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;


/**
 * 引导页
 *
 * @author MIDIAN
 */
public class ActivityGuide extends BaseActivity {

    private int[] mViewPagerData = new int[]{R.drawable.icon_guide1,
            R.drawable.icon_guide2, R.drawable.icon_guide3,R.drawable.icon_guide4};
    private ViewPager viewPager;
    private float downX;
    private int neddMoveX = 80;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        setContentView(R.layout.activity_guide);
        viewPager = findView(R.id.viewPager);
        viewPager.setAdapter(new CustomPagerAdapter());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (downX - ev.getX() > neddMoveX
                    && viewPager.getCurrentItem() == mViewPagerData.length - 1) {

            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private class CustomPagerAdapter extends PagerAdapter {
        private RelativeLayout[] views;
        private int mCount;

        public CustomPagerAdapter() {
            mCount = mViewPagerData.length;
            views = new RelativeLayout[mCount];
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView(views[position % mCount]);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewGroup) container).removeView(views[position % mCount]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RelativeLayout itemView = null;
            if (views[position % mCount] == null
                    || views[position % mCount].isShown()) {
                itemView = (RelativeLayout) LayoutInflater.from(_activity)
                        .inflate(R.layout.item_guide, null);
                LayoutParams vp = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                itemView.setLayoutParams(vp);

            } else {
                itemView = views[position % mCount];
            }
            ImageView mIv = (ImageView) itemView.findViewById(R.id.imageView);
            mIv.setImageResource(mViewPagerData[position]);

            Button skipBtn = (Button) itemView.findViewById(R.id.skipBtn);

            skipBtn.setTag(position);
            skipBtn.setOnClickListener(onClickListener);
            Button entryBtn = (Button) itemView.findViewById(R.id.entryBtn);

            entryBtn.setTag(position);
            entryBtn.setOnClickListener(onClickListener);
//            entryBtn.setImageResource(R.drawable.bg_2radiu_grey);
            if (position == mCount - 1) {
                skipBtn.setVisibility(View.GONE);
                entryBtn.setVisibility(View.VISIBLE);
            } else {
                skipBtn.setVisibility(View.GONE);
                entryBtn.setVisibility(View.GONE);
            }

            views[position % mCount] = itemView;
            container.addView(views[position % mCount]);
            return views[position % mCount];
        }

        private OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Integer) {
                    UIHelper.jump(_activity, MainActivity.class);
                    finish();
                }
            }
        };

    }

}
