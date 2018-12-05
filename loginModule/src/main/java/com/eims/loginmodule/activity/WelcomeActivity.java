package com.eims.loginmodule.activity;

import android.os.Bundle;

import com.eims.baseModule.base.BaseActivity;
import com.eims.loginmodule.R;


public class WelcomeActivity extends BaseActivity {
    private static final int DELAYED = 1000 * 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(LoginActivity.class);
            }
        }, DELAYED);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_welcome;
    }

    @Override
    protected boolean isShowToolBar() {
        return false;
    }
}
