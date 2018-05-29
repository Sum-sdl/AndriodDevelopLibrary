package com.sum.library.app.common;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.domain.BaseValue;
import com.sum.library.domain.ContextView;


/**
 * Created by Sum on 16/6/23.
 * 实现常用数据代理处理
 */
public final class ActivePresent implements ContextView {

    public LoadingView loadingView;

    public ActivePresent(Context context) {
        this.loadingView = new LoadingViewImpl(context);
    }

    public ActivePresent(Fragment fragment) {
        this.loadingView = new LoadingViewImpl(fragment.getContext());
    }

    @Override
    public Object getValue(int type) {
        return null;
    }

    @Override
    public void showValue(int type, Object obj) {
        if (type == BaseValue.TYPE_TOAST) {
            ToastUtils.showShort((String) obj);
        } else if (type == BaseValue.TYPE_DIALOG_PROGRESS_SHOW) {
            loadingView.showProgressLoading((String) obj, true);
        } else if (type == BaseValue.TYPE_DIALOG_LOADING) {
            loadingView.showLoading();
        } else if (type == BaseValue.TYPE_DIALOG_HIDE) {
            loadingView.hideLoading();
        }
    }
}
