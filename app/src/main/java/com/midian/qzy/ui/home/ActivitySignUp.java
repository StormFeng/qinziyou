package com.midian.qzy.ui.home;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midian.qzy.R;
import com.midian.qzy.alipay.AliPay;
import com.midian.qzy.app.Constant;
import com.midian.qzy.bean.JoinActivityBean;
import com.midian.qzy.utils.AppUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midian.baselib.base.BaseActivity;
import midian.baselib.bean.NetResult;
import midian.baselib.utils.FDDataUtils;
import midian.baselib.utils.UIHelper;
import midian.baselib.widget.BaseLibTopbarView;

/**
 * 活动报名页面
 */
public class ActivitySignUp extends BaseActivity implements TextWatcher {
    @BindView(R.id.topbar)
    BaseLibTopbarView topbar;
    @BindView(R.id.et_ContactPerson)
    EditText etContactPerson;
    @BindView(R.id.et_ContactNum)
    EditText etContactNum;
    @BindView(R.id.btn_Reduce_1)
    Button btnReduce1;
    @BindView(R.id.btn_Add_1)
    Button btnAdd1;
    @BindView(R.id.btn_Reduce_2)
    Button btnReduce2;
    @BindView(R.id.btn_Add_2)
    Button btnAdd2;
    @BindView(R.id.tv_AllMoney)
    TextView tvAllMoney;
    @BindView(R.id.tv_Note)
    TextView tvNote;
    @BindView(R.id.tv_ZhiFuBao)
    TextView tvZhiFuBao;
    @BindView(R.id.tv_WeiXin)
    TextView tvWeiXin;
    @BindView(R.id.btn_Pay)
    Button btnPay;
    @BindView(R.id.et_Old)
    EditText etOld;
    @BindView(R.id.et_Child)
    EditText etChild;
    @BindView(R.id.tv_OldPrice)
    TextView tvOldPrice;
    @BindView(R.id.tv_ChildPrice)
    TextView tvChildPrice;
    @BindView(R.id.ll_Pay)
    LinearLayout llPay;

