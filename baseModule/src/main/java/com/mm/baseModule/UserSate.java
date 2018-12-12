package com.mm.baseModule;


import com.eims.baseModule.utils.AppPreferenceManager;
import com.mm.baseModule.base.BaseApplication;

/**
 * 记录用户登录状态
 */
public class UserSate {

    private static class Singleton {
        private static final UserSate SINGLETON = new UserSate();
    }


    private UserSate() {
    }

    public UserSate getInstance() {
        return Singleton.SINGLETON;
    }

    public void login(String count, String psd, boolean isSaveInfo) {
        if (isSaveInfo) {
            setCountToPreference(count);
            setPsdToPreference(psd);
        }
    }

    public void logout() {
        setPsdToPreference("");
    }


    private void setCountToPreference(String count) {
        AppPreferenceManager.setString(BaseApplication.getApplication(), "count", count);
    }

    private String getCountFromPreference() {
        return AppPreferenceManager.getString(BaseApplication.getApplication(), "count");
    }


    private void setPsdToPreference(String psd) {
        AppPreferenceManager.setString(BaseApplication.getApplication(), psd, "psd");
    }

    private String getPsdFromPreference() {
        return AppPreferenceManager.getString(BaseApplication.getApplication(), "psd");
    }

    public boolean isLogin() {
        return getPsdFromPreference().length() > 0;
    }


    public String getCount() {
        //避免被其它地方修改
        return getCountFromPreference();
    }

}
