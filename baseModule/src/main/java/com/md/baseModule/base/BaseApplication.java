package com.md.baseModule.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.md.baseModule.BuildConfig;
import com.md.baseModule.application.BaseAbstractApplication;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

public abstract class BaseApplication extends BaseAbstractApplication implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "BaseApplication";
    private static BaseApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        //检查是否主进程
        if (this.getPackageName().equals(getProcessName(this))) {
            application = this;
            // 加载系统默认设置，字体不随用户设置变化
            updateConfiguration();
            registerActivityLifecycleCallbacks(this);

            //初始化打印工具
            Logger.addLogAdapter(new AndroidLogAdapter() {
                @Override
                public boolean isLoggable(int priority, String tag) {
                    return BuildConfig.DEBUG;
                }
            });

            //初始化ARouter
            if (BuildConfig.DEBUG) {    // These two lines must be written before init, otherwise these configurations will be invalid in the init process
                ARouter.openLog();     // Print log
                ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
            }
            ARouter.init(this);
        }
    }

    private void updateConfiguration() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }


    /**
     * 得到BaseApplication
     */
    public static BaseApplication getApplication() {
        return application;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(this);
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        AppManager.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        AppManager.getInstance().increaseForegroundActivityCount();
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        AppManager.getInstance().decreaseForegroundActivityCount();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        AppManager.getInstance().removeActivity(activity);
    }


    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }


}
