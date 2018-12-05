package com.mm.loginmodule.base;

import com.mm.baseModule.application.AbstractApplication;
import com.mm.netlib.utils.RetrofitClient;

public class LoginApplication extends AbstractApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitClient.init(getApplication());
    }
}
