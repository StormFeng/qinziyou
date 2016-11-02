package com.midian.qzy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.midian.qzy.R;

import midian.baselib.base.BaseActivity;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 添加备注页面
 * Created by Administrator on 2016/7/26 0026.
 */
public class ActivityCommitNote extends BaseActivity{
    private BaseLibTopbarView topbar;
    private EditText et_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitnote);
        topbar=findView(R.id.topbar);
        et_content=findView(R.id.et_content);
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity)).setLeftText("返回",UIHelper.finish(_activity));
        topbar.setTitle("").setRightText("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                String str=et_content.getText().toString();
                intent.putExtra("content",str);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
