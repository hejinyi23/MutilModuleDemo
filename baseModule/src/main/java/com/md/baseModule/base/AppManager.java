package com.md.baseModule.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.Stack;

/**
 * activity堆栈式管理
 */
public class AppManager {

    private static int foregroundActivityCount = 0;//统计前台Activity数量
    private static Stack<Activity> activityStack;
    private static Stack<Fragment> fragmentStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单例模式
     *
     * @return AppManager
     */
    public static AppManager getInstance() {
        synchronized (AppManager.class) {
            if (instance == null) {
                synchronized (AppManager.class) {
                    if (instance == null) {
                        instance = new AppManager();
                    }
                }

            }
        }
        return instance;
    }

    void increaseForegroundActivityCount() {
        foregroundActivityCount++;
    }

    void decreaseForegroundActivityCount() {
        foregroundActivityCount++;
    }

    /**
     * 判断当前应用是否在后台运行
     *
     * @return
     */
    public boolean isRunInBackground() {
        return foregroundActivityCount == 0;
    }


    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    public boolean hasActivity() {
        if (activityStack != null) {
            return !activityStack.isEmpty();
        }
        return false;
    }


    public Activity getTopActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前顶端Activity
     */
    public void finishTopActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }


    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }


    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }


    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }


    public Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }


    public void addFragment(Fragment fragment) {
        if (fragmentStack == null) {
            fragmentStack = new Stack<Fragment>();
        }
        fragmentStack.add(fragment);
    }

    /**
     * 移除指定的Fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentStack.remove(fragment);
        }
    }


    /**
     * 是否有Fragment
     */
    public boolean hasFragment() {
        if (fragmentStack != null) {
            return !fragmentStack.isEmpty();
        }
        return false;
    }


    public Fragment getTopFragment() {
        if (fragmentStack != null) {
            Fragment fragment = fragmentStack.lastElement();
            return fragment;
        }
        return null;
    }


    /**
     * 退出应用程序
     */
    public void exit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            activityStack.clear();
            e.printStackTrace();
        }
    }
}