package com.eims.baseModule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import io.reactivex.annotations.NonNull;

public final class AppPreferenceManager {
    private static final String TAG = "AppPreferenceManager";
    private static Editor editor;
    private static SharedPreferences sp;
    private static final String PREFERENCES_NAME = "config";

    private static SharedPreferences getPreferencesInstance(@NonNull Context context) {
        if (sp == null) {
            synchronized (AppPreferenceManager.class) {
                if (sp == null) {
                    sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
                }
            }
        }
        return sp;
    }


    private static Editor getEditorInstance(Context context) {
        if (editor == null) {
            if (sp != null) {
                editor = sp.edit();
            } else {
                editor = getPreferencesInstance(context).edit();
            }
        }
        return editor;
    }


    public static void setString(Context context, String key, String value) {
        getEditorInstance(context).putString(key, value).commit();
    }

    public static void setBoolean(Context context, String key, boolean value) {
        getEditorInstance(context).putBoolean(key, value).commit();
    }

    public static void setInt(Context context, String key, int value) {
        getEditorInstance(context).putInt(key, value).commit();
    }

    public static void setLong(Context context, String key, long value) {
        getEditorInstance(context).putLong(key, value).commit();
    }

    public static void setFloat(Context context, String key, float value) {
        getEditorInstance(context).putFloat(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defValue) {
        return getPreferencesInstance(context).getString(key, defValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return getPreferencesInstance(context).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key, Boolean defValue) {
        return getPreferencesInstance(context).getBoolean(key, defValue);
    }

    public static int getInt(Context context, String key) {
        return getPreferencesInstance(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getPreferencesInstance(context).getInt(key, defValue);
    }

    public static long getLong(Context context, String key) {
        return getPreferencesInstance(context).getLong(key, 0);
    }

    public static float getFloat(Context context, String key) {
        return getPreferencesInstance(context).getFloat(key, 0.0F);
    }

    /*
     * 清空SharedPreferences
     * */
    public static void clear(Context context) {
        Editor editor = getEditorInstance(context);
        editor.clear();
        editor.commit();
    }

}
