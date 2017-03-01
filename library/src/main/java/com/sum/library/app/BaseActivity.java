package com.sum.library.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.sum.library.R;
import com.sum.library.app.common.ActivePresent;
import com.sum.library.app.common.LoadingView;
import com.sum.library.app.common.RefreshLoadListener;
import com.sum.library.app.common.RefreshView;
import com.sum.library.domain.ContextView;
import com.sum.library.utils.ToastUtil;
import com.sum.library.view.Helper.PhotoHelper;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayout;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayoutDirection;

import org.xutils.x;

import java.io.File;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseActivity extends FragmentActivity implements ContextView, RefreshLoadListener, RefreshView, LoadingView {

    private static final String EXTRA_RESTORE_PHOTO = "extra_photo";
    //统一拍照帮助类
    protected PhotoHelper mPhotoHelper;

    private ActivePresent mPresent;


    protected abstract void initParams();

    public boolean isNeedInject() {
        return true;
    }

    public <T> T _findViewById(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedInject()) {
            x.view().inject(this);
            initParams();
        }
        mPhotoHelper = new PhotoHelper(this);
        mPresent = new ActivePresent(this);
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
    public void onRefreshLoadData() {

    }

    @Override
    public void onRefreshNoMore() {
        ToastUtil.showToastShort(R.string.no_more);
    }

    @Override
    public void initRefresh(SwipeRefreshLayout refresh, SwipeRefreshLayoutDirection direction) {
        mPresent.refreshView.initRefresh(refresh, direction);
    }

    @Override
    public void refreshTop() {
        mPresent.refreshView.refreshTop();
    }

    @Override
    public void refreshMore() {
        mPresent.refreshView.refreshMore();
    }

    @Override
    public void setTotalSize(int total) {
        mPresent.refreshView.setTotalSize(total);
    }

    @Override
    public int getPageIndex() {
        return mPresent.refreshView.getPageIndex();
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
    public void setTintDrawable(Drawable drawable, int colorRes) {
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, colorRes));
    }

    public Drawable getTintDrawable(int drawableRes, int colorRes) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableRes);
        if (colorRes != -1) {
            setTintDrawable(drawable, colorRes);
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
