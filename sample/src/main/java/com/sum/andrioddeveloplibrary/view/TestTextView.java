package com.sum.andrioddeveloplibrary.view;

import android.content.Context;
import android.graphics.Canvas;
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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.e("onSizeChanged w:" + w + " ow:" + oldw + " h:" + h + " oh:" + oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);

        Logger.e("onMeasure w:" + widthMeasureSpec + " h:" + heightMeasureSpec + " m:" + mode + " s:" + size + " h:" + hsize);

    }

    int i = 0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //动态的来设置子view的layout位置
        Logger.e(changed + " onLayout left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom+" i "+i);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.e("onDraw");
//        canvas.drawColor(Color.BLACK);
    }
}
