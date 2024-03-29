package com.sum.library.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

/**
 * Created by sdl on 2017/3/6.
 * 用于快速集成组合View的场景
 */

public abstract class BaseBar extends FrameLayout {

    protected abstract void initParams();

    protected abstract int getLayoutId();

    protected Context mContext;
    protected View mView;
    private boolean mIsFirstInit = true;

    public BaseBar(Context context) {
        super(context);
        if (mIsFirstInit) {
            initLayout(context);
            mIsFirstInit = false;
        }
    }

    public BaseBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mIsFirstInit) {
            initLayout(context);
            mIsFirstInit = false;
        }
    }

    private void initLayout(Context context) {
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(getLayoutId(), this, true);
        initParams();
    }

    public void startActivity(Class activity) {
        getContext().startActivity(new Intent(getContext(), activity));
    }

    public void startActivity(Class activity, Bundle bundle) {
        Intent data = new Intent(getContext(), activity);
        data.putExtras(bundle);
        getContext().startActivity(data);
    }

    public void startActivityForResult(Class activity, int reqCode) {
        Intent data = new Intent(getContext(), activity);
        ((Activity) getContext()).startActivityForResult(data, reqCode);
    }

    public int getColorByResId(int colorRes) {
        return ContextCompat.getColor(mContext, colorRes);
    }

    public Drawable getDrawableByResId(int drawableRes) {
        return ContextCompat.getDrawable(mContext, drawableRes);
    }


}
