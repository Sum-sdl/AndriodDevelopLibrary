package com.sum.andrioddeveloplibrary.testview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sum.library.utils.Logger;

/**
 * Created by Summer on 2016/9/8.
 */
public class TestView extends FrameLayout {
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.e("onFinishInflate");
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        Logger.e("addView:" + index);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Logger.e("generateLayoutParams attrs:"+attrs);
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {

        Logger.e("addView LayoutParams is null? -> " + p);

        return super.generateLayoutParams(p);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.e("onSizeChanged ");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       /* int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int width = getChildMeasureSpec(widthMeasureSpec, 0, child.getLayoutParams().width);
            int heigth = getChildMeasureSpec(widthMeasureSpec, 0, child.getLayoutParams().height);

            int i1 = resolveSizeAndState(child.getLayoutParams().width, widthMeasureSpec,0);
            int i2 = resolveSizeAndState(child.getLayoutParams().height, heightMeasureSpec,0);

            Logger.e("-------"+width+" "+heigth+" "+i1+" "+i2+" "+child.getLayoutParams().width+ " "+child.getLayoutParams().height);

            child.measure(width, heigth);
        }

//        measureChildren(widthMeasureSpec,heightMeasureSpec);
        //确定最后的View大小
        setMeasuredDimension(-2,200);*/
    }


   /* @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //动态的来设置子view的layout位置
        Logger.e(changed + " onLayout left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            Logger.e("w:"+layoutParams.width+" "+layoutParams);
            childAt.layout(10, 50, 10 + layoutParams.width, 50 + layoutParams.height);
        }

    }*/
}
