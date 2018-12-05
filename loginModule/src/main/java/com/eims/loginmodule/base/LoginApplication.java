package com.eims.loginmodule.base;

import com.eims.baseModule.application.AbstractApplication;
import com.eims.netlib.utils.RetrofitClient;

public class LoginApplication extends AbstractApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitClient.init(getApplication());
    }
}
