package com.sum.library.view.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sum.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdl on 2017/10/11.
 */

public class ChooseView extends BaseView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private int[] tipText = new int[]{15, 30, 45, 60, 75, 90};

    //默认值颜色
    private int mColorLineBg = 0xffe5e5e5;
    private int mColorLineAccent = 0xfff8b62d;

    private int mColorTextNor = 0xff666666;
    private int mColorTextAccent = mColorLineAccent;
    private int mTextSize = 50;

    private int mColorCircleBg = mColorLineBg;
    private int mColorCircleAccent = mColorLineAccent;


    //背景线的结束点
    private int mBgLineEndPoint;
    //通过区域判断当前触摸的位置
    private List<Rect> mAverageArea;
    private List<Rect> mShowArea;

    //最后选中的索引
    private int mLastIndex = 0;
    //当前展示的索引
    private int mShowIndex = 0;

    //线与文本的距离
    private int mLineToTextDistance = 120;
    //半个区域的宽度
    private int mUnitWidth;
    //当前跟随移动的起始点
    private float mTouchPointX;

    private ChooseChangeListener mListener;

    private TextPaint mPaintText;
    private Paint mLineBg, mLineSel;
    private Paint mCircle, mCircleMove;
    //画圆的半径
    private int mDefaultRadius = 18, mChooseRadius = 30;
    private int mBitmapRadius;

    private Bitmap mBitmap;

    public interface ChooseChangeListener {
        void onChooseChange(int time);
    }

    public ChooseView(Context context) {
        this(context, null);
    }

    public ChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChooseView);
            mTextSize = array.getDimensionPixelSize(R.styleable.ChooseView_workTextSize, 50);
            mColorTextNor = array.getColor(R.styleable.ChooseView_workTextColorNor, 0xff666666);
            mColorTextAccent = array.getColor(R.styleable.ChooseView_workTextColorSelected, 0xfff8b62d);
            mColorLineBg = array.getColor(R.styleable.ChooseView_workLineColorNor, 0xffe5e5e5);
            mColorLineAccent = array.getColor(R.styleable.ChooseView_workLineColorNor, 0xfff8b62d);
            mLineToTextDistance = array.getDimensionPixelSize(R.styleable.ChooseView_workPaddingToLine, 120);
            array.recycle();
        }
        initPaint();
    }

    private void initPaint() {
        mPaintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextAlign(Paint.Align.CENTER);//设置文本从baseLine基准线的点开始绘制的方向
        mPaintText.setColor(mColorLineAccent);
        mPaintText.setTextSize(mTextSize);

        mLineBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLineBg.setColor(mColorLineBg);
        mLineBg.setStyle(Paint.Style.FILL);
        mLineBg.setStrokeWidth(10);

        mLineSel = new Paint(mLineBg);
        mLineSel.setColor(mColorLineAccent);

        mCircle = new Paint(mLineBg);
        mCircle.setColor(mColorCircleBg);
        mCircle.setStrokeCap(Paint.Cap.ROUND);

        mCircleMove = new Paint(mLineBg);
        mCircleMove.setColor(mColorLineAccent);
        mCircleMove.setFilterBitmap(true);
        mCircleMove.setDither(true);
    }


    public void setChooseChangeListener(ChooseChangeListener mListener) {
        this.mListener = mListener;
    }

    public void setChooseTime(int time) {
        int index = 0;
        for (int t : tipText) {
            if (t == time) {
                break;
            }
            index++;
        }
        mLastIndex = mShowIndex = index;
        mTouchPointX = calculateDistanceByIndex(index);
        invalidate();
    }

    public void setBitmapResId(int resId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        mBitmapRadius = mBitmap.getWidth() / 2;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w == oldw) {//修改高度的时候不做计算
            return;
        }
        int length = tipText.length;
        //单个矩阵宽度
        int singleWidth = w / length;
        int unitWidth = singleWidth / 2;
        mTouchPointX = mUnitWidth = unitWidth;
        mBgLineEndPoint = w - mUnitWidth;
        //单选的分快
        mAverageArea = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Rect rect = new Rect();
            rect.left = i * singleWidth;
            rect.top = 0;
            rect.right = (i + 1) * singleWidth;
            rect.bottom = 50;
            mAverageArea.add(rect);
        }
        //选中展示区域
        mShowArea = new ArrayList<>();
        for (int i = 1, j = (length - 1) * 2; i <= j; i = i + 2) {
            Rect rect = new Rect();
            rect.left = i * unitWidth;
            rect.top = 0;
            rect.right = (i + 2) * unitWidth;
            rect.bottom = 50;
            mShowArea.add(rect);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int index = 0;
        //文本
        for (Rect rect : mAverageArea) {
            drawTipText(canvas, mPaintText, rect, index);
            index++;
        }

        //背景线
        canvas.drawLine(mUnitWidth, mLineToTextDistance, mBgLineEndPoint, mLineToTextDistance, mLineBg);
        //移动的线
        canvas.drawLine(mUnitWidth, mLineToTextDistance, mTouchPointX, mLineToTextDistance, mLineSel);

        //圈
        index = 0;
        for (Rect rect : mAverageArea) {
            drawCircle(canvas, rect, index);
            index++;
        }
        //移动的圈
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mTouchPointX - mBitmapRadius, mLineToTextDistance - mBitmapRadius, mCircleMove);
        } else {
            canvas.drawCircle(mTouchPointX, mLineToTextDistance, mChooseRadius, mCircleMove);
        }
    }

    //绘制圆圈
    private void drawCircle(Canvas canvas, Rect rect, int index) {
        Paint paint = mCircle;
        if (index <= mShowIndex) {
            mCircle.setColor(mColorCircleAccent);
        } else {
            mCircle.setColor(mColorCircleBg);
        }
        canvas.drawCircle(rect.centerX(), mLineToTextDistance, mDefaultRadius, paint);

        //绘制一个小一点白色圈
        if (index > mShowIndex) {
            mCircle.setColor(Color.WHITE);
            canvas.drawCircle(rect.centerX(), mLineToTextDistance, mDefaultRadius - 4, paint);
        }
    }

    //绘制文字
    private void drawTipText(Canvas canvas, TextPaint paint, Rect rect, int index) {
        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();
        int baseline = rect.top - metricsInt.top;//画文本的基线，相当于顶部
        String content = String.valueOf(tipText[index]);
        if (index == mShowIndex) {
            paint.setColor(mColorTextAccent);
        } else {
            paint.setColor(mColorTextNor);
        }
        canvas.drawText(content, rect.centerX(), baseline, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //避免父类拦截事件
        getParent().requestDisallowInterceptTouchEvent(true);
        float moveX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (moveX < mUnitWidth) {
                    moveX = mUnitWidth;
                } else if (moveX > mBgLineEndPoint) {
                    moveX = mBgLineEndPoint;
                }
                mTouchPointX = moveX;
                checkChoosePoint(moveX, 10);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL: //自动移动
            case MotionEvent.ACTION_UP:
                animMoveToTarget(calculateDistanceByIndex(mLastIndex), mTouchPointX);
                break;
        }
        return true;
    }

    private int calculateDistanceByIndex(int index) {
        return mUnitWidth * 2 * index + mUnitWidth;
    }

    private ValueAnimator mAnimator;

    private void animMoveToTarget(float end, float start) {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        ValueAnimator animator = ValueAnimator.ofFloat(start, end).setDuration(200);
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.start();
        mAnimator = animator;
    }

    private void checkChoosePoint(float x, float y) {
        int pointX = (int) x, pointY = (int) y;
        int index = 0;
        for (Rect rect : mAverageArea) {
            if (rect.contains(pointX, pointY)) {
                mLastIndex = index;
                break;
            }
            index++;
        }
        //移动到最后一点的时候，选中展示最后一个点
        if (mTouchPointX == mBgLineEndPoint) {
            mShowIndex = mAverageArea.size() - 1;
            return;
        }
        checkShowPoint(pointX, pointY);
    }

    private void checkShowPoint(int pointX, int pointY) {
        int index = 0;
        int showX = pointX + mChooseRadius - mDefaultRadius;//计算圆的半径突出
        for (Rect rect : mShowArea) {
            if (rect.contains(showX, pointY)) {
                mShowIndex = index;
                break;
            }
            index++;
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (mShowIndex != mLastIndex) {
            mShowIndex = mLastIndex;
        }
        if (mListener != null) {
            mListener.onChooseChange(tipText[mLastIndex]);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mTouchPointX = (float) animation.getAnimatedValue();
        checkShowPoint((int) mTouchPointX, 10);
        invalidate();
    }

}
