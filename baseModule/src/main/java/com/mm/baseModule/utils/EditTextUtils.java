package com.mm.baseModule.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


public class EditTextUtils {

    /**
     * 监听edittext失去焦点的时候，隐藏后面的delete
     */
    public static void visibleDeleteByFocus(final EditText editText,
                                            final View delete) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText1=  (EditText) v;
                if (hasFocus) {
                    if (!TextUtils.isEmpty(editText1.getText())) {
                        delete.setVisibility(View.VISIBLE);
                    }

                } else {
                    delete.setVisibility(View.INVISIBLE);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    /**
     * 输入框有无文字来判断是否显示后面的delete
     */
    public static void visibleDeleteByText(final EditText editText, final View delete) {
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    delete.setVisibility(View.INVISIBLE);
                } else {
                    delete.setVisibility(View.VISIBLE);
                }

            }
        });
        //点击edittext后面的叉的时候，将edittext置空
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    /**
     * 输入框有无文字和输入框是否获得焦点来判断是否显示后面的delete
     */
    public static void visibleDelete(EditText editText, final View delete) {
        visibleDeleteByFocus(editText, delete);
        visibleDeleteByText(editText, delete);
    }

    public static void setPsdHide(final EditText editText, CheckBox checkBox) {
        // 密码的明文密文变化
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                editText.setSelection(editText.getText().length());
            }
        });
    }

    /**
     * 当输入框first内容发生变化时清除输入框other中的内容
     *
     * @param first 需要监听内容变化的输入框
     * @param other 需要清除内容的输入框
     */
    public static void firstChangedAndCleanOther(final EditText first, final EditText... other) {
        if (first == null || other == null || other.length == 0) return;
        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                for (EditText et : other) {
                    et.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
