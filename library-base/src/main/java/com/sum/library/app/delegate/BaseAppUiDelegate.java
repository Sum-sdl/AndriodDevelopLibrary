package com.sum.library.app.delegate;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sum.library.app.common.ActivePresent;
import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;
import com.sum.library.domain.UiViewModel;

/**
 * Created by sdl on 2019-06-22.
 * 统一处理UI界面基础功能代理类,fragment、activity通用
 */
public abstract class BaseAppUiDelegate implements IViewDelegate, UiViewModel {

    protected Context mContext;

    private LifecycleOwner mLifecycle;
    private FragmentActivity mActivity;
    private Fragment mFragment;//可能为null

    //活动数据处理
    private ActivePresent mPresent;
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
        printLifeLog("onAttach:" + activity.getClass().getSimpleName());
        mContext = activity;
        mActivity = activity;
        mLifecycle = lifecycleOwner;
        mFragment = fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, Bundle intentExtras) {
        printLifeLog("onCreate");
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
        printLifeLog("onCreateView");
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }


    public void init() {
        printLifeLog("init");
        if (mRootView != null) {
            initParams(mRootView);
            loadData();
        }
    }

    @Override
    public void onFragmentFirstVisible() {
        //viewpager中fragment首次可见时调用
        printLifeLog("onFragmentFirstVisible");
    }

    @Override
    public void onResume() {
        printLifeLog("onResume");
    }

    @Override
    public void onStop() {
        printLifeLog("onStop");
    }

    @Override
    public void onDestroy() {
        printLifeLog("onDestroy");
    }

    @Override
    public void onNewIntent(Intent intent) {
        printLifeLog("onNewIntent");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        printLifeLog("onActivityResult:" + requestCode + "," + requestCode);
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public void expandActionDeal(ActionState state) {
        //viewModel常用统一扩展操作
    }

    //模板方法打印控制
    protected boolean needPrintLifeLog() {
        return false;
    }

    private void printLifeLog(String fun) {
        if (needPrintLifeLog()) {
            Log.e("ui_life", getClass().getSimpleName() + ":" + getObjectId() + "->" + fun);
        }
    }

    protected String getObjectId() {
        return Integer.toHexString(System.identityHashCode(this));
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

    public LifecycleOwner getLifecycleOwner() {
        return mLifecycle;
    }

    public ActivePresent getActivePresent() {
        return mPresent;
    }

    //启动目标Activity
    public void startActivity(Class targetClass) {
        Intent intent = new Intent(mContext, targetClass);
        startActivity(intent);
    }

    public void startActivity(Intent intent) {
        startActivityForResult(intent, -1);
    }

    //注册在fragment中，通过fragment启动，否则通过activity启动
    public void startActivityForResult(Intent intent, int requestCode) {
        if (getFragment() != null) {
            getFragment().startActivityForResult(intent, requestCode);
        } else {
            getActivity().startActivityForResult(intent, requestCode);
        }
    }

    //关闭Activity里面的fragment
    public void closeBackStackFragment() {
        FragmentManager fragmentManager;
        if (getFragment() != null) {
            fragmentManager = getFragment().getFragmentManager();
        } else {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        if (fragmentManager != null) {
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                fragmentManager.popBackStack();
            }
        }
    }

    //Resource
    public Drawable getDrawableWithTint(int drawableRes, int colorRes) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableRes);
        if (colorRes != -1 && drawable != null) {
            DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, colorRes));
        }
        return drawable;
    }

    public int getColorByResId(int colorResId) {
        return ContextCompat.getColor(mContext, colorResId);
    }

    public Drawable getDrawableByResId(int drawableRes) {
        return ContextCompat.getDrawable(mContext, drawableRes);
    }

    //ui function
    public void showLoadingDilog() {
        mPresent.loadingView.showLoading();
    }

    public void hideLoadingDilog() {
        mPresent.loadingView.hideLoading();
    }

    public void hideLoadingDilog(String msg) {
        mPresent.loadingView.showLoading(msg);
    }

    public void toast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
