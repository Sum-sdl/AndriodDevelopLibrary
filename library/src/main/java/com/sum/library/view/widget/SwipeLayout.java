package com.sum.library.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.sum.library.utils.Logger;

/**
 * Created by Summer on 2016/9/5.
 */
public class SwipeLayout extends FrameLayout {

    // 1.创建一个画笔
    private Paint mPaint = new Paint();

    // 2.初始化画笔
    private void initPaint() {
        mPaint.setColor(Color.BLACK);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px
    }

    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

       /* int mode = MeasureSpec.getMode(widthMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        View view = getChildAt(0);
        //确定子view的大小
        measureChild(view, widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();

        Logger.e("onMeasure wc:" + measuredWidth + " hc:" + measuredHeight + " pw:" + w + " ph:" + h);

        setMeasuredDimension(w,h);*/
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
//        RectF rectF = new RectF(100,200,800,290);
//        canvas.drawRect(rectF,mPaint);

        //translate 移动坐标系，相对于上一个点的位置坐移动

        // 在坐标原点绘制一个黑色圆形
        mPaint.setColor(Color.BLACK);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,100,mPaint);

        // 在坐标原点绘制一个蓝色圆形
        mPaint.setColor(Color.BLUE);
        canvas.translate(200,400);
        canvas.drawCircle(0,0,100,mPaint);

//        canvas.drawLine(100, 300, 900, 330, mPaint);
//        canvas.rotate(30);
    }
}
