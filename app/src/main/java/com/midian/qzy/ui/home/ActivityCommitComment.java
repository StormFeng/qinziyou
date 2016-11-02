package com.midian.qzy.ui.home;

import android.Manifest;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.midian.qzy.R;
import com.midian.qzy.ui.MyCommentFragment;
import com.midian.qzy.utils.AppUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 添加评论页面
 * Created by Administrator on 2016/7/26 0026.
 */
public class ActivityCommitComment extends BaseActivity{
    private static final int REQUEST_IMAGE = 2;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA=1;
    private ArrayList<String> mSelectPath;
    private ArrayList<String> nSelectPath=new ArrayList<>();
    private BaseLibTopbarView topbar;
    private EditText et_content;
    private GridView gridView;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/midianbaselib/pic/";
    private Uri cropUri;
    private File protraitFile;
    private String protraitPath;
    private MyAdapter myAdapter;
    private List<File> pics=new ArrayList<>();
    private TextView tvCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitcomment);
        topbar=findView(R.id.topbar);
        et_content=findView(R.id.et_content);
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity)).setLeftText("返回",UIHelper.finish(_activity));
        topbar.setTitle("").setRightText("提交",this);
        tvCommit=findView(R.id.right_tv);
        findView(R.id.iv_cam).setOnClickListener(this);
        findView(R.id.iv_pic).setOnClickListener(this);
        gridView=findView(R.id.gridView);
        myAdapter=new MyAdapter();
        gridView.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return nSelectPath.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView= LayoutInflater.from(_activity).inflate(R.layout.item_image,null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
            ImageView iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
            Bitmap bitmap = Compressor.getDefault(_activity).compressToBitmap(new File(nSelectPath.get(position)));
            iv.setImageBitmap(bitmap);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putString("Uri",nSelectPath.get(position));
                    bundle.putString("flag","ActivityCommitComment");
                    UIHelper.jump(_activity,PhotoViewActivity.class,bundle);
//                    ArrayList<String> list=new ArrayList();
//                    list.add(nSelectPath.get(position));
//                    bundle.putStringArrayList("pic",list);
//                    UIHelper.jump(_activity,ViewPhotoActivity.class,bundle);
                }
            });
            iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nSelectPath.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath=data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                nSelectPath.addAll(mSelectPath);
                myAdapter.notifyDataSetChanged();
            }
        }else if(requestCode==REQUEST_CODE_CAPTURE_CAMEIA){
            if(resultCode == RESULT_OK){
                if(data==null) {
                    String uri = cropUri.toString().replace("file://","");
                    nSelectPath.add(uri);
                }
                myAdapter.notifyDataSetChanged();
            }
        }
//        Log.d("wqf",nSelectPath.get(0));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1001:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
                    startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
                }else{
                    UIHelper.t(_activity,"已禁止未来宝贝使用相机");
                }
                break;
            case 1002:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    MultiImageSelector selector = MultiImageSelector.create(_activity);
                    selector.showCamera(false);
                    selector.count(3 - gridView.getChildCount());
                    selector.start(_activity, REQUEST_IMAGE);
                }else{
                    UIHelper.t(_activity,"已禁止未来宝贝读取照片");
                }
                break;
            case 1003:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.CAMERA}, 1001);
                }else{
                UIHelper.t(_activity,"已禁止未来宝贝读取照片");
            }
        }
    }

    private void getImageFromCamera() {
        if(Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.d("wqf","ContextCompat.checkSelfPermission----------NO");
                if (ActivityCompat.shouldShowRequestPermissionRationale(_activity,
                        Manifest.permission.CAMERA)) {
                    UIHelper.t(_activity,"请在权限设置中允许未来宝贝使用相机");
                } else {
                    ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1003);
                }
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
            }
        }else{
            Log.d("wqf","ContextCompat.checkSelfPermission----------YES");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        }
    }
    // 拍照保存的绝对路径
    private Uri getCameraTempFile() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            UIHelper.t(this, getString(com.midian.baselib.R.string.submit_head_pic_error));
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
        return this.cropUri;
    }
    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        int id=arg0.getId();
        if(id==R.id.iv_cam){
            if (gridView.getChildCount() != 3) {
                getImageFromCamera();
            }else {
                UIHelper.t(_activity, "最多只能上传3张");
            }
        }else if(id==R.id.iv_pic) {
            if(Build.VERSION.SDK_INT >= 23){
                if (ContextCompat.checkSelfPermission(_activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("wqf","ContextCompat.checkSelfPermission----------NO");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(_activity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        UIHelper.t(_activity,"请在权限设置中允许未来宝贝读取照片");
                    } else {
                        ActivityCompat.requestPermissions(_activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
                    }
                } else {
                    if (gridView.getChildCount() != 3) {
                        MultiImageSelector selector = MultiImageSelector.create(_activity);
                        selector.showCamera(false);
                        selector.count(3 - gridView.getChildCount());
                        selector.start(_activity, REQUEST_IMAGE);
                    } else {
                        UIHelper.t(_activity, "最多只能上传3张");
                    }
                }
            }else{
                Log.d("wqf","ContextCompat.checkSelfPermission----------YES");
                if (gridView.getChildCount() != 3) {
                    MultiImageSelector selector = MultiImageSelector.create(_activity);
                    selector.showCamera(false);
                    selector.count(3 - gridView.getChildCount());
                    selector.start(_activity, REQUEST_IMAGE);
                } else {
                    UIHelper.t(_activity, "最多只能上传3张");
                }
            }
        }else if(id==com.midian.baselib.R.id.right_tv){
            tvCommit.setClickable(false);
            String str=et_content.getText().toString();
            if("".equals(str)){
                tvCommit.setClickable(true);
                UIHelper.t(_activity,"还没有填写评论内容哟");
                return;
            }
            File temp;
            File file;
            if(nSelectPath.size()>0){
                for(int i=0;i<nSelectPath.size();i++){
                    temp=new File(nSelectPath.get(i));
                    file = Compressor.getDefault(this).compressToFile(temp);
                    pics.add(file);
                    Log.d("wqf","PPPPPPPPPPPP");
                }
            }else{
                pics=null;
                Log.d("wqf","KKKKKKKKKKKKKKKK");
            }
            try {
                AppUtil.getPpApiClient(ac).addActivityComment(ac.user_id,CommentFragment.activity_id,str,pics,this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        if(res.isOK()){
            finish();
            MyCommentFragment.isUpdate=true;
            CommentFragment.isUpdate=true;
        }else{
            tvCommit.setClickable(true);
            ac.handleErrorCode(_activity,res.ret_info);
        }
    }
}
