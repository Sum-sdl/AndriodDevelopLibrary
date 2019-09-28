package com.sum.library.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.sum.library.app.common.ActivePresent;
import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;
import com.sum.library.domain.UiViewModel;
import com.sum.library.utils.AppUtils;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements UiViewModel {

    //活动数据处理
    protected ActivePresent mPresent;

    protected Context mContext;

    //布局id
    protected abstract int getLayoutId();

    //kotlin 不需要实现view 初始化
    protected abstract void initParams();

    //加载数据
    protected void loadData() {

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
        mPresent = new ActivePresent(this);

        BaseViewModel viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.registerActionState(this,
                    actionState -> {
                        if (actionState != null) {
                            mPresent.dealActionState((ActionState) actionState, this);
                        }
                    });
        }

        initParams();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void showLoadingDilog() {
        mPresent.loadingView.showLoading();
    }

    public void hideLoadingDilog() {
        mPresent.loadingView.hideLoading();
    }

    //base
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
