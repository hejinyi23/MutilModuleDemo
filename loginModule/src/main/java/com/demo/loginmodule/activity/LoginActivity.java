package com.demo.loginmodule.activity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.demo.baseModule.base.BaseActivity;
import com.demo.loginmodule.R;
import com.demo.loginmodule.R2;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.demo.baseModule.constant.RouteUrl.WEB_MODULE_WEB_ACTIVITY;

@Route(path = "/loginModule/LoginActivity")
public class LoginActivity extends BaseActivity {

    @BindView(R2.id.et_account)
    EditText etAccount;
    @BindView(R2.id.et_password)
    EditText etPassword;
    @BindView(R2.id.pb_login)
    View mProgressView;

    private UserLoginTask mAuthTask = null;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R2.id.btn_sign})
    public void onClick(View view) {
        attemptLogin();
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        etAccount.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            etAccount.setError(getString(R.string.error_field_required));
            focusView = etAccount;
            cancel = true;
        } else if (!isEmailValid(account)) {
            etAccount.setError(getString(R.string.error_invalid_account));
            focusView = etAccount;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!cancel && (TextUtils.isEmpty(password) || !isPasswordValid(password))) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(this, account, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String account) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mAccount;
        private final String mPassword;
        private WeakReference<LoginActivity> weakReference;

        UserLoginTask(LoginActivity activity, String account, String password) {
            weakReference = new WeakReference<>(activity);
            mAccount = account;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            LoginActivity activity = weakReference.get();
            if (weakReference == null) {
                return;
            }
            activity.mAuthTask = null;
            activity.showProgress(false);
            if (success) {
                activity.finish();
                ARouter.getInstance().build(WEB_MODULE_WEB_ACTIVITY).navigation();
                // activity.startActivity(new Intent(activity, MainActivity.class));
            } else {
                activity.etPassword.setError(activity.getString(R.string.error_incorrect_password));
                activity.etPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            LoginActivity activity = weakReference.get();
            if (weakReference == null) {
                return;
            }
            activity.mAuthTask = null;
            activity.showProgress(false);
        }
    }
}

