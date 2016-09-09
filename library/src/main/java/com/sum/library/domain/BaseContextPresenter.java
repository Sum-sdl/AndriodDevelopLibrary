package com.sum.library.domain;

import com.sum.library.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by Summer on 2016/9/9.
 * 处理对应界面的业务逻辑
 */
public abstract class BaseContextPresenter implements ContextView {

    private WeakReference<ContextView> mView = null;

    public BaseContextPresenter(ContextView view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public Object getValue(int type) {
        return mView.get().getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        ContextView contextView = mView.get();
        if (contextView != null) {
            contextView.showValue(type, obj);
        } else {
            Logger.e("ContextView is null");
        }
    }

    //根据业务需求子类重写
    public void onCreate() {
    }

    public void onResume() {
    }

    public void onStop() {
    }

    public void onDestory() {
    }
}
