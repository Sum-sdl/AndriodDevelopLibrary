package com.sum.library.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sum.library.app.common.ActivePresent;
import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;
import com.sum.library.domain.UiViewModel;
import com.sum.library.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment implements UiViewModel {

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

    protected ActivePresent mPresent;

    private boolean mIsInflateView = false;//在onViewCreated执行后进行数据加载

    private boolean mIsMulti = false;

    protected Context mContext;

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public void expandActionDeal(ActionState state) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = new ActivePresent(this);
        BaseViewModel viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.registerActionState(this,
                    actionState -> {
                        if (actionState != null) {
                            mPresent.dealActionState((ActionState) actionState, this);
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
            printFragmentLife("onViewCreated-initParams");
            initParams(view);
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && mIsMulti) {
            onUserVisible();
        }
        printFragmentLife("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
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
        mContext = context;
        printFragmentLife("onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        printFragmentLife("onDetach");
    }

    public View getCacheView() {
        if (mWRView == null) {
            return null;
        }
        return mWRView.get();
    }

    public <T extends View> T findViewById(int id) {
        return getCacheView().findViewById(id);
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

    /**
     * 方法在Fragment可见的时候加载数据
     * 子类可根据需要判断使用已经加载完成过一次
     */
    public void onUserFirstVisible() {
        printFragmentLife("onUserFirstVisible");
    }

    /**
     * fragment可见（ViewPager切换回来或者onResume）
     */
    public void onUserVisible() {
        printFragmentLife("onUserVisible");
    }

    /**
     * fragment不可见（ViewPager切换掉或者onPause）
     */
    public void onUserInvisible() {
        printFragmentLife("onUserInvisible");
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

    public int getColorByResId(int colorResId) {
        return ContextCompat.getColor(mContext, colorResId);
    }

    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(getContext(), clazz));
    }

    protected void printFragmentLife(String fun) {
        if (PRINT_LIFE) {
            String ids = Integer.toHexString(System.identityHashCode(this));
            Log.e("fragment_life", getClass().getSimpleName() + " " + ids + "->" + fun);
        }
    }
}
