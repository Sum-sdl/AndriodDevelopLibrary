package com.sum.andrioddeveloplibrary.App;

import android.os.Bundle;

import com.sum.library.app.BaseActivity;

/**
 * Created by sdl on 2017/12/29.
 */

public abstract class BaseAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}