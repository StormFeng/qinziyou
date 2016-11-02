package midian.baselib.widget;

import com.midian.baselib.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.WindowManager.LayoutParams;

/**
 * 自定义通用弹窗
 * Created by chu on 2016/2/26.
 */
public class CustomDialog extends Dialog {
    private Context context;
    private String title;
    private String content;
    private String confirmButtonText;
    private String cancelButtonText;
    private VerificationDialogInterface mDialogInterface;

    private TextView title_tv, contentHint_tv, content_hint;
    private EditText content_et;
    private Button confirm_bt, cancel_bt;

    // 自定义回调，用于Dialog事件监听
    public interface VerificationDialogInterface {
        public void doConfirm();

        public void doCancel();

    }

    public CustomDialog(Context context, VerificationDialogInterface mDialogInterface) {
        super(context);
        this.mDialogInterface = mDialogInterface;
    }

    public CustomDialog(Context context, String title, String content, String confirmButtonText,
                        String cancelButtonText) {
        super(context, R.style.confirm_dialog);
        this.context = context;
        this.title = title;
        this.content = content;
        this.confirmButtonText = confirmButtonText;
        this.cancelButtonText = cancelButtonText;
    }

    public static void make(Context context, String title, String content, String confirmButtonText,
                            String cancelButtonText) {
        CustomDialog verificationDialog = new CustomDialog(context, title, content, confirmButtonText,
                cancelButtonText);
        verificationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        verificationDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_dialog);

        title_tv = (TextView) findViewById(R.id.dialog_title);// 标题
        confirm_bt = (Button) findViewById(R.id.confirm_bt);// 确认
        cancel_bt = (Button) findViewById(R.id.cancel_bt);// 取消
        content_et = (EditText) findViewById(R.id.input);// 输入框
        contentHint_tv = (TextView) findViewById(R.id.hint);// 状态提示文字
        content_hint = (TextView) findViewById(R.id.hint);
        title_tv.setText(title);
        content_hint.setText(content);
        confirm_bt.setText(confirmButtonText);
        cancel_bt.setText(cancelButtonText);

        confirm_bt.setOnClickListener(new clickListener());
        cancel_bt.setOnClickListener(new clickListener());

        // 设置对齐方式
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        // 设置排列方式
        WindowManager.LayoutParams p = window.getAttributes();
        p.width = LayoutParams.MATCH_PARENT;
        window.setAttributes(p);

    }

    public void setClicklistener(VerificationDialogInterface mDialogInterface) {
        this.mDialogInterface = mDialogInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.cancel_bt) {
                mDialogInterface.doCancel();
                dismiss();
            }
            if (id == R.id.confirm_bt) {
                mDialogInterface.doConfirm();
                dismiss();
            }
        }
    }
}
