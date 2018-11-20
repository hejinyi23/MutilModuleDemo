package com.demo.baseModule.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.baseModule.R;
import com.demo.baseModule.utils.HandleBackUtil;
import com.demo.baseModule.utils.StringUtil;


import static android.view.Window.ID_ANDROID_CONTENT;


public abstract class BaseActivity extends AppCompatActivity {
    private InputMethodManager imm;
    protected boolean locked = false;
    private LinearLayout parentLinearLayout;
    private TextView mTvTitle;
    private TextView mTvRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //当Activity从异常销毁中恢复后savedInstanceState一定不为null，随后会执行恢复流程，然而当savedInstanceState设置为空后便会走正常启动流程
        if (null != savedInstanceState) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //是否需要ToolBar
        if (isShowToolBar()) {
            initContentView(R.layout.base_base_activity);
            setContentView(getContentViewLayoutId());
            showBackIcon();
            initTooBarView();
        } else {
            setContentView(getContentViewLayoutId());
        }
    }

    private void initTooBarView(){
        Toolbar toolbar =getToolbar();
        mTvTitle=toolbar.findViewById(R.id.tv_tb_center_title);
        mTvRight=toolbar.findViewById(R.id.tv_right);
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 解决透明Activity焦点游标跑到上一个Activity控件中的问题
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    View relative = (View) getCurrentFocus().getParent();
                    relative.setFocusable(true);
                    relative.setFocusableInTouchMode(true);
                    relative.requestFocus();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public Resources getResources() {
        if (isNeedSystemResConfig()) {
            return super.getResources();
        } else {//解决字体大小变化影响
            Resources res = super.getResources();
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
            return res;
        }
    }

    @Override
    public void onBackPressed() {
        if (!HandleBackUtil.handleBackPress(this)) {
            super.onBackPressed();
        }
    }


    protected abstract int getContentViewLayoutId();

    private void initContentView(@LayoutRes int layoutResID) {
        ViewGroup viewGroup = getContentView();
        viewGroup.removeAllViews();

        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        //  add parentLinearLayout in viewGroup
        viewGroup.addView(parentLinearLayout);
        //  add the layout of BaseActivity in parentLinearLayout
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
    }


    /**
     * @param layoutResID layout id of sub-activity
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (isShowToolBar()) {
            LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
        } else {
            super.setContentView(layoutResID);
        }
    }


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Action跳转界面
     **/
    protected void startActivity(String action) {
        startActivity(action, null);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Activity带返回值的跳转
     */
    public void startActivity(Bundle bundle, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(this, cls);
        this.startActivityForResult(intent, requestCode);
    }

    /**
     * Activity带返回值的跳转
     */
    public void startActivity(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        this.startActivityForResult(intent, requestCode);
    }


    /**
     * 默认返回true，使用系统资源，如果个别界面不需要，在这些activity中Override this method ，then return false;
     *
     * @return
     */
    protected boolean isNeedSystemResConfig() {
        return true;
    }

    protected void setFullScreen() {
        // 设置全屏
        View decorView = getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

    public ViewGroup getContentView() {
        return findViewById(ID_ANDROID_CONTENT);
    }

    public View getDecorView() {
        return getWindow().getDecorView();
    }


    private void showBackIcon() {
        if (null != getToolbar() && isShowBackIcon()) {
            getToolbar().setNavigationOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            }));
        }
    }

    private void showBackIcon(@DrawableRes int iconBack) {
        getToolbar().setNavigationIcon(iconBack);
    }

    /**
     * @return TextView in center
     */
    public TextView getToolbarTitle() {
        return mTvTitle;
    }


    /**
     * @return TextView on the right
     */
    public TextView getSubTitle() {
        return mTvRight;
    }


    public void setSubTitlee(CharSequence subTitle) {
        mTvRight.setText(subTitle);
    }

    public void setSubTitle(int titleRes) {
        mTvRight.setText(StringUtil.getString(titleRes));
    }

    /**
     * set Title
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        } else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    public void setToolBarTitle(int titleRes) {
        mTvTitle.setText(StringUtil.getString(titleRes));
    }

    /**
     * the toolbar of this Activity
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.base_toolbar);
    }

    /**
     * is show back icon,default is none。
     * you can override the function in subclass and return to true show the back icon
     *
     * @return
     */
    protected boolean isShowBackIcon() {
        return true;
    }

    protected boolean isShowToolBar() {
        return true;
    }


}
