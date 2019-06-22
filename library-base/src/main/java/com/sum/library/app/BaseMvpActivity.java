package com.sum.library.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.sum.library.domain.mvp.IViewDelegate;
import com.sum.library.utils.AppUtils;

/**
 * Created by sdl on 2019-06-22.
 */
public abstract class BaseMvpActivity extends FragmentActivity {

    private IViewDelegate mViewDelegate;

    //UI界面代理类
    protected abstract Class<? extends IViewDelegate> getViewDelegateClass();

    private void checkOrCreateDelegate() {
        if (mViewDelegate == null) {
            try {
                mViewDelegate = getViewDelegateClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("create IDelegate error");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("create IDelegate error");
            }
        }
    }

    //状态栏背景 颜色
    protected int statusBarColor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (statusBarColor() != 0) {//状态栏颜色
            AppUtils.setColor(this, statusBarColor());
            if (statusBarColor() == Color.WHITE) {
                AppUtils.setDark(this);
            }
        }
        checkOrCreateDelegate();
        mViewDelegate.onCreate(this, savedInstanceState, getIntent().getExtras(), this);
        setContentView(mViewDelegate.onCreateView(getLayoutInflater(), null, savedInstanceState));
        mViewDelegate.init();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        checkOrCreateDelegate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewDelegate != null) {
            mViewDelegate.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mViewDelegate != null) {
            mViewDelegate.onStop();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mViewDelegate != null) {
            mViewDelegate.onNewIntent(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mViewDelegate != null) {
            mViewDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewDelegate != null) {
            mViewDelegate.onDestroy();
        }
        mViewDelegate = null;
    }


}
