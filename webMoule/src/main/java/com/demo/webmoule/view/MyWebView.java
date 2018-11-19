package com.demo.webmoule.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyWebView extends WebView {
    private float startx;
    private float starty;

    private int minHeight = -1;

    private static final String TAG = "MyWebView";
    private ValueCallback<Uri[]> filePathCallback;
    private OnWebViewActionListener onWebViewActionListener;
    private boolean isOnShowFileChooser;

    public MyWebView(Context context) {
        this(context, null);
    }


    public MyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public MyWebView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WebSettings settings = this.getSettings(); // 设置WebView属性，能够执行JavaScript脚本
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);// 设置出现缩放工具
        settings.setSupportZoom(false); // 设置可以支持缩放
        //设置自适应屏幕
        settings.setUseWideViewPort(true);// 为图片添加放大缩小功能
        settings.setLoadWithOverviewMode(true);// 缩放至屏幕大小
        setInitialScale(100);//100代表不缩放
        setHorizontalScrollBarEnabled(false);

        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源

        //---------开启存储、缓存-------
        settings.setDomStorageEnabled(true);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        //--------------------
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);// 允许本地缓存

        //去掉缩放按钮
        getSettings().setDisplayZoomControls(false);

        setWebViewClient(new WebViewClient() {
            /**
             * @param url
             * @return
             */
            private boolean interceptRequest(String url) {
                if (isOnShowFileChooser) {
                    return true;
                }

                if (onWebViewActionListener != null) {
                    return onWebViewActionListener.shouldOverrideUrlLoading(url);
                } else {
                    loadUrl(url);
                }

                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onPageStarted(url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onPageFinished(url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return interceptRequest(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request != null) {
                        String url = request.getUrl().toString();
                        if (interceptRequest(url)) {
                            return true;
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }


        });


        setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onReceivedTitle(title);
                }
            }


            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onJsAlert(url, message, result);
                }
                //注意:
                //必须要这一句代码:result.confirm()表示:
                //处理结果为确定状态同时唤醒WebCore线程
                //否则不能继续点击按钮
                result.confirm();
                return true;
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onProgressChanged(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (onWebViewActionListener != null) {
                    onWebViewActionListener.onShowFileChooser(fileChooserParams);
                }
                isOnShowFileChooser = true;
                // Double check that we don't have any existing callbacks
                if (MyWebView.this.filePathCallback != null) {
                    MyWebView.this.filePathCallback.onReceiveValue(null);
                }

                MyWebView.this.filePathCallback = filePathCallback;
                return true;
            }


        });
    }


    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() >= 2) { //多点触控
            getParent().requestDisallowInterceptTouchEvent(true);//屏蔽了父控件
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://webview按下
                getParent().requestDisallowInterceptTouchEvent(true);
                startx = event.getX();
                starty = event.getY();
                break;
            case MotionEvent.ACTION_MOVE://webview滑动
                float offsetx = Math.abs(event.getX() - startx);
                float offsety = Math.abs(event.getY() - starty);
                if (offsetx > offsety) {
                    getParent().requestDisallowInterceptTouchEvent(true);//屏蔽了父控件
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);//事件传递给父控件
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (minHeight > -1 && getMeasuredHeight() < minHeight) {
            setMeasuredDimension(getMeasuredWidth(), minHeight);
        }
    }

    public void loadDataWithBaseURL(String html) {
        this.loadDataWithBaseURL("about:blank", html, "text/html", "utf-8", null);
    }

    public void removeWebView() {
        setWebViewClient(null);
        setWebChromeClient(null);
        ((ViewGroup) getParent()).removeView(this);
        destroy();
    }

    public void uploadFile(Uri[] uris) {
        if (filePathCallback == null) {
            return;
        }
        filePathCallback.onReceiveValue(uris);
        filePathCallback = null;
    }

    public OnWebViewActionListener getOnWebViewActionListener() {
        return onWebViewActionListener;
    }

    public void setOnWebViewActionListener(OnWebViewActionListener onWebViewActionListener) {
        this.onWebViewActionListener = onWebViewActionListener;
    }

    public interface OnWebViewActionListener {
        boolean onShowFileChooser(WebChromeClient.FileChooserParams fileChooserParams);

        void onPageStarted(String url, Bitmap favicon);

        void onPageFinished(String url);

        boolean shouldOverrideUrlLoading(String url);

        boolean onJsAlert(String url, String message, JsResult result);

        void onProgressChanged(int newProgress);

        void onReceivedTitle(String title);
    }

}

