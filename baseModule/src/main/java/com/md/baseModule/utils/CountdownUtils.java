package com.md.baseModule.utils;

import android.os.CountDownTimer;

/**
 * 倒计时类
 */
public class CountdownUtils extends CountDownTimer {
    private CountdownUListener listener;

    public CountdownUListener getListener() {
        return listener;
    }

    public void setListener(CountdownUListener listener) {
        this.listener = listener;
    }

    public CountdownUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public CountdownUtils(long millisInFuture, long countDownInterval, CountdownUListener listener) {
        super(millisInFuture, countDownInterval);
        this.listener = listener;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        if (listener != null) {
            listener.onFinish();
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示

        if (listener != null) {
            listener.onTick(millisUntilFinished);
        }
    }


    public interface CountdownUListener {
        void onFinish();

        void onTick(long millisUntilFinished);
    }

}
