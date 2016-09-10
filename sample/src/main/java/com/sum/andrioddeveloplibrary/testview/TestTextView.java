package com.sum.andrioddeveloplibrary.testview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sum.library.utils.Logger;

/**
 * Created by Summer on 2016/9/5.
 */
public class TestTextView extends TextView {
    public TestTextView(Context context) {
        super(context);
    }

    public TestTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Logger.e("onSizeChanged w:" + w + " ow:" + oldw + " h:" + h + " oh:" + oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.e("onMeasure " + getLayoutParams().height + " " + getLayoutParams().width + " " + getLayoutParams());

        getLayoutParams().width = 1000;
        //设置固定大小
        setMeasuredDimension(400, 100);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //动态的来设置子view的layout位置
        Logger.e(changed + " onLayout left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom);

    }
}
