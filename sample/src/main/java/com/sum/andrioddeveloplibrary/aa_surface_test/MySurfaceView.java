package com.sum.andrioddeveloplibrary.aa_surface_test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by sdl on 2018/11/19.
 */
public class MySurfaceView extends SurfaceView {
    private Paint mPaint;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(SizeUtils.dp2px(20f));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private volatile boolean loop = false;

    private int num = 1;

    public void start() {
        loop = true;
        threadStart();
    }

    public void stop() {
        loop = false;
    }

    public void print() {
        SurfaceHolder holder = getHolder();
        if (holder != null) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            num += 10;
            canvas.drawCircle(10 + num, 10 + num, 40, mPaint);
            holder.unlockCanvasAndPost(canvas);
        }
        printLog();
    }

    public void reset() {
        num = 0;
    }

    public void printLog() {
        LogUtils.e("index->" + num);
    }

    //子线程可直接绘制数据源
    //lockCanvas 获取画布绘制数据
    //unlockCanvasAndPost 通知系统服务SurfaceFlinger刷新

    //lockCanvas返回的画布，每次都缓存了上次的绘制内容
    private void threadStart() {
        new Thread(() -> {
            while (loop) {
                SurfaceHolder holder = getHolder();
                if (holder != null) {
                    Canvas canvas = holder.lockCanvas();
                    canvas.drawColor(Color.WHITE);
//                    num++;
                    num += 10;
                    String text = "index->" + num;
                    canvas.drawText(text, getMeasuredWidth() / 2 - mPaint.measureText(text) / 2, getMeasuredHeight() / 3, mPaint);
                    canvas.drawCircle(10 + num, 10 + num, 60, mPaint);
                    holder.unlockCanvasAndPost(canvas);
                    printLog();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (num >= 1000) {
                        num = 50;
                    }
                }
            }
        }, "surface_child_thread").start();
    }
}
