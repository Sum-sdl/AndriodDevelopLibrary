package com.sum.andrioddeveloplibrary.autosize;

import com.sum.andrioddeveloplibrary.R;
import com.sum.library.app.BaseActivity;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Created by sdl on 2017/12/29.
 * 自适配屏幕
 *
 * 750x1334大小适配
 */

public class AutoSizeTestActivity extends BaseActivity implements CustomAdapt {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initParams() {

    }


    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 375;
    }
}
