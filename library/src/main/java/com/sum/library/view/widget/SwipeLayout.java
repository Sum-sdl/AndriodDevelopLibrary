package com.sum.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sum.library.R;
import com.sum.library.utils.Logger;

/**
 * Created by Summer on 2016/9/5.
 */
public class SwipeLayout extends FrameLayout {

    private boolean mDragEnable;

    private ViewDragHelper mDragHelper;

    //上层可滚动的View
    private View mTouchView;

    //上层View水平方向可以拖动的距离
    private int mDragRange;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwipeLayout);

        mDragRange = array.getDimensionPixelSize(R.styleable.SwipeLayout_swipe_drag_distance, 0);

        array.recycle();

        mDragHelper = ViewDragHelper.create(this, mDragHeCallback);

        mDragEnable = true;
    }

    public void setDragEnable(boolean dragEnable) {
        mDragEnable = dragEnable;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mTouchView = getChildAt(getChildCount() - 1);
        }
    }

    public void close() {
        smoothSlideTo(0);
    }

    public void open() {
        smoothSlideTo(1);
    }

    //平滑滚动View到固定位置
    private void smoothSlideTo(int isOpen) {
        if (mDragHelper.smoothSlideViewTo(mTouchView, getPaddingLeft() + isOpen * mDragRange, getPaddingTop())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private ViewDragHelper.Callback mDragHeCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mDragEnable && mTouchView == child;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            // 这里要返回控件的getPaddingTop() + mTopLp.topMargin，否则有margin和padding快速滑动松手时会上下跳动
            return getPaddingTop();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            //设置水平方向上可滚动的距离
            return mDragRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //限制View水平方向上的位置
            int minLeft, maxLeft;
            minLeft = getPaddingLeft() - mDragRange;
            maxLeft = getPaddingLeft();

            int min = Math.min(Math.max(minLeft, left), maxLeft);

            Logger.e("clampViewPositionHorizontal left:" + left + " dx:" + dx+" min:"+min);

            return min;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Logger.e("onViewReleased xvel:" + xvel + " yvel:" + yvel);

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

//            Logger.e("onViewPositionChanged left:" + left + " dx:" + dx);
        }
    };

}
