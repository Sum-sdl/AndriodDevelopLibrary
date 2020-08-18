package com.sum.andrioddeveloplibrary.refreshview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.simple.SimpleComponent;
import com.sum.andrioddeveloplibrary.R;
import com.sum.library.utils.AppUtils;
import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2020/8/18
 */
public class CustomRefreshHeader extends SimpleComponent implements RefreshHeader {


    //<editor-fold desc="RelativeLayout">
    public CustomRefreshHeader(Context context) {
        this(context, null);
    }

    private View iv_view,ll_content;

    protected CustomRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        View.inflate(context, R.layout.custom_refresh_header, this);

        iv_view = findViewById(R.id.iv_view);
        ll_content = findViewById(R.id.ll_content);

    }

    private void print(String msg) {
        Logger.e(msg);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        print("onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        print("onDetachedFromWindow");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        print("onFinishInflate");
    }

    //初始化后，只调用一次
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        super.onInitialized(kernel, height, maxDragHeight);
        print("onInitialized ->" + height + ":" + maxDragHeight);
    }

    //onReleased 之后，执行回弹，回调接口OnRefreshListener，onStateChanged->开始执行动画
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onStartAnimator(refreshLayout, height, maxDragHeight);
        print("onStartAnimator->" + height + "," + maxDragHeight);
    }

    //手动释放后执行
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onReleased(refreshLayout, height, maxDragHeight);
        print("onReleased");
    }

    //手势联动动画使用
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight);
        print("onMoving->" + isDragging + "," + percent + "," + offset + "," + height + "," + maxDragHeight);
        if (isDragging) {
            int start = Color.parseColor("#FFF44336");
            int edn = Color.parseColor("#FF22C111");
            int i = AppUtils.rgbEvaluate(percent, start, edn);
            ll_content.setBackgroundColor(i);
        }
    }

    //刷新结果回调,由RefreshLayout.finishRefresh触发刷新结果
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        print("onFinish+:" + success);
        return super.onFinish(refreshLayout, success);
    }

    //刷新结束，标记是否还有更多内容，一般用于底部刷新
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        print("setNoMoreData+:" + noMoreData);
        return super.setNoMoreData(noMoreData);
    }

    //状态切换
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
        print("onStateChanged:" + oldState + ";" + newState);
    }
}
