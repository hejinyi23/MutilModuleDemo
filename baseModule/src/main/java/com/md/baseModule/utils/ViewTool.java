package com.md.baseModule.utils;

import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * @author 潘城尧
 * @category 简化findViewById及隐藏与显示View等方法。
 */

public class ViewTool {
    private Window window;

    public ViewTool(Window window) {
        this.window = window;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getViewById(int id) {
        return (T) window.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getViewById(View rootView, int id) {
        return (T) rootView.findViewById(id);
    }

    public void hideView(int... resourdIds) {

        for (int id : resourdIds) {
            View view = window.findViewById(id);
            if (view != null) {
                view.setVisibility(View.GONE);
            }

        }

    }


    public void hideView(View... view) {
        for (View v : view) {
            if (v != null && v.getVisibility() == View.VISIBLE) {
                v.setVisibility(View.GONE);
            }
        }

    }

    public void showView(View... view) {
        for (View v : view) {
            if (v != null && v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
            }
        }

    }

    public void showView(int... resourceIds) {

        for (int id : resourceIds) {
            View view = window.findViewById(id);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }

        }

    }

    public void setText(int textViewId, String text) {
        TextView textView;
        try {
            textView = getViewById(textViewId);
            setText(textView, text);
            showView(textView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public <T extends TextView> void setText(T view, String text) {
        if (view != null) {
            view.setText(text);
        }
    }


    public <T extends TextView> String getText(T view) {
        return view == null ? null : view.getText().toString().trim();
    }

    public void setViewNonClick(View... view) {
        for (View v : view) {
            if (v != null && v.isClickable()) {
                v.setClickable(false);
            }
        }

    }

    public void setViewNonClick(int... resourceIds) {

        for (int id : resourceIds) {
            View view = window.findViewById(id);
            if (view != null && view.isClickable()) {
                view.setClickable(false);
            }
        }

    }

    public void setViewCanClick(View... view) {
        for (View v : view) {
            if (v != null && !v.isClickable()) {
                v.setClickable(true);
            }
        }

    }


    public void setViewCanClick(int... resourceIds) {

        for (int id : resourceIds) {
            View view = window.findViewById(id);
            if (view != null && !view.isClickable()) {
                view.setClickable(true);

            }
        }

    }


}
