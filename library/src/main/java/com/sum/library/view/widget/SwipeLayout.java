package com.sum.library.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sum.library.utils.Logger;

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
        Logger.e("init");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.e("onFinishInflate");
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        Logger.e("addView");
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Logger.e("generateLayoutParams 属性");
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        Logger.e("generateLayoutParams 默认");
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        Logger.e("generateLayoutParams ");
        return super.generateLayoutParams(p);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.e("onSizeChanged ");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Logger.e("onMeasure self:"+getLayoutParams().width+" "+MeasureSpec.getSize(widthMeasureSpec));

        ViewGroup.LayoutParams layoutParams = getChildAt(0).getLayoutParams();
        layoutParams.width = 100;

        Logger.e("onMeasure child:" + layoutParams.width + " " + layoutParams.height + " " + layoutParams);

        int childMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, layoutParams.width+100);


        measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);

        Logger.e("onMeasure childSpec:" + MeasureSpec.getSize(childMeasureSpec)+" "+getChildAt(0).getMeasuredWidth());

        setMeasuredDimension(getLayoutParams().width,getLayoutParams().height);
//        getChildMeasureSpec() $ child.measure(cWspec,cHspec);
//        measureChild(getChildAt(0),widthMeasureSpec,heightMeasureSpec);

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //动态的来设置子view的layout位置
        Logger.e(changed + " onLayout left:" + left + " top:" + top + " right:" + right + " bottom:"+bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.e("onDraw");
    }
}
