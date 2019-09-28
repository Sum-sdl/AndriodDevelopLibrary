package com.sum.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by sdl on 2017/3/6.
 */

public abstract class BaseAppBar extends FrameLayout {

    protected abstract void initParams();

    protected abstract int getLayoutId();

    protected Context mContext;
    protected View mView;
    private boolean mIsFirstInit = true;

    public BaseAppBar(Context context) {
        super(context);
        if (mIsFirstInit) {
            initLayout(context);
            mIsFirstInit = false;
        }
    }

    public BaseAppBar(Context context, AttributeSet attrs) {
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

    public <T extends View> T _findViewById(int id) {
        return mView.findViewById(id);
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

    public int getColorRes(int colorRes) {
        return ContextCompat.getColor(mContext, colorRes);
    }

}
