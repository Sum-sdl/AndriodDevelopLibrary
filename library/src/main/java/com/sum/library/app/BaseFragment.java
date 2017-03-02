package com.sum.library.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sum.library.R;
import com.sum.library.app.common.ActivePresent;
import com.sum.library.app.common.LoadingView;
import com.sum.library.app.common.RefreshLoadListener;
import com.sum.library.app.common.RefreshView;
import com.sum.library.domain.ContextView;
import com.sum.library.utils.Logger;
import com.sum.library.utils.ToastUtil;
import com.sum.library.view.Helper.PhotoHelper;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayout;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayoutDirection;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment implements ContextView, RefreshView, RefreshLoadListener, LoadingView {

    private static final String EXTRA_RESTORE_PHOTO = "extra_photo";

    private static final boolean DEBUG = false;
    //缓存View对象
    private WeakReference<View> mWRView;

    //首次创建调用
    protected abstract void initParams(View view);

    //手动初始化布局
    protected abstract int getLayoutId();

    //统一拍照帮助类
    protected PhotoHelper mPhotoHelper;

    private ActivePresent mPresent;


    private boolean mIsNeedLoadData = false;//view创建完成后，状态是当前可见的

    private boolean mIsPrepared = false;//view是否已经创建完成，可以加载数据


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = new ActivePresent(this);
        mPhotoHelper = new PhotoHelper(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View cacheView = null;
        if (mWRView != null) {
            cacheView = getCacheView();
        }
        if (cacheView == null) {
//            View view = x.view().inject(this, inflater, container);
            cacheView = inflater.inflate(getLayoutId(), container, false);
            mWRView = new WeakReference<>(cacheView);
            initParams(cacheView);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPrepared = true;
        loadData();
    }

    public View getCacheView() {
        return mWRView.get();
    }

    public <T> T _findViewById(int id) {
        return (T) getCacheView().findViewById(id);
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
    public void hideLoading() {
        mPresent.loadingView.hideLoading();
    }

    @Override
    public void onRefreshLoadData() {

    }

    @Override
    public void onRefreshNoMore() {
        ToastUtil.showToastShort(R.string.no_more);
    }

    @Override
    public void initRefresh(SwipeRefreshLayout refresh, SwipeRefreshLayoutDirection direction) {
        mPresent.refreshView.initRefresh(refresh, direction);
    }

    @Override
    public void refreshTop() {
        mPresent.refreshView.refreshTop();
    }

    @Override
    public void refreshMore() {
        mPresent.refreshView.refreshMore();
    }

    @Override
    public void setTotalSize(int total) {
        mPresent.refreshView.setTotalSize(total);
    }

    @Override
    public int getPageIndex() {
        return mPresent.refreshView.getPageIndex();
    }

    @Override
    public Object getValue(int type) {
        return mPresent.getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        mPresent.showValue(type, obj);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        File photo = mPhotoHelper.getPhoto();
        if (photo != null) {
            outState.putSerializable(EXTRA_RESTORE_PHOTO, photo);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            File photo = (File) savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
            if (photo != null) {
                mPhotoHelper.setPhoto(photo);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//获取fragment的可见状态
            mIsNeedLoadData = true;
            loadData();
        } else {
            mIsNeedLoadData = false;
        }
    }

    //首次加载和多次加载
    private void loadData() {
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

}
