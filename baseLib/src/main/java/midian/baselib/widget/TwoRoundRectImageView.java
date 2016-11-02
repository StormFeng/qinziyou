package midian.baselib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import midian.baselib.utils.BitmapUtils;
import midian.baselib.utils.ImageUtils;

/**两种大小圆角ImageView
 * Created by moshouguan on 2015/12/23.
 */
public class TwoRoundRectImageView extends ImageView {
    private Paint paint;
    public TwoRoundRectImageView(Context context) {
        super(context);
        paint=new Paint();
    }

    public TwoRoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
    }

    public TwoRoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable=getDrawable();
        if(drawable!=null){
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bitmap= ImageUtils.zoomBitmap(bitmap,getWidth(),getHeight());
            Bitmap b = BitmapUtils.getTwoRoundedCornerBitmap(bitmap, getWidth()/10);
            Rect scr=new Rect(0,0,b.getWidth(),b.getHeight());
            Rect rectDest=new Rect(0,0,getWidth(),getHeight());
            canvas.drawBitmap(b,scr,rectDest,paint);
        }else{
            super.onDraw(canvas);
        }
    }

}
