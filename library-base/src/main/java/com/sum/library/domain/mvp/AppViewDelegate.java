package com.sum.library.domain.mvp;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sum.library.app.common.ActivePresent;
import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;
import com.sum.library.domain.UiViewModel;

/**
 * Created by sdl on 2019-06-22.
 * 统一处理UI界面基础功能代理类
 */
public abstract class AppViewDelegate implements IViewDelegate, UiViewModel {

    protected Context mContext;

    private LifecycleOwner mLifecycle;
    private FragmentActivity mActivity;
    private Fragment mFragment;//可能为null
    //活动数据处理
    protected ActivePresent mPresent;
    //传递的参数
    protected Bundle mIntentExtras;

    private View mRootView;

    //布局id
    protected abstract int getLayoutId();

    //首次创建初始化调用
    protected abstract void initParams(View view);

    //加载数据
    protected void loadData() {

    }

    @Override
    public void onAttach(FragmentActivity activity, Fragment fragment, LifecycleOwner lifecycleOwner) {
        mContext = activity;
        mActivity = activity;
        mLifecycle = lifecycleOwner;
        mFragment = fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, Bundle intentExtras) {
        printFragmentLife("onCreate");
        mIntentExtras = intentExtras;
        mPresent = new ActivePresent(mActivity);
        BaseViewModel viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.registerActionState(mLifecycle,
                    actionState -> {
                        if (actionState != null) {
                            mPresent.dealActionState((ActionState) actionState, this);
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        printFragmentLife("onCreateView");
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }


    public final void init() {
        printFragmentLife("init");
        initParams(mRootView);
        loadData();
    }

    @Override
    public void onFragmentFirstVisible() {
        //viewpager中fragment首次可见时调用
        printFragmentLife("onFragmentFirstVisible");
    }

    @Override
    public void onResume() {
        printFragmentLife("onResume");
    }

    @Override
    public void onStop() {
        printFragmentLife("onStop");
    }

    @Override
    public void onDestroy() {
        printFragmentLife("onDestroy");
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public void expandActionDeal(ActionState state) {
        //viewModel常用统一扩展操作
    }

    public static boolean PRINT_LIFE = false;

    private void printFragmentLife(String fun) {
        if (PRINT_LIFE) {
            String ids = Integer.toHexString(System.identityHashCode(this));
            Log.e("fragment_life", getClass().getSimpleName() + ":" + ids + "->" + fun);
        }
    }


    //expand fun
    public <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    protected FragmentActivity getActivity() {
        return mActivity;
    }

    protected Fragment getFragment() {
        return mFragment;
    }

    public LifecycleOwner getLifecycle() {
        return mLifecycle;
    }
}
