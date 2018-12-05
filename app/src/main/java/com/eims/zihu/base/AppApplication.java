package com.eims.zihu.base;

import com.eims.baseModule.base.BaseApplication;
import com.eims.loginmodule.base.LoginApplication;

import com.eims.zihu.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

import butterknife.ButterKnife;


public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
    }

    @Override
    protected void initLogic() {
        //注册其它模块中的application
        registerBaseApplicationLogic(LoginApplication.class);
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

}