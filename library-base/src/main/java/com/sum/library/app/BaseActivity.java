package com.sum.library.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;

import com.sum.library.app.common.ActiveDefaultImpl;
import com.sum.library.app.common.ICommonActive;
import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;
import com.sum.library.app.common.UiAction;
import com.sum.library.utils.AppUtils;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements UiAction {

    //上下文
    protected Context mContext;

    //统一的ViewModel操作数据处理
    protected ICommonActive mUiActive;

    //布局id
    protected abstract int getLayoutId();

    //kotlin 不需要实现view 初始化
    protected abstract void initParams();

    //加载数据的调用模板方法,首次加载不去调用,如果需要调用,手动调用
    public void loadData() {

    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public void expandActionDeal(ActionState state) {

    }

    //界面初始化后最先调用的模板方法，处理一些
    protected void onCreateDoFirst(Bundle savedInstanceState) {

    }

    //状态栏背景 颜色
    protected int statusBarColor() {
        return 0;
    }

    //默认采用lib中的样式处理
    protected ICommonActive getCommandActive() {
        return new ActiveDefaultImpl(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDoFirst(savedInstanceState);
        setContentView(getLayoutId());
        if (statusBarColor() != 0) {//状态栏颜色
            AppUtils.setColor(this, statusBarColor());
        }

        mContext = this;
        mUiActive = getCommandActive();

        BaseViewModel presenter = getViewModel();
        if (presenter != null) {
            presenter.registerActionState(this, new Observer<ActionState>() {
                @Override
                public void onChanged(ActionState actionState) {
                    if (actionState != null && mUiActive != null) {
                        mUiActive.doActionCommand(actionState, BaseActivity.this);
                    }
                }
            });
        }

        initParams();
    }

    //useful
    public void setDrawableTint(Drawable drawable, int colorRes) {
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, colorRes));
    }

    public Drawable getTintDrawable(int drawableRes, int colorRes) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableRes);
        if (colorRes != -1) {
            setDrawableTint(drawable, colorRes);
        }
        return drawable;
    }

    public Drawable getTintDrawable(int drawableRes) {
        return getTintDrawable(drawableRes, -1);
    }


    public int getColorByResId(int colorResId) {
        return ContextCompat.getColor(this, colorResId);
    }


    //base
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
