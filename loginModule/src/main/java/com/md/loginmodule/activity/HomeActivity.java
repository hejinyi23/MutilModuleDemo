package com.md.loginmodule.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;


import com.md.baseModule.base.BaseActivity;
import com.md.loginmodule.R;
import com.md.loginmodule.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    private Unbinder unbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbind = ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_home;
    }


    @OnClick({R2.id.btn_login_home, R2.id.btn_regist_home})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_login_home) {
            startActivity(LoginActivity.class);
        } else {
            startActivity(RegisterActivity.class);
        }
    }


    @Override
    protected void onDestroy() {
        unbind.unbind();
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
