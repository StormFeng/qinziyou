package com.midian.qzy.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midian.login.view.LoginActivity;
import com.midian.qzy.R;
import com.midian.qzy.bean.UserDetailBean;
import com.midian.qzy.ui.myaccount.ChangeNameActivity;
import com.midian.qzy.ui.myaccount.ChangePasswordActivity;
import com.midian.qzy.ui.myaccount.ChooseSexActivity;
import com.midian.qzy.utils.AppUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.api.ApiCallback;
import midian.baselib.base.BaseFragment;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.FileUtils;
import midian.baselib.utils.ImageUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.CircleImageView;
import midian.baselib.widget.dialog.PicChooserDialog;

import static android.app.Activity.RESULT_OK;

/**
 * 个人资料
 * Created by Administrator on 2016/7/18 0018.
 */
public class MyAccountFragment extends BaseFragment implements View.OnClickListener, ApiCallback {
    @BindView(R.id.iv_Head)
    CircleImageView ivHead;
    @BindView(R.id.tv_Name)
    TextView tvName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_Sex)
    TextView tvSex;
    @BindView(R.id.ll_sex)
    LinearLayout llSex;
    @BindView(R.id.tv_Number)
    TextView tvNumber;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.ll_image)
    LinearLayout llImage;
    @BindView(R.id.v_Line_2)
    View vLine2;
    @BindView(R.id.v_Line_1)
    View vLine1;
    @BindView(R.id.btn_Cancel)
    Button btnCancel;

    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/midianbaselib/pic/";
    private final static int CROP = 200;
    private File mhead = null;
    private final int CHANGE_NAME = 2001;
    private final int CHANGE_SEX = 2002;
    private String name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, null);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick({R.id.ll_name, R.id.ll_sex, R.id.ll_phone, R.id.ll_password, R.id.ll_image, R.id.ll_Sex, R.id.btn_Cancel })
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_name:
//                bundle.putString("type", "name");
                intent.putExtra("type", "name");
                intent.setClass(_activity, ChangeNameActivity.class);
                this.startActivityForResult(intent, CHANGE_NAME);
                break;
            case R.id.ll_phone:
                bundle.putString("type", "phone");
                UIHelper.jump(_activity, ChangeNameActivity.class, bundle);
                break;
            case R.id.ll_password:
                UIHelper.jump(_activity, ChangePasswordActivity.class);
                break;
            case R.id.ll_image:
                takePhoto();
                break;
            case R.id.ll_Sex:
                intent.setClass(_activity, ChooseSexActivity.class);
                this.startActivityForResult(intent, CHANGE_SEX);
                break;
            case R.id.btn_Cancel:
                if("立即登录".equals(btnCancel.getText().toString().trim())){
//                    UIHelper.jumpForResult(_activity,LoginActivity.class,6001);
                    this.startActivityForResult(new Intent(_activity,LoginActivity.class),6001);
                }else{
                    final Dialog dialog=new Dialog(_activity,R.style.add_dialog);
                    Window window=dialog.getWindow();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_logout);
                    dialog.show();
                    window.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    window.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ac.clearUserInfo();
                            btnCancel.setText("立即登录");
                            btnCancel.setBackgroundColor(getResources().getColor(R.color.blue_qzy));
                            dialog.dismiss();
                            new Thread () {
                                public void run () {
                                    try {
                                        Log.d("wqf","模拟返回");
                                        Instrumentation inst= new Instrumentation();
                                        inst.sendKeyDownUpSync(KeyEvent. KEYCODE_BACK);
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActionCamera();
                } else {
                    UIHelper.t(_activity, "已禁止未来宝贝使用相机");
                }
            case 1002:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1001);
                    ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.CAMERA}, 1001);
                }else{
                    UIHelper.t(_activity,"已禁止未来宝贝读取照片");
                }
                break;
        }
    }

    public void takePhoto() {
        PicChooserDialog dialog = new PicChooserDialog(_activity,
                com.midian.baselib.R.style.bottom_dialog);
        dialog.setListner(new PicChooserDialog.OnPicChooserListener() {
            @Override
            public void onClickFromGallery(View view) {
                startImagePick();
            }

            @Override
            public void onClickFromCamera(View view) {

                if(Build.VERSION.SDK_INT >= 23){
                    if (ContextCompat.checkSelfPermission(_activity,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("wqf","ContextCompat.checkSelfPermission----------NO");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(_activity,
                                Manifest.permission.CAMERA)) {
                            UIHelper.t(_activity,"请在权限设置中允许未来宝贝使用相机");
                        } else {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
//                            ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
                        }
                    } else {
                        startActionCamera();
                    }
                }else{
                    startActionCamera();
                }
            }
        });
        dialog.show();
    }

    public void startImagePick() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(
                Intent.createChooser(intent, getString(com.midian.baselib.R.string.select_pic)),
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
    }

    private void startActionCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
        this.startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    private Uri getCameraTempFile() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            UIHelper.t(_activity, getString(com.midian.baselib.R.string.submit_head_pic_error));
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        // 照片命名
        String cropFileName = "osc_camera_" + timeStamp + ".jpg";
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);
        cropUri = Uri.fromFile(protraitFile);
        this.origUri = this.cropUri;
        return this.cropUri;
    }

    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            UIHelper.t(_activity, getString(com.midian.baselib.R.string.submit_head_pic_error));
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (!TextUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(_activity, uri);
        }
        String ext = FileUtils.getFileFormat(thePath);
        ext = TextUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "osc_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                    startActionCrop(origUri);// 拍照后裁剪
                    break;
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                    startActionCrop(data.getData());// 选图后裁剪
                    break;
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                    // 获取头像缩略图
                    if (!TextUtils.isEmpty(protraitPath) && protraitFile.exists()) {
                        protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath,
                                200, 200);
                        outputBitmap(protraitBitmap, protraitPath);
                    }
                    break;
                case CHANGE_NAME:
