package midian.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by moshouguan on 2016/1/25.
 */
public class BaseObserverView extends LinearLayout {

    ViewTreeObserver vto;
    SctrollListener mSctrollListener;
int statusBarHeight=0;
    public BaseObserverView(Context context) {
        super(context);
        init(context);
    }

    public BaseObserverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        statusBarHeight=getStatusBarHeight();
        vto=getViewTreeObserver();
        vto.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int[] location = new int[2];
                getLocationOnScreen(location);
//                getLocationOnScreen(location);
                if (mSctrollListener != null) {
                    mSctrollListener.onScrollChanged(location[1]-statusBarHeight);
                }
            }
        });
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public void setSctrollListener(SctrollListener mSctrollListener){
        this.mSctrollListener=mSctrollListener;
    }

  public  interface SctrollListener {
        public void onScrollChanged(int y);
    }

}
