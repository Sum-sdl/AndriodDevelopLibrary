package com.sum.library.view.refresh;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.simple.SimpleComponent;

/**
 * Created by sdl on 2020/8/28
 */
public class RefreshEmptyHeader extends SimpleComponent implements RefreshHeader {
    protected RefreshKernel mRefreshKernel;
    private boolean mNeedRefreshCallback = true;

    private TextView mTipView;

    //<editor-fold desc="RelativeLayout">
    public RefreshEmptyHeader(Context context) {
        this(context, null);
    }

    public RefreshEmptyHeader(Context context, boolean refreshCallback, String text) {
        this(context, null);
        mNeedRefreshCallback = refreshCallback;
        if (!TextUtils.isEmpty(text)) {
            mTipView.setText(text);
        }
    }

    protected RefreshEmptyHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        //增加一个TextView
        mTipView = new TextView(context);
        mTipView.setText("");
        mTipView.setGravity(Gravity.CENTER);
        mTipView.setPadding(0, dp2px(20), 0, dp2px(18));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -2);
        addView(mTipView, params);
    }

    private int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        //忽略更多底部内容显示
        return false;
    }
}