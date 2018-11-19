package com.demo.app.base;

import com.demo.baseModule.base.BaseApplication;
import com.demo.loginmodule.base.LoginApplication;

public class AppApplication extends BaseApplication {
    @Override
    protected void initLogic() {
        //注册其它模块中的application
       registerBaseApplicationLogic(LoginApplication.class);
    }
}