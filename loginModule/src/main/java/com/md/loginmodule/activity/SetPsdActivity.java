package com.md.loginmodule.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.baseModule.base.BaseActivity;

import com.md.baseModule.utils.EditTextUtils;
import com.md.baseModule.utils.SoftKeyboardUtil;
import com.md.baseModule.utils.StringUtil;
import com.md.baseModule.utils.T;
import com.md.loginmodule.R;
import com.md.loginmodule.R2;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.md.loginmodule.activity.PhoneVerificationActivity.FROM_FIND_PSD;
import static com.md.loginmodule.activity.PhoneVerificationActivity.FROM_REGISTER;


public class SetPsdActivity extends BaseActivity {

    @BindView(R2.id.reminder_set_psd)
    TextView reminder;
    @BindView(R2.id.et_new_psd)
    EditText etNewPsd;
    @BindView(R2.id.delete_new_psd)
    ImageView deleteNewPsd;
    @BindView(R2.id.switch_new_psd)
    CheckBox switchNewPsd;
    @BindView(R2.id.et_verification_psd)
    EditText etVerificationPsd;
    @BindView(R2.id.delete_verification_psd)
    ImageView deleteVerificationPsd;
    @BindView(R2.id.switch_verification_psd)
    CheckBox switchVerificationPsd;
    @BindView(R2.id.et_old_psd)
    EditText etOldPsd;
    @BindView(R2.id.delete_old_psd)
    ImageView deleteOldPsd;
    @BindView(R2.id.switch_old_psd)
    CheckBox switchOldPsd;
    @BindView(R2.id.ll_old_psd)
    LinearLayout llOldPsd;
    @BindView(R2.id.tv_new_psd)
    TextView tvNewPsd;
    @BindView(R2.id.tv_verification_psd)
    TextView tvVerificationPsd;
    @BindView(R2.id.bt_finish)
    Button btFinish;

    Intent intent;
    String phone;
    String newPsd;
    String oldPsd;
    String verificationPsd;
    String verificationCode;//验证码
    int flag;
    String account;


    @IntDef({FROM_FIND_PSD, FROM_REGISTER})
    @Retention(RetentionPolicy.SOURCE)
    private @interface FLag {
    }


    public static Intent getStartIntent(@NonNull Context context, @FLag int flag, String phone, String verificationCode) {
        Intent intent = new Intent(context, SetPsdActivity.class);
        intent.putExtra("flag", flag);
        intent.putExtra("phone", phone);
        intent.putExtra("verification_code", verificationCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setToolBarTitle(StringUtil.getString(R.string.login_set_psd));

        // 输入框有无文字来判断是否显示后面的delete
        EditTextUtils.visibleDelete(etNewPsd, deleteNewPsd);
        EditTextUtils.visibleDelete(etVerificationPsd, deleteVerificationPsd);

        // 密码的明文密文变化
        EditTextUtils.setPsdHide(etNewPsd, switchNewPsd);
        EditTextUtils.setPsdHide(etVerificationPsd, switchVerificationPsd);

        intent = getIntent();
        flag = intent.getIntExtra("flag", -1);
        if (flag == FROM_REGISTER) {//设置密码
            phone = intent.getStringExtra("phone");
            llOldPsd.setVisibility(View.GONE);
            reminder.setText("");
            btFinish.setText(StringUtil.getString(R.string.login_save_and_login));
            tvNewPsd.setText(StringUtil.getString(R.string.login_psd));

            tvVerificationPsd.setText(StringUtil.getString(R.string.login_psd_verification));
            etVerificationPsd.setHint(StringUtil.getString(R.string.login_input_psd));

        } else if (flag == PhoneVerificationActivity.FROM_FIND_PSD) {//找回密码
            verificationCode = intent.getStringExtra("verification_code");
            llOldPsd.setVisibility(View.GONE);

            reminder.setText(StringUtil.getString(R.string.login_set_new_dan_cancel_od));
            btFinish.setText(StringUtil.getString(R.string.login_completely_delete));

        }
//        else if (flag == .RESET_PSD) {//重置密码
//            // 输入框有无文字来判断是否显示后面的delete
//            EditTextUtils.visibleDelete(etOldPsd, deleteOldPsd);
//            EditTextUtils.setPsdHide(etOldPsd, switchOldPsd);
//            btFinish.setText(StringUtil.getString(R.string.complete));
//
//            reminder.setText(StringUtil.getString(R.string.set_psd_string1));
//        }

    }


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_set_psd;
    }


    @OnClick(R2.id.bt_finish)
    public void onViewClicked() {
        SoftKeyboardUtil.hide(this);
        newPsd = etNewPsd.getText().toString();
        verificationPsd = etVerificationPsd.getText().toString();
        if (newPsd.length() < 8 || newPsd.length() > 20 || verificationPsd.length() < 8 || verificationPsd.length() > 20) {
            T.show("请输入符合规范的密码");
            return;
        }

        if (!newPsd.equals(verificationPsd)) {
            T.show("两次输入密码不一样");
            return;
        }
        if (flag == FROM_REGISTER) {//注册

        } else if (flag == PhoneVerificationActivity.FROM_FIND_PSD) {//来自找回密码


        }
//        else if (flag == .RESET_PSD) {//“我”->"重置登录密码"
//            oldPsd = etOldPsd.getText().toString();
//            if (!TextUtils.isEmpty(oldPsd)) {
//
//            } else {
//                T.show("旧密码不可为空");
//            }
//        }
    }


}
