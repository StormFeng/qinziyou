package com.midian.qzy.app;

import android.graphics.Bitmap;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.baidu.mapapi.SDKInitializer;
import com.midian.login.utils.LoginLibHelp;
import com.midian.qzy.api.QzyApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import midian.baselib.app.AppContext;
import midian.baselib.utils.FDBitmapUtil;
import midian.baselib.utils.FDFileUtil;

/**
 * Created by XuYang on 15/4/13.
 */
public class MAppContext extends AppContext {

    private static MAppContext mInstance;

    public static MAppContext getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);//初始化百度MapLib
        LoginLibHelp.init(this);//初始化登陆接口
        QzyApiClient.init(this);   //初始化接口
        //最好放到 Application oncreate执行
//        initImagePicker();//初始化imagePicker
    }

//    private void initImagePicker() {
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
//        imagePicker.setShowCamera(true);                      //显示拍照按钮
//        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
////        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
//    }

    /**
     * 启动推送
     */
    public void startPush() {
        System.out.println("startWork---启动推送");
        PushSettings.enableDebugMode(getApplicationContext(),true);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "E1ntuF04XM7ThBXXsKnlY6oPVjGRIQzj");
    }

    public void changePush() {
        if (isClosePush) {
            stopPush();
        } else {
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "E1ntuF04XM7ThBXXsKnlY6oPVjGRIQzj");
        }
    }

    /**
     * 停用推送
     */
    public void stopPush() {
        System.out.println("stopPush");
        PushManager.stopWork(getApplicationContext());
    }

    /**
     * 压缩聊天图片
     *
     * @param picFileName
     * @return
     */
    public String compressImage(String picFileName) {
        Bitmap image = FDBitmapUtil.decodeFile(picFileName, 1000);
        FDFileUtil.deleteFile(new File(picFileName), this);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        float options = 90;
        while (baos.toByteArray().length / 1024 > 200 && options > 10) { // 循环判断如果压缩后图片
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, (int) options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10f;// 每次都减少10
        }
        System.out.println("压缩后大小 " + (baos.toByteArray().length / 1024));

        // 保存新文件
        try {
            FileOutputStream out = new FileOutputStream(new File(picFileName));
            out.write(baos.toByteArray());
            out.flush();
            out.close();
            return picFileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


}