    private String pay_way = "alipay";
    private String activity_id;
    private String OldPrice;
    private String ChildPrice;
    private String text;
    private static final String APP_ID = com.midian.UMengUtils.Constant.weixinAppId;
    private IWXAPI api;
    public static int flag = 0;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        OldPrice = mBundle.getString("OldPrice");
        ChildPrice = mBundle.getString("ChildPrice");
        activity_id = mBundle.getString("activity_id");
        text = mBundle.getString("text");
        btnPay.setText(text);
        if ("免费参加".equals(text)) {
            llPay.setVisibility(View.GONE);
        }
        tvOldPrice.setText(OldPrice + "元");
        tvChildPrice.setText(ChildPrice + "元");
        tvAllMoney.setText(Double.parseDouble(OldPrice) + Double.parseDouble(ChildPrice) + "");
        topbar.setLeftImageButton(R.drawable.icon_back, UIHelper.finish(_activity));
        topbar.setTitle("报名").setLeftText("返回", UIHelper.finish(_activity));
        etOld.addTextChangedListener(this);
        etChild.addTextChangedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tvNote.setText(data.getStringExtra("content"));
        }
    }

    @OnClick({R.id.btn_Reduce_1, R.id.btn_Add_1, R.id.btn_Reduce_2, R.id.btn_Add_2, R.id.btn_Pay, R.id.tv_ZhiFuBao, R.id.tv_WeiXin, R.id.tv_Note})
    public void onClick(View view) {
        int n, m;
        if ("".equals(etOld.getText().toString())) {
            n = 0;
        } else {
            n = Integer.valueOf(etOld.getText().toString());
        }
        if ("".equals(etChild.getText().toString())) {
            m = 0;
        } else {
            m = Integer.valueOf(etChild.getText().toString());
        }
        switch (view.getId()) {
            case R.id.btn_Reduce_1:
                if (n == 0) {
                    return;
                } else {
                    n--;
                }
                etOld.setText(n + "");
                break;
            case R.id.btn_Add_1:
                n++;
                etOld.setText(n + "");
                break;
            case R.id.btn_Reduce_2:
                if (m == 0) {
                    return;
                } else {
                    m--;
                }
                etChild.setText(m + "");
                break;
            case R.id.btn_Add_2:
                m++;
                etChild.setText(m + "");
                break;
            case R.id.btn_Pay:
                isFirst = false;
                String adult_count = etOld.getText().toString();
                String child_count = etChild.getText().toString();
                String contact_person = etContactPerson.getText().toString();
                String contact_num = etContactNum.getText().toString();
                if ("".equals(contact_person) || "".equals(contact_num)) {
                    UIHelper.t(_activity, "请补充完整报名信息");
                    return;
                }
                if (!FDDataUtils.isMobile(contact_num)) {
                    UIHelper.t(_activity, "请填写正确的电话号码");
                    return;
                }
                if ("0.0".equals(tvAllMoney.getText().toString().trim())) {
                    pay_way = null;
                }
                api = WXAPIFactory.createWXAPI(_activity, APP_ID, true);
                api.registerApp(APP_ID);
                AppUtil.getPpApiClient(ac).joinActivity(ac.user_id, activity_id, adult_count, child_count, contact_person, contact_num, pay_way, this);
                btnPay.setClickable(false);
                break;
            case R.id.tv_ZhiFuBao:
                setButton(R.id.tv_ZhiFuBao);
                break;
            case R.id.tv_WeiXin:
                setButton(R.id.tv_WeiXin);
                break;
            case R.id.tv_Note:
                UIHelper.jumpForResult(_activity, ActivityCommitNote.class, 1001);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("+++++++++++++++++++++++++++  " + isFirst);
        if (!isFirst) {
            btnPay.setClickable(true);
            isFirst = false;
            if (flag == 1) {
                Log.d("wqf", "判断支付flag");
                flag = 0;
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onApiSuccess(NetResult res, String tag) {
        super.onApiSuccess(res, tag);
        if (res.isOK()) {
            JoinActivityBean bean = (JoinActivityBean) res;
            if ("alipay".equals(bean.getContent().getPay_way())) {
                String seller_id = bean.getContent().getAlipay_param().getReceive_account();// 商户收款账号
                String app_id = bean.getContent().getAlipay_param().getMerchant_no();
                String rsaKey = bean.getContent().getAlipay_param().getPrivate_key();// 商户私钥，pkcs8格式
                String pay_price = bean.getContent().getAlipay_param().getPay_price();//价格
                String pay_desc = bean.getContent().getAlipay_param().getPay_desc();//描述
                String pay_title = bean.getContent().getAlipay_param().getPay_title();//标题
                String order_sn = bean.getContent().getOrder_sn();//订单号
                String notify_url = Constant.BBASEURL + bean.getContent().getAlipay_param().getCallback_url();//回调地址

                String orderInfo = AliPay.getOrderInfo(app_id, seller_id, order_sn, pay_desc, pay_title, pay_price, notify_url);
                String sign = AliPay.sign(orderInfo, rsaKey);
                new AliPay(_activity).pay(sign, orderInfo);
            } else if ("weixin".equals(bean.getContent().getPay_way())) {
//                IWXAPI api= WXAPIFactory.createWXAPI(_activity, APP_ID, true);
//                api.registerApp(APP_ID);
                PayReq request = new PayReq();
                request.appId = bean.getContent().getWeixin_param().getAppid();
                request.partnerId = bean.getContent().getWeixin_param().getPartnerid();
                request.prepayId = bean.getContent().getWeixin_param().getPrepayid();
                request.packageValue = bean.getContent().getWeixin_param().getPkg();
                request.nonceStr = bean.getContent().getWeixin_param().getNoncestr();
                request.timeStamp = bean.getContent().getWeixin_param().getTimestamp();
                request.sign = bean.getContent().getWeixin_param().getSign();
                api.sendReq(request);
            } else{
                UIHelper.t(_activity,"报名成功");
                setResult(RESULT_OK);
                finish();
            }
        }else{
            ac.handleErrorCode(_activity,res.ret_info);
            btnPay.setClickable(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setButton(int id) {
        if (id == R.id.tv_ZhiFuBao) {
            tvWeiXin.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_weixin_n), null, null);
            tvZhiFuBao.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_zhifubao_y), null, null);
            pay_way = "alipay";
        } else if (id == R.id.tv_WeiXin) {
            tvWeiXin.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_wechat_select), null, null);
            tvZhiFuBao.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_zhifubao_n), null, null);
            pay_way = "weixin";
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int n, m;
        if ("".equals(etOld.getText().toString())) {
            n = 0;
        } else {
            n = Integer.valueOf(etOld.getText().toString());
        }
        if ("".equals(etChild.getText().toString())) {
            m = 0;
        } else {
            m = Integer.valueOf(etChild.getText().toString());
        }
        double v = n * Double.parseDouble(OldPrice) + m * Double.parseDouble(ChildPrice);
        DecimalFormat df = new DecimalFormat("#.##");

        tvAllMoney.setText(df.format(v));
    }
}
