package com.sum.library.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sdl on 2018/5/8.
 * ViewStub 扩展
 */
public class PubEmptyView extends View {

    public PubEmptyView(Context context) {
        this(context, null, 0);
    }

    public PubEmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PubEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

}
