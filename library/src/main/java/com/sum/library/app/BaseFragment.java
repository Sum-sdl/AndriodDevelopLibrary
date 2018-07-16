package com.sum.library.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sum.library.app.common.ActivePresent;
import com.sum.library.app.common.LoadingView;
import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;
import com.sum.library.domain.UiViewModel;
import com.sum.library.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment implements LoadingView, UiViewModel {

    public static boolean PRINT_LIFE = false;

    //缓存View对象
    private WeakReference<View> mWRView;

    //首次创建调用（Kotlin通过id查布局需要在onViewCreated执行后才能使用）
    protected abstract void initParams(View view);

    //加载数据
    protected void loadData() {

    }

    //初始化布局
    protected abstract int getLayoutId();

    private ActivePresent mPresent;

    private boolean mIsNeedLoadData = false;//view创建完成后，状态是当前可见的

    private boolean mIsPrepared = false;//view是否已经创建完成，可以加载数据

    private boolean mIsInflateView = false;//在onViewCreated执行后进行数据加载

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = new ActivePresent(this);

        BaseViewModel viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.registerActionState(this,
                    actionState -> {
                        if (actionState != null) {
                            mPresent.dealActionState((ActionState) actionState);
                        }
                    });
        }

        printFragmentLife("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        printFragmentLife("onCreateView");
        View cacheView = null;
        if (mWRView != null) {
            cacheView = getCacheView();
        }
        if (cacheView == null) {
            cacheView = inflater.inflate(getLayoutId(), container, false);
            mWRView = new WeakReference<>(cacheView);
            mIsInflateView = true;
        } else {
            mIsInflateView = false;
            ViewParent parent = cacheView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(cacheView);
                Logger.e("base fragment remove from parent  " + this.getClass().getName());
            }
        }
        return cacheView;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        if (mIsInflateView) {
            printFragmentLife("onViewCreated-initParams");
            initParams(view);
            loadData();
        }
        mIsPrepared = true;
        onVisibleLoadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        printFragmentLife("onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        printFragmentLife("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        printFragmentLife("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        printFragmentLife("onDestroy");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        printFragmentLife("onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        printFragmentLife("onDetach");
    }

    public View getCacheView() {
        return mWRView.get();
    }

    public <T extends View> T _findViewById(int id) {
        return getCacheView().findViewById(id);
    }

    @Override
    public void showLoading() {
        mPresent.loadingView.showLoading();
    }

    @Override
    public void showLoading(String msg) {
        mPresent.loadingView.showLoading(msg);
    }

    @Override
    public void showLoading(String msg, boolean cancelable) {
        mPresent.loadingView.showLoading(msg, cancelable);
    }

    @Override
    public void showProgressLoading(String msg, boolean cancelable) {
        mPresent.loadingView.showProgressLoading(msg, cancelable);
    }

    @Override
    public void hideLoading() {
        mPresent.loadingView.hideLoading();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//获取fragment的可见状态
            mIsNeedLoadData = true;
            onVisibleLoadData();
        } else {
            mIsNeedLoadData = false;
        }
    }

    //首次加载和viewpager中可见加载
    private void onVisibleLoadData() {
        if (mIsNeedLoadData && mIsPrepared) {
            onFragmentVisibleLoadData();
        }
    }

    /**
     * 方法在Fragment可见的时候加载数据
     * 子类可根据需要判断使用已经加载完成过一次
     */
    protected void onFragmentVisibleLoadData() {
    }


    //关闭Activity里面的fragment
    public void closeBackStackFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                fragmentManager.popBackStack();
            }
        }
    }

    protected void printFragmentLife(String fun) {
        if (PRINT_LIFE) {

            String ids = Integer.toHexString(System.identityHashCode(this));

            Log.e("life", getClass().getSimpleName() + " " + ids + "->" + fun);
        }
    }
}
