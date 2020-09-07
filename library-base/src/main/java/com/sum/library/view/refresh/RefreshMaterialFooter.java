package com.sum.library.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.simple.SimpleComponent;
import com.sum.library.R;
import com.sum.library.view.drawble.MaterialProgressDrawable;

/**
 * Created by sdl on 2020/8/28
 */
public class RefreshMaterialFooter extends SimpleComponent implements RefreshFooter {

    public static String TIP = "加载中...";
    public static String NO_MORE_TIP = "我是有底线的";

    protected RefreshKernel mRefreshKernel;

    //加载的文本提示
    private TextView mTipTextView, mNoMoreText;
    private View mNoMoreView;

    private MaterialProgressDrawable mDrawable;
    private int[] colors;

    protected boolean mNoMoreData = false;

    //<editor-fold desc="RelativeLayout">
    public RefreshMaterialFooter(Context context) {
        this(context, null);
    }

    protected RefreshMaterialFooter(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        //布局位置样式
        //内容跟随移动
        mSpinnerStyle = SpinnerStyle.Translate;

        View.inflate(context, R.layout.pub_refresh_material_footer, this);
        //初始化ui
        mNoMoreView = findViewById(R.id.no_more_data);
        mTipTextView = findViewById(R.id.loading_tip);
        mTipTextView.setText(TIP);
        mNoMoreText = findViewById(R.id.tv_no_more_data);
        mNoMoreText.setText(NO_MORE_TIP);
        //view
        ImageView loading = findViewById(R.id.loading);

        //父容器
        View ll_footer = findViewById(R.id.ll_footer);
        MaterialProgressDrawable drawable = new MaterialProgressDrawable(context, ll_footer);
        drawable.setColorSchemeColors(ContextCompat.getColor(context, R.color.pub_loading_small_bg_color));
        drawable.setAlpha(255);
        loading.setImageDrawable(drawable);
        mDrawable = drawable;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawable != null) {
            if (mDrawable.isRunning()) {
                mDrawable.stop();
            }
        }
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        super.onInitialized(kernel, height, maxDragHeight);
        mRefreshKernel = kernel;
        //自动加载下一页
        kernel.getRefreshLayout().setEnableAutoLoadMore(true);
    }

    //onReleased 之后，执行回弹，回调接口OnRefreshListener，onStateChanged->开始执行动画
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (mDrawable != null) {
            if (colors != null && colors.length > 0) {
                mDrawable.setColorSchemeColors(colors);
                colors = null;
            }
            mDrawable.start();
        }
    }

    //<editor-fold desc="RefreshHeader">
    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (mDrawable != null) {
            mDrawable.stop();
        }
        return 0;//延迟500毫秒之后再弹回
    }

    //下拉刷新,会重置状态
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            if (noMoreData) {
                mNoMoreView.setVisibility(VISIBLE);
                mTipTextView.setVisibility(View.INVISIBLE);
            } else {
                mNoMoreView.setVisibility(GONE);
                mTipTextView.setVisibility(View.VISIBLE);
            }
        }
        return true;
    }

    //具体的色值
    public RefreshMaterialFooter setRefreshColor(int... colors) {
        this.colors = colors;
        return this;
    }

    public TextView getTipTextView() {
        return mTipTextView;
    }

    public TextView getNoMoreTextView() {
        return mNoMoreText;
    }

}
