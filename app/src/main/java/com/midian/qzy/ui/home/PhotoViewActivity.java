package com.midian.qzy.ui.home;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.midian.qzy.R;

import java.io.File;

import id.zelory.compressor.Compressor;
import midian.baselib.base.BaseActivity;
import midian.baselib.utils.FDDataUtils;

public class PhotoViewActivity extends BaseActivity {

    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photoview);
        iv = (ImageView) findViewById(R.id.iv);
        if("CommentTpl".equals(mBundle.getString("flag"))){
            String url=mBundle.getString("url");
            ac.setImage(iv, FDDataUtils.getImageUrl(url,360,360));
        }else if("ActivityCommitComment".equals(mBundle.getString("flag"))){
            String uri=mBundle.getString("Uri");
            Log.d("wqf",uri);
            Bitmap bitmap = Compressor.getDefault(_activity).compressToBitmap(new File(uri));
            iv.setImageBitmap(bitmap);
        }
    }
}
