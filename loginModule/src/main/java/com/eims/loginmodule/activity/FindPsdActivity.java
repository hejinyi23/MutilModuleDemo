package com.eims.loginmodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.IntDef;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.baseModule.utils.EditTextUtils;
import com.eims.baseModule.utils.SoftKeyboardUtil;
import com.eims.baseModule.utils.StringUtil;
import com.eims.loginmodule.R;
import com.eims.loginmodule.R2;

import com.eims.baseModule.base.BaseActivity;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.eims.loginmodule.activity.PhoneVerificationActivity.FROM_CHANGE_PHONE2;
import static com.eims.loginmodule.activity.PhoneVerificationActivity.FROM_FIND_PSD;


public class FindPsdActivity extends BaseActivity {
    @BindView(R2.id.et_phone_find_psd)
    EditText etPhoneFindPsd;
    @BindView(R2.id.delete_phone)
    ImageView deletePhone;
    @BindView(R2.id.tip_find_psd)
    TextView tipFindPsd;
    @BindView(R2.id.space_find_psd)
    TextView space;
    String phone;
    int flag;


    @IntDef({FROM_FIND_PSD, FROM_CHANGE_PHONE2})
    @Retention(RetentionPolicy.SOURCE)
    private @interface FLag {
    }

    public static Intent getStartIntent(Activity activity, @FLag int flag) {
        Intent intent = new Intent(activity, FindPsdActivity.class);
        intent.putExtra("flag", flag);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        ButterKnife.bind(this);
        // 输入框有无文字来判断是否显示后面的delete
        EditTextUtils.visibleDelete(etPhoneFindPsd, deletePhone);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", -1);

        if (flag == FROM_FIND_PSD) {//找回密码
            setToolBarTitle(StringUtil.getString(R.string.login_find_psd_string));
        } else if (flag == FROM_CHANGE_PHONE2) {//更换手机号码
            setToolBarTitle(StringUtil.getString(R.string.login_replace_phone_num));
            tipFindPsd.setVisibility(View.GONE);
            space.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_find_psd;
    }

    @OnClick(R2.id.btn_next_find_psd)
    public void onViewClicked() {
        SoftKeyboardUtil.hide(this);
        phone = etPhoneFindPsd.getText().toString();
        if (StringUtil.isMobileNO(phone)) {
            startActivity(PhoneVerificationActivity.getStartItent(this, phone, FROM_FIND_PSD));
        } else {
            Toast.makeText(this, StringUtil.getString(R.string.login_phone_num_unused), Toast.LENGTH_SHORT).show();
        }
    }
}
