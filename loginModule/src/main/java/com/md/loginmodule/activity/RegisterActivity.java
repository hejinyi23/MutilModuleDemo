package com.md.loginmodule.activity;

import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.md.baseModule.base.BaseActivity;
import com.md.baseModule.utils.EditTextUtils;
import com.md.baseModule.utils.SoftKeyboardUtil;
import com.md.baseModule.utils.StringUtil;
import com.md.baseModule.utils.T;
import com.md.loginmodule.R;
import com.md.loginmodule.R2;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R2.id.et_phone_regist)
    EditText mEtPhone;
    @BindView(R2.id.tv_delete)
    ImageView mDeletePhone;
    String phone;
    @BindView(R2.id.cb_agree)
    CheckBox cb_agree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        initView();
    }

    private void initView() {
        EditTextUtils.visibleDelete(mEtPhone, mDeletePhone);
    }


    @Override
    protected boolean isShowToolBar() {
        return false;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_regist;
    }

    @OnClick({R2.id.regist, R2.id.user_agreement, R2.id.tv_delete})
    public void onViewClicked(View view) {
        SoftKeyboardUtil.hide(this);
        if (view.getId() == R.id.regist) {
            if(!isPhoneNum(mEtPhone.getText().toString())){
                T.show(StringUtil.getString(R.string.login_phone_num_unused));
                return;
            }

            if (cb_agree.isChecked()) {
                doRegister();
            } else {
                T.show(StringUtil.getString(R.string.login_agree_protocol_and_go_on));
            }

        } else if (view.getId() == R.id.user_agreement) {
            startActivity(UserAgreementActivity.class);
        } else if (view.getId() == R.id.tv_delete) {
            mEtPhone.setText("");
        }
    }

    private void doRegister() {
        startActivity(PhoneVerificationActivity.getStartItent(this,mEtPhone.getText().toString(),PhoneVerificationActivity.FROM_REGISTER));
    }

    private boolean isPhoneNum(String phoneNum){
        return StringUtil.isMobileNO(phoneNum);
    }
}
