package com.sum.library.app.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.domain.ActionState;


/**
 * Created by Sum on 16/6/23.
 * 统一UI通用处理
 */
public final class ActivePresent {

    public LoadingView loadingView;

    public ActivePresent(Context context) {
        this.loadingView = new LoadingViewImpl(context);
    }

    public ActivePresent(Fragment fragment) {
        this.loadingView = new LoadingViewImpl(fragment.getContext());
    }

    public void setLoadingView(LoadingView loadingView) {
        if (loadingView != null) {
            this.loadingView = loadingView;
        }
    }

    public void dealActionState(ActionState state) {
        int action = state.getState();
        switch (action) {
            case ActionState.TOAST:
                if (!TextUtils.isEmpty(state.getMsg())) {
                    ToastUtils.showShort(state.getMsg());
                }
                break;

            case ActionState.DIALOG_HIDE:
                loadingView.hideLoading();
                break;

            case ActionState.NET_ERROR:
                loadingView.hideLoading();
                break;

            case ActionState.DIALOG_LOADING:
                if (!TextUtils.isEmpty(state.getMsg())) {
                    loadingView.showLoading(state.getMsg());
                } else {
                    loadingView.showLoading();
                }
                break;

            case ActionState.DIALOG_PROGRESS_SHOW:
                if (!TextUtils.isEmpty(state.getMsg())) {
                    loadingView.showProgressLoading(state.getMsg(), true);
                }
                break;
        }
    }
}
