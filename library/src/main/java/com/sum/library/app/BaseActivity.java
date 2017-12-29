package com.sum.library.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;

import com.sum.library.app.common.ActivePresent;
import com.sum.library.app.common.LoadingView;
import com.sum.library.domain.ContextView;
import com.sum.library.net.Retrofit2Helper;
import com.sum.library.view.Helper.PhotoHelper;

import java.io.File;

import retrofit2.Retrofit;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements ContextView, LoadingView {

    private static final String EXTRA_RESTORE_PHOTO = "extra_photo";
    //统一拍照帮助类
    protected PhotoHelper mPhotoHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mRetrofit = Retrofit2Helper.getRetrofit();
        mPhotoHelper = new PhotoHelper(this);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        File photo = mPhotoHelper.getPhoto();
        if (photo != null) {
            outState.putSerializable(EXTRA_RESTORE_PHOTO, photo);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        File photo = (File) savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
        if (photo != null) {
            mPhotoHelper.setPhoto(photo);
        }
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
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
