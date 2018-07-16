package com.sum.andrioddeveloplibrary.App;

import android.graphics.Color;
import android.os.Bundle;

import com.sum.library.app.BaseActivity;
import com.sum.library.net.Retrofit2Helper;

import retrofit2.Retrofit;

/**
 * Created by sdl on 2017/12/29.
 */

public abstract class BaseAppActivity extends BaseActivity {

    protected Retrofit mRetrofit = Retrofit2Helper.getRetrofit();

    @Override
    protected int statusBarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
