package com.mm.webmoule.activity;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mm.baseModule.base.BaseActivity;
import com.mm.baseModule.base.RefusedPermissionsTool;
import com.mm.baseModule.utils.StringUtil;
import com.mm.baseModule.utils.T;

import com.mm.webmoule.R;
import com.mm.webmoule.R2;
import com.mm.webmoule.view.MyWebView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.util.ScanTool;


import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

@Route(path = "/webModule/WebActivity")
public class WebActivity extends BaseActivity {
    @BindView(R2.id.wv_content)
    MyWebView wvContent;

    private static final String TAG = "WebActivity";
    private static final String ACTION_TAKE_SCAN_CODE = "110";//扫码
    private static final String ACTION_SHARE = "111";//分享

    private static final int REQUEST_SCAN_OR_CODE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    private Disposable disposable;
    private JsPromptResult result;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                break;
            case REQUEST_SCAN_OR_CODE:
                IntentResult intentResult = IntentIntegrator.parseActivityResult(resultCode, data);
                String ORCode = intentResult.getContents();
                if (result != null) {
                    ORCode = ORCode == null ? "" : ORCode;
                    result.confirm(ORCode);
                    result = null;
                }
                break;
        }

    }

    @Override
    protected boolean isShowToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initWebView();
        RxPermissions rxPermissions = new RxPermissions(this);
        disposable = rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            T.show(StringUtil.getString(R.string.need_storge_permission_to_cache));
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            RefusedPermissionsTool.showMissingPermissionDialog(WebActivity.this, permission.name);
                        }
                    }
                });

        wvContent.loadUrl("file:///android_asset/web_js_call_android.html");
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }


    private void initWebView() {
        //将当前对象映射到JS中，在JS中可以调用 @JavascriptInterface 标记的方法
        wvContent.setOnWebViewActionListener(new MyWebView.OnWebViewActionListener() {
            @Override
            public boolean onShowFileChooser(WebChromeClient.FileChooserParams fileChooserParams) {
                return false;
            }

            @Override
            public void onPageStarted(String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(String url) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                return false;
            }

            @Override
            public boolean onJsAlert(String url, String message, JsResult result) {
                Log.d(TAG, "onJsAlert() called with: url = [" + url + "], message = [" + message + "], result = [" + result + "]");
                return false;
            }


            @Override
            public void onProgressChanged(int newProgress) {

            }

            @Override
            public void onReceivedTitle(String title) {

            }

            @Override
            public boolean onJsPrompt(final String url, String message, String defaultValue, final JsPromptResult result) {
                Log.d(TAG, "onJsPrompt() called with: url = [" + url + "], message = [" + message + "], defaultValue = [" + defaultValue + "], result = [" + result + "]");
                final Uri uri = Uri.parse(message);
                // 检测符合约定的协议
                if (!("js").equals(uri.getScheme()) && "webView".equals(uri.getAuthority())) {
                    return false;
                }
                WebActivity.this.result = result;
                Set<String> keys = uri.getQueryParameterNames();
                for (String key : keys) {
                    String param = uri.getQueryParameter(key);
                    if (param == null) continue;
                    switch (param) {
                        case ACTION_TAKE_SCAN_CODE:
                            scanCode(result);
                            break;
                        case ACTION_SHARE:

                            closeJsPromptResult();
                            break;
                    }
                }


                return true;
            }
        });
    }

    private void scanCode(final JsPromptResult result) {
        RxPermissions rxPermissions = new RxPermissions(WebActivity.this);
        disposable = rxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {//已授权
                            ScanTool.scanBarcodeCustomLayout(WebActivity.this, REQUEST_SCAN_OR_CODE);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            T.show(StringUtil.getString(R.string.need_camera_permission_to_take_photo));
                            closeJsPromptResult();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            RefusedPermissionsTool.showMissingPermissionDialog(WebActivity.this, permission.name);
                            closeJsPromptResult();
                        }

                    }
                });
    }


    private void closeJsPromptResult() {
        if (result != null) {
            result.confirm("");
        }
    }


    @Override
    public void onBackPressed() {
        if (wvContent.canGoBack()) {
            wvContent.goBack();//返回上一页面
        } else {
            //直接返回桌面
            Intent intent = new Intent();
            // 回到桌面
            intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
            intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvContent.removeWebView();
        wvContent = null;
        disposable.dispose();
    }

}
