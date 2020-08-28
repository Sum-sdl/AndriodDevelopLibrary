package com.sum.library.app.common;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.widget.Toast;

import com.sum.library.domain.ActionState;


/**
 * Created by Sum on 16/6/23.
 * 统一UI通用处理
 */
public class ActiveDefaultImpl implements ICommonActive {

    private LoadingView loadingView;
    private Context mContext;

    public ActiveDefaultImpl(Activity context) {
        this.loadingView = buildLoadingView(context);
        mContext = context;
    }

    public ActiveDefaultImpl(Fragment fragment) {
        this.loadingView = buildLoadingView(fragment.getActivity());
        mContext = fragment.getContext();
    }

    //子类处理的LoadingView实现
    protected LoadingView buildLoadingView(Activity activity) {
        return new LoadingViewImpl(activity, 2);
    }

    //统一动作处理
    private void dealActionState(ActionState state, UiAction viewModel) {
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

            case ActionState.DIALOG_LOADING_SHOW:
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

    @Override
    public void doActionCommand(ActionState state, UiAction uiAction) {
        dealActionState(state, uiAction);
    }

    @Override
    public LoadingView getLoadingView() {
        return loadingView;
    }
}
