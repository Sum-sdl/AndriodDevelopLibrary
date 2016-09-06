package com.sum.library.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.sum.library.utils.Logger;

/**
 * Created by Summer on 2016/9/5.
 */
public class SwipeLayout extends FrameLayout {


    public SwipeLayout(Context context) {
        this(context, null, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Logger.e("init");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.e("onFinishInflate");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.e("onSizeChanged");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.e("onMeasure");
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //动态的来设置子view的layout位置
        Logger.e(changed + " onLayout left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom);

    }
}
