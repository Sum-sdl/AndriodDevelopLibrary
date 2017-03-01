package com.sum.library.app.common;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.sum.library.domain.BaseValue;
import com.sum.library.domain.ContextView;
import com.sum.library.utils.ToastUtil;


/**
 * Created by Sum on 16/6/23.
 * 实现常用数据代理处理
 */
public final class ActivePresent implements ContextView {

    public LoadingView loadingView;

    public RefreshView refreshView;

    public ActivePresent(Context context) {
        this.loadingView = new LoadingViewImpl(context);
        if (context instanceof RefreshLoadListener) {
            refreshView = new RefreshViewImpl((RefreshLoadListener) context);
        } else {
            throw new RuntimeException("Context not implement RefreshLoadListener");
        }
    }

    public ActivePresent(Fragment fragment) {
        this.loadingView = new LoadingViewImpl(fragment.getContext());
        if (fragment instanceof RefreshLoadListener) {
            refreshView = new RefreshViewImpl((RefreshLoadListener) fragment);
        } else {
            throw new RuntimeException("Fragment not implement RefreshLoadListener");
        }
    }

    @Override
    public Object getValue(int type) {
        return null;
    }

    @Override
    public void showValue(int type, Object obj) {
        if (type == BaseValue.TYPE_TOAST) {
            ToastUtil.showToastShort((String) obj);
        } else if (type == BaseValue.TYPE_DIALOG_PROGRESS_SHOW) {
            loadingView.showLoading((String) obj);
        } else if (type == BaseValue.TYPE_DIALOG_LOADING) {
            loadingView.showLoading();
        } else if (type == BaseValue.TYPE_DIALOG_HIDE) {
            loadingView.hideLoading();
        }
    }
}
