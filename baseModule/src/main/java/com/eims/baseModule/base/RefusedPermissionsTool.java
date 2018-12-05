
package com.eims.baseModule.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.eims.baseModule.R;

/**
 * 对已经选择了不再提示的权限处理
 */
public abstract class RefusedPermissionsTool {

    private static String getTip(String permissionName) {
        String tip = "";
        switch (permissionName) {
            case Manifest.permission.RECORD_AUDIO:
                tip = "麦克风";
                break;
            case Manifest.permission.CAMERA:
                tip = "相机";
                break;

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                tip = "读写存储";
                break;
            case Manifest.permission.ACCESS_NETWORK_STATE:
                tip = "网络状态";
                break;
        }
        return tip;
    }

    /**
     * 启动应用的设置页面
     */
    private static void startAppSettings(Activity activity) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    /**
     * 显示提示信息
     */
    public static void showMissingPermissionDialog(@NonNull final Activity activity, @NonNull String permissionName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.base_notifyTitle);
        builder.setMessage("【你已经选择了不再提示，或系统默认不再提示】\r\n" +
                "获取【" + getTip(permissionName) + "】权限失败,将导致部分功能无法正常使用，需要到设置页面手动授权");

        // 拒绝
        builder.setNegativeButton(R.string.base_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "请授权后继续", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setPositiveButton(R.string.base_authorization,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(activity);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }


}
