package com.sum.library.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sum.library.utils.Logger;

import org.xutils.x;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment {

    private static final boolean DEBUG = false;
    private boolean isInject = false;
    protected View mView;//显示的Fragment布局,对象的缓存使用

    //首次创建调用
    protected abstract void initParams();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (!isInject) {
            isInject = true;
            mView = x.view().inject(this, inflater, container);
            initParams();
        } else {
            ViewParent parent = mView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mView);
                Logger.e("base fragment remove from parent  " + this.getClass().getName());
            }
            if (DEBUG) {
                Logger.v("onCreateView show cache view");
            }
        }

        return mView;
    }

}