//                    name = data.getStringExtra("name");
                    tvName.setText(ac.getProperty("name"));
                    break;
                case CHANGE_SEX:
                    String sex = data.getStringExtra("sex");
                    if ("1".equals(sex)) {
                        tvSex.setText("男");
                    } else {
                        tvSex.setText("女");
                    }
                    break;
                case 6001:
                    btnCancel.setText("注销");
                    btnCancel.setBackgroundColor(getResources().getColor(R.color.red));
                    break;
            }
        }
    }

    public void outputBitmap(Bitmap bitmap, String path) {
//        ivHead.setImageBitmap(bitmap);
        Log.d("wqf", path);
        mhead = new File(path);
        try {
            AppUtil.getPpApiClient(ac).updateUser(ac.user_id, mhead, null, null, this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtil.getPpApiClient(ac).userDetail(ac.user_id, this);
//        Log.e("wqf","onResume");
//        Log.e("wqf",ac.isUserIdExsit()+"");
        if(!ac.isUserIdExsit()){
            btnCancel.setText("立即登录");
            btnCancel.setBackgroundResource(R.drawable.radius_bg_light_bluesolid);
        }else{
            btnCancel.setText("注销");
            btnCancel.setBackgroundResource(R.drawable.radius_bg_redsolid);
        }
    }


    @Override
    public void onApiStart(String tag) {

    }

    @Override
    public void onApiLoading(long count, long current, String tag) {

    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        if (res.isOK()) {
            if ("userDetail".equals(tag)) {
                UserDetailBean bean = (UserDetailBean) res;
                tvName.setText(bean.getContent().getName());
                tvNumber.setText(bean.getContent().getPhone());
                try {
                    ac.setImage(ivHead, FDDataUtils.getImageUrl(bean.getContent().getHead_pic(), 100, 100));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ("1".equals(bean.getContent().getSex())) {
                    tvSex.setText("男");
                } else {
                    tvSex.setText("女");
                }
                if (!"1".equals(bean.getContent().getUser_from())) {
                    llPassword.setVisibility(View.GONE);
                    llPhone.setVisibility(View.GONE);
                    vLine1.setVisibility(View.GONE);
                    vLine2.setVisibility(View.GONE);
                } else {
                    llPassword.setVisibility(View.VISIBLE);
                    llPhone.setVisibility(View.VISIBLE);
                    vLine1.setVisibility(View.VISIBLE);
                    vLine2.setVisibility(View.VISIBLE);
                }
            }
            if ("updateUser".equals(tag)) {
                if (protraitBitmap != null) {
                    ivHead.setImageBitmap(protraitBitmap);
                }
                if (!"".equals(name)) {
                    tvName.setText(name);
                }
                UIHelper.t(_activity, res.ret_info);
            }
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {

    }

    @Override
    public void onParseError(String tag) {

    }
}
