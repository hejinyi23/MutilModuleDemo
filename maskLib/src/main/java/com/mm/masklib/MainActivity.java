package com.mm.masklib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;


import com.demo.masklib.R;
import com.mm.masklib.mask.MaskTool;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private View mask;
    private ViewGroup decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MaskTool.show(MainActivity.this);
                }
            }).start();

        } else {
            MaskTool.hide(this);
        }
    }


}
