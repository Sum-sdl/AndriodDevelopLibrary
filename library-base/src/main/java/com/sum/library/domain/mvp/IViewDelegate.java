package com.sum.library.domain.mvp;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sdl on 2019-06-22.
 * Activity、Fragment代理类
 */
public interface IViewDelegate {


    /**
     * 界面首次初始化，只调用一次
     *
     * @param context            启动的Activity
     * @param savedInstanceState 保存的信息
     * @param intentExtras       启动传递的参数
     * @param lifecycleOwner     Activity、Fragment的Lifecycle
     */
    void onCreate(FragmentActivity context, Bundle savedInstanceState, Bundle intentExtras, LifecycleOwner lifecycleOwner);

    /**
     * @return 界面需要显示的View
     */
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 统一初始化操作方法
     */
    void init();

    /**
     * fragment在viewpager中首次可见时调用
     */
    void onFragmentFirstVisible();

    //life fun

    void onResume();

    void onStop();

    void onDestroy();

    void onNewIntent(Intent intent);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
