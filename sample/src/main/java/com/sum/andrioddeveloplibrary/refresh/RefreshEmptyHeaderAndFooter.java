package com.sum.andrioddeveloplibrary.refresh;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;
import com.scwang.smart.refresh.header.falsify.FalsifyAbstract;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2020/8/28
 */
public class RefreshEmptyHeaderAndFooter extends FalsifyAbstract implements RefreshHeader, RefreshFooter {

    private boolean mNeedRefreshCallback = true;

    private TextView mTipView;

    //<editor-fold desc="RelativeLayout">
    public RefreshEmptyHeaderAndFooter(Context context) {
        this(context, null);
    }

    public RefreshEmptyHeaderAndFooter(Context context, boolean refreshCallback, String text) {
        this(context, null);
        mNeedRefreshCallback = refreshCallback;
        if (!TextUtils.isEmpty(text)) {
            mTipView.setText(text);
        }
    }

    protected RefreshEmptyHeaderAndFooter(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        //增加一个TextView
        mTipView = new TextView(context);
        mTipView.setText("上拉加载更多");
        mTipView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, SizeUtils.dp2px(50));
        addView(mTipView, params);
    }

    public RefreshEmptyHeaderAndFooter setNeedRefreshCallback(boolean needRefreshCallback) {
        this.mNeedRefreshCallback = needRefreshCallback;
        return this;
    }

    private void print(String msg) {
        Logger.e(msg);
    }


    //初始化后，只调用一次
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        mRefreshKernel = kernel;
        kernel.getRefreshLayout().setEnableAutoLoadMore(false);
    }

    //手动释放后执行
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (mRefreshKernel != null) {
            //onReleased 的时候 调用 setState(RefreshState.None); 并不会立刻改变成 None
            //而是先执行一个回弹动画，RefreshFinish 是介于 Refreshing 和 None 之间的状态
            //RefreshFinish 用于在回弹动画结束时候能顺利改变为 None
            if (mNeedRefreshCallback) {
                mRefreshKernel.setState(RefreshState.RefreshFinish);
            } else {
                refreshLayout.closeHeaderOrFooter();
            }
        }
    }

    //状态切换
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
        print("onStateChanged:" + oldState + ";" + newState);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        //忽略更多底部内容显示
        return false;
    }
}
