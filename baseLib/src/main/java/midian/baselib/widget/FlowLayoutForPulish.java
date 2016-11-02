package midian.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class FlowLayoutForPulish extends RadioGroup {

    private static final String TAG = "FlowLayout";

    public int maxColumnCount = 4;

    public FlowLayoutForPulish(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(15, 15, 15, 15);
        return params;
    }

    public int getMaxColumnCount() {
        return maxColumnCount;
    }

    public void setMaxColumnCount(int maxColumnCount) {
        this.maxColumnCount = Math.max(1, maxColumnCount);
    }

    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

//        Log.e(TAG, sizeWidth + "," + sizeHeight);

        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        int cCount = getChildCount();
        int childWidth = 0;

        if (maxColumnCount > 0) {
            childWidth = (sizeWidth) / maxColumnCount;
            childWidth += (getPaddingLeft() + getPaddingRight()) * (maxColumnCount - 1) / maxColumnCount;
            int low = cCount / maxColumnCount + (cCount % maxColumnCount > 0 ? 1 : 0);
            height = low * childWidth;
            height -= (getPaddingBottom() + getPaddingTop()) * (low - 1);
        }

        //  遍历每个子元素
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

    }

    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        int cCount = getChildCount();
        int childWidth = 0;
        int childHeight = childWidth;

//        l+= getLeftPaddingOffset();
//        t+= getTopPaddingOffset();
//        r-= getPaddingRight();
//        b-= getPaddingBottom();

        // 遍历所有的孩子
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            childWidth = child.getMeasuredWidth();
            childHeight = childWidth;
            // 如果已经需要换行
            if (childWidth + lineWidth + getPaddingLeft() + getPaddingTop() > width) {
                // 记录这一行所有的View以及最大高度
                mLineHeight.add(childWidth);
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                lineWidth = 0;// 重置行宽
                lineViews = new ArrayList<View>();
            }
            /**
             * 如果不需要换行，则累加
             */
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);
        }
        // 记录最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int left = getPaddingLeft();
        int top = getPaddingTop();
        // 得到总行数
        int lineNums = mAllViews.size();
        for (int i = 0; i < lineNums; i++) {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);

//            Log.e(TAG, "第" + i + "行 ：" + lineViews.size() + " , " + lineViews);
//            Log.e(TAG, "第" + i + "行， ：" + lineHeight);

            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth();
                childHeight = childWidth;
                //计算childView的left,top,right,bottom
                int lc = left;
                int tc = top;
                int rc = lc + childHeight;
                int bc = tc + childHeight;

//                Log.e(TAG, child + " , l = " + lc + " , t = " + t + " , r ="+ rc + " , b = " + bc);

                child.layout(lc, tc, rc, bc);

                left += childHeight;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }

    }
}
