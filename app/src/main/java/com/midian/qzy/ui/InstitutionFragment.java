package com.midian.qzy.ui;

import android.Manifest;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.midian.qzy.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import midian.baselib.base.BaseFragment;
import midian.baselib.utils.FileUtils;
import midian.baselib.utils.ImageUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.dialog.PicChooserDialog;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class InstitutionFragment extends BaseFragment implements View.OnClickListener {

    public static EditText etName;
    public static EditText etContactPerson;
    public static EditText etContactNum;
    public static EditText etContactAddress;
    public static EditText etNote;
    private ImageView ivPic;
    private View rlPic;
    private TextView tvPic;
    public static File mFile;

    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/midianbaselib/pic/";
    private final static int CROP = 200;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_institution, null);
        etName= (EditText) v.findViewById(R.id.et_Name);
        etContactPerson= (EditText) v.findViewById(R.id.et_ContactPerson);
        etContactNum= (EditText) v.findViewById(R.id.et_ContactNum);
        etContactAddress= (EditText) v.findViewById(R.id.et_ContactAddress);
        etNote= (EditText) v.findViewById(R.id.et_Note);
        tvPic= (TextView) v.findViewById(R.id.tv_Pic);
        rlPic=v.findViewById(R.id.rl_Pic);
        ivPic= (ImageView) v.findViewById(R.id.iv_Pic);
        rlPic.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        takePhoto();
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
                    startImagePick();
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
                if(Build.VERSION.SDK_INT >= 23){
                    if (ContextCompat.checkSelfPermission(_activity,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("wqf","ContextCompat.checkSelfPermission----------NO");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(_activity,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            UIHelper.t(_activity,"请在权限设置中允许未来宝贝读取权限");
                        } else {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
//                            ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
                        }
                    } else {
                        startImagePick();
                    }
                }else{
                    startImagePick();
                }
//                startImagePick();
            }

            @Override
            public void onClickFromCamera(View view) {
//                startActionCamera();
                if(Build.VERSION.SDK_INT >= 23){
                    if (ContextCompat.checkSelfPermission(_activity,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("wqf","ContextCompat.checkSelfPermission----------NO");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(_activity,
                                Manifest.permission.CAMERA)) {
                            UIHelper.t(_activity,"请在权限设置中允许未来宝贝使用相机");
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1001);
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
            Bitmap bitmap;
            switch (requestCode) {
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
//                    startActionCrop(origUri);// 拍照后裁剪
                    try {
                        Log.d("wqf","try");
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), origUri);
                        outputBitmap(bitmap, ImageUtils.getAbsoluteImagePath(_activity,origUri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
//                    startActionCrop(data.getData());// 选图后裁剪
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        outputBitmap(bitmap, ImageUtils.getAbsoluteImagePath(_activity,data.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
//                    // 获取头像缩略图
//                    if (!TextUtils.isEmpty(protraitPath) && protraitFile.exists()) {
//                        protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath,
//                                400, 200);
//                        outputBitmap(protraitBitmap, protraitPath);
//                    }
//                    break;
            }
        }
    }

    public void outputBitmap(Bitmap bitmap, String path) {

        Log.d("wqf","outputBitmap");
        ivPic.setImageBitmap(bitmap);
        tvPic.setVisibility(View.GONE);
        mFile = new File(path);
    }
}
