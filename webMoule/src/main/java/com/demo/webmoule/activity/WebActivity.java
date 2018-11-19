package com.demo.webmoule.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.demo.baseModule.base.CheckPermissionsActivity;
import com.demo.eventlib.events.PermissionsPassedEvent;
import com.demo.webmoule.R;
import com.demo.webmoule.R2;
import com.demo.webmoule.view.MyWebView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/webModule/WebActivity")
public class WebActivity extends CheckPermissionsActivity {
    @BindView(R2.id.wv_content)
    MyWebView wvContent;

    private static final String TAG = "WebActivity";
    private static final int REQUEST_CAMERA_CODE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    private static final int REQUEST_PERMISSION_STORAGE = 3;
    private String mCameraPhotoPath;
    private long size = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != REQUEST_CAMERA_CODE || resultCode != Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (data != null || mCameraPhotoPath != null) {
            String file_path = mCameraPhotoPath.replace("file:", "");
            File file = new File(file_path);
            size = file.length();
            Integer count = 1;
            ClipData clipData = null;

            try {
                clipData = data.getClipData();

                if (clipData == null && data.getDataString() != null) {
                    count = data.getDataString().length();
                } else if (clipData != null) {
                    count = clipData.getItemCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Uri[] results = new Uri[count];
            if (size != 0) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else if (data.getClipData() == null) {
                results = new Uri[]{Uri.parse(data.getDataString())};
            } else {

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    results[i] = clipData.getItemAt(i).getUri();
                }
            }

            wvContent.uploadFile(results);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initWebView();
        checkPermissions(REQUEST_PERMISSION_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        wvContent.loadUrl("https://www.baidu.com/");
    }

    private void initWebView() {
        wvContent.addJavascriptInterface(this, "android");
        wvContent.setOnWebViewActionListener(new MyWebView.OnWebViewActionListener() {
            @Override
            public boolean onShowFileChooser(WebChromeClient.FileChooserParams fileChooserParams) {
                if (checkPermissions(REQUEST_PERMISSION_CAMERA, Manifest.permission.CAMERA)) {
                    openCamera();
                }
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
                return false;
            }

            @Override
            public boolean onJsAlert(String url, String message, JsResult result) {
                return false;
            }

            @Override
            public void onProgressChanged(int newProgress) {

            }

            @Override
            public void onReceivedTitle(String title) {

            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "Unable to create Image File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        contentSelectionIntent.setType("image/*");

        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[2];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(Intent.createChooser(chooserIntent, "Select images"), 1);
    }


    /**
     * JS调用android的方法
     *
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        Log.i("getClient", "html调用客户端:" + str);
    }


    @Override
    public void onBackPressed() {
        if (wvContent.canGoBack()) {
            wvContent.goBack();//返回上一页面
        } else {
            //直接返回桌面
            Intent intent = new Intent();
            // 为Intent设置Action、Category属性
            intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
            intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        wvContent.removeWebView();
        wvContent = null;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPermissionsPassedEvent(PermissionsPassedEvent event) {
        if (event.getRequestCode() == REQUEST_PERMISSION_CAMERA) {
            openCamera();
        }
    }


}
