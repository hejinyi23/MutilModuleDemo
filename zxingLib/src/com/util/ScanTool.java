package com.util;

import android.app.Activity;

import com.activity.CustomScannerActivity;
import com.google.zxing.integration.android.IntentIntegrator;

public class ScanTool {

    private static IntentIntegrator getDefault(Activity activity, int requestCode) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setRequestCode(requestCode);
        return integrator;
    }

    public static void scanBarcodeCustomLayout(Activity activity, int requestCode) {
        IntentIntegrator integrator = getDefault(activity, requestCode);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("对准后扫描");
        integrator.setBeepEnabled(false);//关掉哔哔声
        integrator.setTimeout(1000 * 40);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

}
