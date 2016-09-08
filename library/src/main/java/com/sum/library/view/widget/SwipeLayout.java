package com.sum.library.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Summer on 2016/9/5.
 */
public class SwipeLayout extends FrameLayout {

    //LayoutParams 是父容器的布局参数

    public SwipeLayout(Context context) {
        this(context, null, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
