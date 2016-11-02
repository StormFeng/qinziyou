package midian.baselib.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * 绘制drawable 工具类
 * Created by moshouguan on 2015/12/23.
 */
public class DrawableUtils {

    public static ShapeDrawable createShapDrawable(int color) {
        float[] outerRadii ={0,0,0,0,0,0,0,0};//外矩形 左上、右上、右下、左下 圆角半径
        RectF rectF=new RectF();
        RoundRectShape roundRectShape=new RoundRectShape(outerRadii,rectF,outerRadii);

        ShapeDrawable mShapeDrawable=new ShapeDrawable(roundRectShape);
        mShapeDrawable.getPaint().setColor(color);
        mShapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        mShapeDrawable.getPaint().setAntiAlias(true);


        return mShapeDrawable;
    }
    /**
     * 把dp转为px
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
