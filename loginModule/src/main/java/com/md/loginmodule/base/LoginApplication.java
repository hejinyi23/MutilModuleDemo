package com.md.loginmodule.base;

import com.md.baseModule.application.AbstractApplication;
import com.md.netlib.utils.RetrofitClient;

public class LoginApplication extends AbstractApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitClient.init(getApplication());
    }
}
