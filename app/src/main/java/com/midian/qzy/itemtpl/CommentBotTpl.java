package com.midian.qzy.itemtpl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midian.login.view.LoginActivity;
import com.midian.qzy.R;
import com.midian.qzy.ui.home.ActivityCommitComment;
import com.midian.qzy.ui.home.ActivityCommitNote;
import com.midian.qzy.ui.home.CommentFragment;

import midian.baselib.bean.NetResult;
import midian.baselib.utils.UIHelper;
import midian.baselib.view.BaseTpl;

public class CommentBotTpl extends BaseTpl<NetResult> implements View.OnClickListener {

    public static LinearLayout ll_Comment;
    public CommentBotTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentBotTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        root.setOnClickListener(this);
        ll_Comment=findView(R.id.ll_Comment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_comment_bot;
    }

    @Override
    public void setBean(NetResult bean, int position) {
    }

    @Override
    public void onClick(View v) {
        if(!ac.isUserIdExsit()){
            UIHelper.jump(_activity, LoginActivity.class);
            return;
        }
        UIHelper.jump(_activity, ActivityCommitComment.class);
    }
}
