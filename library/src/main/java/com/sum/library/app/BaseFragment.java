package com.sum.library.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sum.library.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment {

    private static final boolean DEBUG = false;
    //缓存View对象
    private WeakReference<View> mWRView;

    public View getCacheView() {
        if (mWRView == null) {
            return null;
        }
        return mWRView.get();
    }

    //首次创建调用
    protected abstract void initParams();

    //手动初始化布局
    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View cacheView = getCacheView();

        if (cacheView == null) {
//            View view = x.view().inject(this, inflater, container);
            cacheView = inflater.inflate(getLayoutId(), container, false);
            mWRView = new WeakReference<>(cacheView);
            initParams();
        } else {
            ViewParent parent = cacheView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(cacheView);
                Logger.e("base fragment remove from parent  " + this.getClass().getName());
            }
            if (DEBUG) {
                Logger.v("onCreateView show cache view");
            }
        }
        return cacheView;
    }

}
