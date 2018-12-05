package com.mm.loginmodule.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mm.baseModule.base.BaseActivity;
import com.mm.baseModule.utils.EditTextUtils;
import com.mm.loginmodule.R;
import com.mm.loginmodule.R2;
import com.mm.loginmodule.entity.LoginEntity;
import com.mm.loginmodule.netapi.LoginApiService;
import com.mm.masklib.mask.MaskTool;
import com.mm.netlib.http.BaseResponse;
import com.mm.netlib.utils.RetrofitClient;
import com.mm.netlib.utils.RxUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.mm.baseModule.constant.RouteUrl.WEB_MODULE_WEB_ACTIVITY;

public class LoginActivity extends BaseActivity {

    @BindView(R2.id.et_account)
    EditText etAccount;
    @BindView(R2.id.et_password)
    EditText etPassword;
    @BindView(R2.id.iv_delete)
    View ivDelete;
    @BindView(R2.id.cb_switch)
    CheckBox cbSwitch;
    private Unbinder unbinder;
    private CompositeDisposable mDisposable;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    protected boolean isShowToolBar() {
        return false;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
        unbinder.unbind();
    }

    @OnClick({R2.id.btn_sign, R2.id.iv_delete, R2.id.tv_register, R2.id.tv_forget_psd})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_sign) {//登录
            attemptLogin();
        } else if (id == R.id.iv_delete) {//清空账号
            etAccount.setText("");
        } else if (id == R.id.tv_register) {//注册
            startActivity(RegisterActivity.class);
        } else if (id == R.id.tv_forget_psd) {//忘记密码
            startActivity(FindPsdActivity.getStartIntent(this, PhoneVerificationActivity.FROM_FIND_PSD));
        }
    }


    private void attemptLogin() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(account)) {
            focusView = etAccount;
            cancel = true;
        } else if (!isEmailValid(account)) {
            focusView = etAccount;
            cancel = true;
        }

        if (!cancel && (TextUtils.isEmpty(password))) {
            focusView = etPassword;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            ARouter.getInstance().build(WEB_MODULE_WEB_ACTIVITY).navigation();
            //requestNetWork();
        }
    }

    private boolean isEmailValid(String account) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    private void showProgress(final boolean show) {
        if (show) {
            MaskTool.show(this);
        } else {
            MaskTool.hide(this);
        }
    }

    private void initView() {
        mDisposable = new CompositeDisposable();
        EditTextUtils.visibleDelete(etAccount, ivDelete);
        EditTextUtils.setPsdHide(etPassword, cbSwitch);
    }


    public void requestNetWork() {
        Disposable disposable = RetrofitClient.getInstance().create(LoginApiService.class).loginRequest("老衲", "1234")
                .compose(RxUtils.<LoginEntity>schedulersTransformer())//指定观察者及被被观察者的调度线程
                .compose(RxUtils.<LoginEntity>exceptionTransformer())//添加自定义异常处理
                //这个方法与observeOn指定线程一致
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //正在请求
                    }
                }).doOnNext(new Consumer<BaseResponse<LoginEntity>>() {
                    @Override
                    public void accept(BaseResponse<LoginEntity> loginEntityBaseResponse) throws Exception {
                        Log.d(TAG, "doOnNext-------------------: ");

                    }
                })
                .subscribe(new Consumer<BaseResponse<LoginEntity>>() {
                               @Override
                               public void accept(BaseResponse<LoginEntity> response) throws Exception {
                                   Log.d(TAG, "BaseResponse-----------accept: ");
                                   //请求成功
                                   if (response.isOk()) {
                                       ARouter.getInstance().build(WEB_MODULE_WEB_ACTIVITY);
                                   } else {
                                       ARouter.getInstance().build(WEB_MODULE_WEB_ACTIVITY);
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.d(TAG, "Throwable--------- called with: throwable = [" + throwable + "]");

                                   throwable.printStackTrace();
                                   // 异常处理
                               }
                           }
                        , new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.d(TAG, "Action----------------: ");
                            }
                        });

        mDisposable.add(disposable);


    }


}

