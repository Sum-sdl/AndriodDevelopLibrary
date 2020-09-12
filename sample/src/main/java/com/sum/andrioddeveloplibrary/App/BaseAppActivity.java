package com.sum.andrioddeveloplibrary.App;

import android.graphics.Color;
import android.os.Bundle;

import com.sum.library.app.BaseActivity;
import com.sum.library.utils.AppUtils;
import com.sum.library_network.Retrofit2Helper;

import retrofit2.Retrofit;

/**
 * Created by sdl on 2017/12/29.
 */

public abstract class BaseAppActivity extends BaseActivity {

    protected Retrofit mRetrofit = Retrofit2Helper.getRetrofit();

    //Tip statusBarColor
    //如果状态栏透明，推荐BarUtils.setStatusBarAlpha(this)，不要全透明
    //给状态栏设置颜色 statusBarColor 设置颜色比较方便

    @Override
    protected int statusBarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setStatusBarLightMode(this,true);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
