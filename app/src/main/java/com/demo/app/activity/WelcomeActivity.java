package com.demo.app.activity;

import android.os.Bundle;
import com.alibaba.android.arouter.launcher.ARouter;
import com.demo.baseModule.base.BaseActivity;
import com.demo.app.R;

import static com.demo.baseModule.constant.RouteUrl.LOGIN_MODULE_LOGIN_ACTIVITY;


public class WelcomeActivity extends BaseActivity {
    private static final int DELAYED = 1000 * 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setFullScreen();
        setContentView(R.layout.login_activity_welcome);

        getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //finish();
                ARouter.getInstance().build(LOGIN_MODULE_LOGIN_ACTIVITY).navigation();
            }
        }, DELAYED);
    }
}
