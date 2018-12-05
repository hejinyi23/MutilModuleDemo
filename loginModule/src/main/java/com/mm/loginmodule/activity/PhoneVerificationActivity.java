package com.mm.loginmodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mm.baseModule.base.BaseActivity;
import com.mm.baseModule.utils.CountdownUtils;
import com.mm.baseModule.utils.SoftKeyboardUtil;
import com.mm.baseModule.utils.StringUtil;
import com.mm.baseModule.utils.T;
import com.mm.loginmodule.R;
import com.mm.loginmodule.R2;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneVerificationActivity extends BaseActivity {

    @BindView(R2.id.et_verification)
    EditText mEtVerification;
    @BindView(R2.id.btn_verification)
    Button btnVerification;
    @BindView(R2.id.phone_verification)
    TextView mPhoneVerification;
    @BindView(R2.id.reminder_phone_verification)
    TextView reminder;
    String verification;
    int flag;
    String phone;

    @IntDef({FROM_PHONE_LOGIN, FROM_REGISTER, FROM_FIND_PSD, FROM_CHANGE_PHONE, FROM_CHANGE_PHONE2})
    @Retention(RetentionPolicy.SOURCE)
    private @interface From {
    }

    public static final int FROM_PHONE_LOGIN = 1;// 手机登录界面进入
    public static final int FROM_REGISTER = 2;// 注册界面进入
    public static final int FROM_FIND_PSD = 3;// 找回密码界面进入
    public static final int FROM_CHANGE_PHONE = 4;// 更换手机号码
    public static final int FROM_CHANGE_PHONE2 = 5;// 更换手机号码,验证新手机号码


    public static Intent getStartItent(Activity activity, String phoneNum, @From int from) {
        Intent intent = new Intent(activity, PhoneVerificationActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("from", from);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Intent intent = getIntent();
        flag = intent.getIntExtra("from", -1);
        phone = intent.getStringExtra("phoneNum");
        initView();


    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_verification_phone;
    }

    @OnClick({R2.id.btn_verification, R2.id.btn_next_verification})
    public void onClick(View view) {
        SoftKeyboardUtil.hide(this);
        int id = view.getId();
        if (id == R.id.btn_verification) {
            btnVerification.setText(StringUtil.getString(R.string.login_sending));
            new CountdownUtils(180 * 1000, 1000, new CountdownUtils.CountdownUListener() {
                @Override
                public void onFinish() {
                    btnVerification.setText(StringUtil.getString(R.string.login_resend));
                    btnVerification.setTextColor(ContextCompat.getColor(PhoneVerificationActivity.this, R.color.base_colorPrimary));
                    btnVerification.setEnabled(true);
                }

                @Override
                public void onTick(long millisUntilFinished) {
                    btnVerification.setEnabled(false);
                    btnVerification.setTextColor(ContextCompat.getColor(PhoneVerificationActivity.this, R.color.base_secondary_text));
                    btnVerification.setText(millisUntilFinished / 1000 + "s后重新获取");
                }
            }).start();

        } else if (id == R.id.btn_next_verification) {

            String verificationCode = mEtVerification.getText().toString();

            if(TextUtils.isEmpty(verificationCode)){
                T.show(StringUtil.getString(R.string.login_input_verification_code));
                return;
            }

            switch (flag) {
                case FROM_REGISTER:
                    startActivity(SetPsdActivity.getStartIntent(this, FROM_REGISTER, phone,verificationCode));
                    break;
                case FROM_FIND_PSD:
                    startActivity(SetPsdActivity.getStartIntent(this, FROM_FIND_PSD, phone,verificationCode));
                    break;
            }

        }


    }


    private void initView() {
        mPhoneVerification.setText("+86 " + phone.substring(0, 3) + "******" + phone.substring(9, 11));
        switch (flag) {
            case FROM_REGISTER:
                setToolBarTitle(StringUtil.getString(R.string.login_register_zi_hu_account));
                break;
            case FROM_FIND_PSD:
                setToolBarTitle(StringUtil.getString(R.string.login_find_psd_string));
                break;
        }
    }
}
