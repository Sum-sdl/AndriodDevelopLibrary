package com.sum.library.app.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.domain.ActionState;


/**
 * Created by Sum on 16/6/23.
 * 实现常用数据代理处理
 */
public final class ActivePresent {

    public LoadingView loadingView;

    public ActivePresent(Context context) {
        this.loadingView = new LoadingViewImpl(context);
    }

    public ActivePresent(Fragment fragment) {
        this.loadingView = new LoadingViewImpl(fragment.getContext());
    }

    public void dealActionState(ActionState state) {
        int action = state.getState();
        if (action == ActionState.TOAST) {
            if (!TextUtils.isEmpty(state.getMsg())) {
                ToastUtils.showShort(state.getMsg());
            }
        } else if (action == ActionState.DIALOG_HIDE) {
            loadingView.hideLoading();
        } else if (action == ActionState.DIALOG_LOADING) {
            loadingView.showLoading();
        } else if (action == ActionState.DIALOG_PROGRESS_SHOW) {
            if (!TextUtils.isEmpty(state.getMsg())) {
                loadingView.showProgressLoading(state.getMsg(), true);
            }
        }
    }
}
