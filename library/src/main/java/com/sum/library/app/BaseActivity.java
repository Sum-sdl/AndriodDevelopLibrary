package com.sum.library.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.sum.library.app.common.ActivePresent;
import com.sum.library.app.common.LoadingView;
import com.sum.library.domain.ContextView;
import com.sum.library.net.Retrofit2Helper;
import com.sum.library.utils.AppUtils;

import retrofit2.Retrofit;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements ContextView, LoadingView {

    //活动数据处理
    private ActivePresent mPresent;

    //统一网络请求
    protected Retrofit mRetrofit;

    //布局id
    protected abstract int getLayoutId();

    //kotlin 不需要实现view 初始化
    protected abstract void initParams();

    //加载数据
    protected void loadData() {

    }

    //状态栏背景透明
    protected boolean statusBarTranslate() {
        return false;
    }

    //状态栏背景
    // 颜色
    protected int statusBarColor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (statusBarTranslate()) {
            BarUtils.setStatusBarAlpha(this, 0);
        } else if (statusBarColor() != 0) {//状态栏颜色
            AppUtils.setColor(this, statusBarColor());
            if (statusBarColor() == Color.WHITE) {
                AppUtils.setDark(this);
            }
        }

        mRetrofit = Retrofit2Helper.getRetrofit();
        mPresent = new ActivePresent(this);
        initParams();
        loadData();
    }

    @Override
    public void showLoading() {
        mPresent.loadingView.showLoading();
    }

    @Override
    public void showLoading(String msg) {
        mPresent.loadingView.showLoading(msg);
    }

    @Override
    public void showLoading(String msg, boolean cancelable) {
        mPresent.loadingView.showLoading(msg, cancelable);
    }

    @Override
    public void showProgressLoading(String msg, boolean cancelable) {
        mPresent.loadingView.showProgressLoading(msg, cancelable);
    }

    @Override
    public void hideLoading() {
        mPresent.loadingView.hideLoading();
    }


    @Override
    public Object getValue(int type) {
        return mPresent.getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        mPresent.showValue(type, obj);
    }

    //useful
    public void updateDrawableTint(Drawable drawable, int colorRes) {
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, colorRes));
    }

    public Drawable getTintDrawable(int drawableRes, int colorRes) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableRes);
        if (colorRes != -1) {
            updateDrawableTint(drawable, colorRes);
        }
        return drawable;
    }

    public Drawable getTintDrawable(int drawableRes) {
        return getTintDrawable(drawableRes, -1);
    }

    //base
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
