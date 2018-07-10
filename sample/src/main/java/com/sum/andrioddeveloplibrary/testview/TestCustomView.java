package com.sum.andrioddeveloplibrary.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Summer on 2018/6/15.
 */
public class TestCustomView extends View {
    private static final int LAYER_FLAGS = 0;

    /* Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;*/

    // 1.创建一个画笔
    private Paint mPaint = new Paint();

    // 2.初始化画笔
    private void initPaint() {
        mPaint.setColor(Color.BLACK);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px
    }

    private int mWidth, mHeight;


    public TestCustomView(Context context) {
        super(context);
    }

    public TestCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TestCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int centerX, centerY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        centerX = w / 2;
        centerY = h / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.translate(10, 10);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(75, 75, 75, mPaint);
        canvas.saveLayerAlpha(0, 0, 200, 200, 0x88, LAYER_FLAGS);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(125, 125, 75, mPaint);
        canvas.restore();

    }
}
