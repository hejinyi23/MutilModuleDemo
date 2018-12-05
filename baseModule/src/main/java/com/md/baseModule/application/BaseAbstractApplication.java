package com.md.baseModule.application;


import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseAbstractApplication extends Application {
    private List<Class<? extends AbstractApplication>> logicClasses;
    private List<AbstractApplication> logics;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        logicClasses = new ArrayList<>();
        logics = new ArrayList<>();
        initLogic();
        logicAttach(base);
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        logicCreate();
    }

    /**
     * 在主Module的application中实现
     * 在实现的方法中调用registerBaseApplicationLogic（）以注册各个Module中的Application到logicClasses中
     */
    protected abstract void initLogic();

    public void registerBaseApplicationLogic(Class<? extends AbstractApplication>... logicClass) {
        logicClasses.addAll(Arrays.asList(logicClass));
    }


    private void logicCreate() {
        for (AbstractApplication aClass : logics) {
            aClass.setApplication(this);
            aClass.onCreate();
        }
    }

    private void logicAttach(Context base) {
        for (Class<? extends AbstractApplication> aClass : logicClasses) {
            try {
                AbstractApplication baseApplicationLogic = aClass.newInstance();
                baseApplicationLogic.attachBaseContext(base);
                logics.add(baseApplicationLogic);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
