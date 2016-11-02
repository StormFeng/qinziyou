
package com.midian.qzy.wxapi;

import android.util.Log;

import com.midian.qzy.ui.home.ActivityPay_Y;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import midian.baselib.utils.UIHelper;

public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        Log.d("wqf","onResp");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.d("wqf","onPayFinish,errCode="+resp.errCode);
//            UIHelper.jump(WXEntryActivity.this, ActivityPay_Y.class);
        }
    }
}
