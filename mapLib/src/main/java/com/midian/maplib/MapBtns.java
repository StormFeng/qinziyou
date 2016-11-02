package com.midian.maplib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by MIDIAN on 2015/12/17.
 */
public class MapBtns extends LinearLayout{
    View foot;
    public MapBtns(Context context) {
        super(context);
        init(context);
    }

    public MapBtns(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context){
        foot= LayoutInflater.from(context).inflate(R.layout.map_btns,null);
        addView(foot);
    }

}
