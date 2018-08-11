package com.sum.library.view.widget;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.blankj.utilcode.util.SizeUtils;
import com.sum.library.R;

/**
 * Created by sdl on 2018/8/6.
 */
public class CircleView extends View {

    private int progress;
    private int stoke_width;

    private String text;
    private int pDirection;
    private int start_direction;

    private Paint paint_background = null;
    private Paint paint_foreground = null;
    private Paint paint_foreground_second = null;
    private Paint paint_text = null;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        int background_color = array.getColor(R.styleable.CircleView_background_color, Color.parseColor("#000000"));
        int second_color = array.getColor(R.styleable.CircleView_foreground_bg_color, Color.parseColor("#000000"));
        int foreground_color = array.getColor(R.styleable.CircleView_foreground_color, Color.parseColor("#ffffff"));

        progress = array.getInt(R.styleable.CircleView_progress, 100);

        pDirection = array.getInt(R.styleable.CircleView_direction, 1);

        stoke_width = array.getDimensionPixelSize(R.styleable.CircleView_stoke_width, SizeUtils.dp2px(2.4f));
        int text_size = array.getDimensionPixelSize(R.styleable.CircleView_text_size, SizeUtils.dp2px(10.4f));

        int text_color = array.getColor(R.styleable.CircleView_text_color, foreground_color);
        text = array.getString(R.styleable.CircleView_text);
        if (TextUtils.isEmpty(text)) {
            text = "跳过";
        }
        start_direction = array.getInt(R.styleable.CircleView_start_direction, 3);
        array.recycle();

        paint_background = new Paint();
        paint_background.setAntiAlias(true);
        paint_background.setStrokeCap(Paint.Cap.ROUND);
        paint_background.setStrokeJoin(Paint.Join.ROUND);
        paint_background.setStyle(Paint.Style.FILL);
        paint_background.setColor(background_color);

        paint_foreground = new Paint(paint_background);
        paint_foreground.setStrokeWidth(stoke_width);
        paint_foreground.setStyle(Paint.Style.STROKE);
        paint_foreground.setColor(foreground_color);

        paint_foreground_second = new Paint(paint_foreground);
        paint_foreground_second.setColor(second_color);

        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setColor(text_color);
        paint_text.setTextSize(text_size);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = getMeasuredHeight() < getMeasuredWidth() ? getMeasuredHeight() / 2 - stoke_width : getMeasuredWidth() / 2 - stoke_width;
        //绘制里面的圆
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint_background);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint_foreground_second);

        //绘制圆弧
        RectF rectF = new RectF(getMeasuredWidth() / 2 - radius, getMeasuredHeight() / 2 - radius, getMeasuredWidth() / 2 + radius, getMeasuredHeight() / 2 + radius);
        if (pDirection == 1) {
            canvas.drawArc(rectF, 90 * (start_direction - 3), (float) -progress / 100 * 360, false, paint_foreground);
        } else {
            canvas.drawArc(rectF, 90 * (start_direction - 3), (float) progress / 100 * 360, false, paint_foreground);
        }

        //绘制文字
        RectF rectText = new RectF(getMeasuredWidth() / 2 - radius, getMeasuredHeight() / 2 - radius, getMeasuredWidth() / 2 + radius, getMeasuredHeight() / 2 + radius);
        Paint.FontMetricsInt fontMetrics = paint_text.getFontMetricsInt();
        int baseline = ((int) (rectText.top + (rectText.bottom - rectText.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top));
        canvas.drawText(text, rectText.left + (rectText.width() - paint_text.measureText(text)) / 2, baseline, paint_text);
    }

    public void setText(String text, int progress) {
        this.text = text;
        this.progress = progress;
        postInvalidate();
    }

    private ValueAnimator valueAnimator;

    public void fastStart() {
        fastStart(3000, null);
    }

    public void fastStart(int duration, AnimatorListenerAdapter listener) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator = null;
            removeCallbacks(mDeal);
        }
        valueAnimator = ValueAnimator.ofInt(100, 0);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation ->
                setText("跳过", (int) (100 - animation.getAnimatedFraction() * 100))
        );
        if (listener != null) {
            valueAnimator.addListener(listener);
        }
        valueAnimator.start();

        postDelayed(mDeal, duration);
    }

    private Runnable mDeal = new Runnable() {
        @Override
        public void run() {
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            clearAnimation();
        }
    };
}
