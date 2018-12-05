package com.mm.masklib.mask;

import android.app.Activity;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.masklib.R;


public class MaskTool {
    private static final String TAG = "MaskTool";

    public synchronized static void show(final Activity activity) {
        if (idMainThread()) {
            addView(activity);
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addView(activity);
                }
            });
        }
    }

    private static void addView(Activity activity) {
        View mask = LayoutInflater.from(activity).inflate(R.layout.layout_mask, null);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if (decorView.findViewById(R.id.ll_mask) == null) {
            decorView.addView(mask);
        }
    }


    public synchronized static void hide(final Activity activity) {
        if (idMainThread()) {
            removeView(activity);
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeView(activity);
                }
            });
        }
    }

    private static void removeView(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View mask = decorView.findViewById(R.id.ll_mask);
        if (mask != null) {
            decorView.removeView(mask);
        }
    }


    private static boolean idMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
