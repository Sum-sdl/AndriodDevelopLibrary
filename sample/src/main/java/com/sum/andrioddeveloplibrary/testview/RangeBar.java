package com.sum.andrioddeveloplibrary.testview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sum.andrioddeveloplibrary.R;

/**
 * Created by sdl on 2017/4/1.
 */

public class RangeBar extends View {

    private int maxValue, minUnit, lineHeight;

    private Paint paintLine;

    private Paint paintLineSecond;

    private Drawable mThumb;

    private int mWidth, mHeight;

    private int mStartX, mStartY, mEndX, mEndY;

    public RangeBar(Context context) {
        this(context, null, 0);
    }

    public RangeBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RangeBar);
        int lineColor = array.getColor(R.styleable.RangeBar_lineColor, 0xfff);
        int lineSecondColor = array.getColor(R.styleable.RangeBar_lineSecondColor, 0xff0);
        int thumbRes = array.getResourceId(R.styleable.RangeBar_thumbRes, R.mipmap.img_near_round);
        maxValue = array.getInt(R.styleable.RangeBar_maxValue, 100);
        minUnit = array.getInt(R.styleable.RangeBar_minUnit, 1);
        int lineHeight = array.getDimensionPixelOffset(R.styleable.RangeBar_lineHeight, 20);
        array.recycle();

        mThumb = ContextCompat.getDrawable(getContext(), thumbRes);

        paintLine = getLinePaint(lineColor, lineHeight);
        paintLineSecond = getLinePaint(lineSecondColor, lineHeight);
    }

    private Paint getLinePaint(int color, int stroke) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(stroke);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int intrinsicHeight = mThumb.getIntrinsicHeight();

        int intrinsicWidth = mThumb.getIntrinsicWidth();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }

    void drawTrack(Canvas canvas) {
        //底部线
        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, paintLine);

    }


    private int dp2px(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
