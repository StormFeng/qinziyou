package midian.baselib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midian.baselib.R;
import com.midian.maplib.AppConstant;

import java.security.KeyStore;

/**标题tab高48dp
 * Created by XuYang on 15/4/12.
 */
public class TopbarTabView extends LinearLayout {
    String[] tabs;
    private int tabPadding = 24;
    private onTabChangeListener onTabChangeListener;
    private int curIndex = 0;
    private LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private int tabTextSize = 15;
    private int tabTextColor = 0xffffffff;
    private int tabTextselectColor = 0xFF47a0db;
    // 分隔线颜色
    private int dividerColor = 0xffffffff;
    private Paint dividerPaint;
    private int dividerPadding = 12;
    private int topPadding = 2;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;
    private int tabCount = 0;

    public interface onTabChangeListener {
        void onTabChange(int index);
    }

    public TopbarTabView(Context context) {
        super(context);
        init(context);
    }

    public TopbarTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        dividerPaint = new Paint(dividerColor);
        dividerPadding = dp2px(context, 12);
        topPadding = dp2px(context, 2);

        defaultTabLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1.0f);
        defaultTabLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        setPadding(0, topPadding, 0, topPadding);
        tabs = getResources().getStringArray(R.array.select_filter_btns);
        tabCount = tabs.length;
        this.removeAllViews();
        View left = new View(getContext());
        addView(left, expandedTabLayoutParams);
        for (int i = 0; i < tabs.length; i++)
            addTextTab(i, tabs[i]);
        View right = new View(getContext());
        addView(right, expandedTabLayoutParams);
        setCurIndex(curIndex);
    }

    public void initTab(String [] tabStrs){
        tabs = tabStrs;
        tabCount = tabs.length;
        if(tabCount<2)return;
        this.removeAllViews();
        View left = new View(getContext());
        addView(left, expandedTabLayoutParams);
        for (int i = 0; i < tabs.length; i++)
            addTextTab(i, tabs[i]);
        View right = new View(getContext());
        addView(right, expandedTabLayoutParams);
        setCurIndex(curIndex);
    }


    private void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
        addTab(position, tab);
    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);

        addTab(position, tab);

    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurIndex(position);
            }
        });
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        addView(tab,defaultTabLayoutParams);
    }

    public TopbarTabView.onTabChangeListener getOnTabChangeListener() {
        return onTabChangeListener;
    }

    public void setOnTabChangeListener(
            TopbarTabView.onTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(int i) {


        changeState(i);
        if (onTabChangeListener != null) {
            onTabChangeListener.onTabChange(i);
        }
        this.curIndex = i;
    }

    public void changeState(int position) {
        for (int i = 0; i < tabCount; i++) {

            View v = getChildAt(i+1);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                if (i == position ) {
                    tab.setTextColor(tabTextselectColor);
                    setTabBackgroud(tab,i==0,i==tabCount-1,true);
                } else {
                    tab.setTextColor(tabTextColor);
                    setTabBackgroud(tab,i==0,i==tabCount-1,false);
                }

            }
        }
    }

    /**
     * 主动修改状态
     * @param position
     *
     */
    public void setchangeState(int position) {
        this.curIndex = position;
        for (int i = 0; i < tabCount; i++) {

            View v = getChildAt(i+1);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                if (i == position ) {
                    tab.setTextColor(tabTextselectColor);
                    setTabBackgroud(tab,i==0,i==tabCount-1,true);

                } else {
                    tab.setTextColor(tabTextColor);
                    setTabBackgroud(tab,i==0,i==tabCount-1,false);
                }

            }
        }
    }

    private void setTabBackgroud(TextView v,boolean isLeft,boolean isRight,boolean isSelect){
        float[] outerRadii ={0,0,0,0,0,0,0,0};//外矩形 左上、右上、右下、左下 圆角半径
        RectF rectF=new RectF();
        int r=dp2px(getContext(),22);
        int d=dp2px(getContext(),1);
        if(isSelect){
            rectF=null;
        }else{
            rectF.left= d;
            rectF.top= d;
            if(isRight){
                rectF.right=d;
            }else{
                rectF.right= 0;
            }
            rectF.bottom= d;
        }
        if(isLeft){
            outerRadii[0]=r;
            outerRadii[1]=r;
            outerRadii[6]=r;
            outerRadii[7]=r;
        }else if(isRight){
            outerRadii[2]=r;
            outerRadii[3]=r;
            outerRadii[4]=r;
            outerRadii[5]=r;
        }
        RoundRectShape roundRectShape=new RoundRectShape(outerRadii,rectF,outerRadii);
        ShapeDrawable mShapeDrawable=new ShapeDrawable(roundRectShape);
        mShapeDrawable.getPaint().setColor(dividerColor);
        mShapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        mShapeDrawable.getPaint().setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //Android系统大于等于API16，使用setBackground
            v.setBackground(mShapeDrawable);
        } else {
            //Android系统小于API16，使用setBackgroundDrawable
            v.setBackgroundDrawable(mShapeDrawable);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    /**
     * 把dp转为px
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
