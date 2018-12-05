package com.md.loginmodule.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;


import com.md.baseModule.base.BaseActivity;
import com.md.loginmodule.R;
import com.md.loginmodule.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAgreementActivity extends BaseActivity {
    @BindView(R2.id.web_user_agreement)
    WebView web;
    @BindView(R2.id.pb_user_agreement)
    ProgressBar progressBar;

    public static final String USE_RAGRESS_URL = "https://isv.xiniu.com/tk.html";//用户协议用的url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        web.loadUrl(USE_RAGRESS_URL);

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_user_agreement;
    }
}
