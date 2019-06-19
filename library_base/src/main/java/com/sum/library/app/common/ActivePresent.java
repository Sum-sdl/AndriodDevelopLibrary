package com.sum.library.app.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.sum.library.domain.ActionState;
import com.sum.library.domain.UiViewModel;


/**
 * Created by Sum on 16/6/23.
 * 统一UI通用处理
 */
public final class ActivePresent {

    public LoadingView loadingView;
    private Context mContext;

    public ActivePresent(Context context) {
        this.loadingView = new LoadingViewImpl(context);
        mContext = context;
    }

    public ActivePresent(Fragment fragment) {
        this.loadingView = new LoadingViewImpl(fragment.getContext());
        mContext = fragment.getContext();
    }

    public void setLoadingView(LoadingView loadingView) {
        if (loadingView != null) {
            this.loadingView = loadingView;
        }
    }

    public void dealActionState(ActionState state, UiViewModel viewModel) {
        int action = state.getState();
        switch (action) {
            case ActionState.TOAST:
                if (!TextUtils.isEmpty(state.getMsg())) {
                    Toast.makeText(mContext, state.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;

            case ActionState.DIALOG_HIDE:
                loadingView.hideLoading();
                if (!TextUtils.isEmpty(state.getMsg())) {
                    Toast.makeText(mContext, state.getMsg(), Toast.LENGTH_SHORT).show();
                }
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
        viewModel.expandActionDeal(state);
        //添加对象缓存
        ActionState.release(state);
    }
}
