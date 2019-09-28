package com.sum.library.app.delegate;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sum.library.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by sdl on 2019-06-22.
 */
public abstract class BaseDelegateFragment extends Fragment {

    private IViewDelegate mViewDelegate;

    //UI界面代理类
    protected abstract Class<? extends IViewDelegate> getViewDelegateClass();

    //在onViewCreated执行后进行数据加载
    private boolean mIsInflateView = false;

    private boolean mIsMulti = false;

    //缓存View对象
    private WeakReference<View> mWRView;

    //界面初始化后最先调用的模板方法
    protected void onCreateFirst(Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateFirst(savedInstanceState);
        checkOrCreateDelegate();
        mViewDelegate.onAttach(getActivity(), this, this);
        //都是v4包兼容类
        mViewDelegate.onCreate(savedInstanceState, getArguments());
    }

    private void checkOrCreateDelegate() {
        if (mViewDelegate == null) {
            try {
                mViewDelegate = getViewDelegateClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("create IDelegate error");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("create IDelegate error");
            } catch (java.lang.InstantiationException e) {
                throw new RuntimeException("create IDelegate error");
            }
        }
    }

    public IViewDelegate getViewDelegate() {
        return mViewDelegate;
    }

    private View getCacheView() {
        if (mWRView == null) {
            return null;
        }
        return mWRView.get();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        checkOrCreateDelegate();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checkOrCreateDelegate();
        View cacheView = null;
        if (mWRView != null) {
            cacheView = getCacheView();
        }
        if (cacheView == null) {
            cacheView = mViewDelegate.onCreateView(inflater, container, savedInstanceState);
            mWRView = new WeakReference<>(cacheView);
            mIsInflateView = true;
        } else {
            mIsInflateView = false;
            ViewParent parent = cacheView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(cacheView);
                Logger.e("base fragment remove from parent  " + this.getClass().getName());
            }
        }
        return cacheView;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        if (mIsInflateView) {
            mViewDelegate.init();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && mIsMulti) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewDelegate != null) {
            mViewDelegate.onDestroy();
        }
        mViewDelegate = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && getCacheView() != null && !mIsMulti) {
            mIsMulti = true;
            onUserFirstVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getCacheView() != null && !mIsMulti) {
            mIsMulti = true;
            onUserFirstVisible();
        } else if (isVisibleToUser && getCacheView() != null && mIsMulti) {
            onUserVisible();
        } else if (!isVisibleToUser && getCacheView() != null && mIsMulti) {
            onUserInvisible();
        } else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mViewDelegate != null) {
            mViewDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 方法在Fragment可见的时候加载数据
     * 子类可根据需要判断使用已经加载完成过一次
     */
    private void onUserFirstVisible() {
        if (mViewDelegate != null) {
            mViewDelegate.onFragmentFirstVisible();
        }
    }

    /**
     * fragment可见（ViewPager切换回来或者onResume）
     */
    private void onUserVisible() {
        if (mViewDelegate != null) {
            mViewDelegate.onResume();
        }
    }

    /**
     * fragment不可见（ViewPager切换掉或者onPause）
     */
    private void onUserInvisible() {
        if (mViewDelegate != null) {
            mViewDelegate.onStop();
        }
    }

}
