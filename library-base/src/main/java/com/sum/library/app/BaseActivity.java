package com.sum.library.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.sum.library.app.common.ActivePresent;
import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;
import com.sum.library.domain.UiAction;
import com.sum.library.utils.AppUtils;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements UiAction {

    //活动数据处理
    protected ActivePresent mUiActive;

    protected Context mContext;

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

    //状态栏背景 颜色
    protected int statusBarColor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (statusBarColor() != 0) {//状态栏颜色
            AppUtils.setColor(this, statusBarColor());
            if (statusBarColor() == Color.WHITE) {
                AppUtils.setDark(this);
            }
        }

        mContext = this;
        mUiActive = new ActivePresent(this);

        BaseViewModel presenter = getViewModel();
        if (presenter != null) {
            presenter.registerActionState(this,
                    actionState -> {
                        if (actionState != null) {
                            mUiActive.dealActionState((ActionState) actionState, this);
                        }
                    });
        }

        initParams();
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

    public int getColorByResId(int colorResId) {
        return ContextCompat.getColor(this, colorResId);
    }

    public Drawable getTintDrawable(int drawableRes) {
        return getTintDrawable(drawableRes, -1);
    }

    //base
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
