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
                    /*这里canvas.save();和canvas.restore();是两个相互匹配出现的，作用是用来保存画布的状态和取出保存的状态的
                     * 当我们对画布进行旋转，缩放，平移等操作的时候其实我们是想对特定的元素进行操作,但是当你用canvas的方法来进行这些操作的时候，其实是对整个画布进行了操作，
                     * 那么之后在画布上的元素都会受到影响，所以我们在操作之前调用canvas.save()来保存画布当前的状态，当操作之后取出之前保存过的状态，
                     * (比如：前面元素设置了平移或旋转的操作后，下一个元素在进行绘制之前执行了canvas.save();和canvas.restore()操作)这样后面的元素就不会受到(平移或旋转的)影响
                     */
//                    canvas.save();
//                    canvas.drawColor(Color.WHITE);
//                    num++;
                    num += 10;
                    String text = "index->" + num;
                    canvas.drawText(text, getMeasuredWidth() / 2 - mPaint.measureText(text) / 2, getMeasuredHeight() / 3, mPaint);
                    canvas.drawCircle(10 + num, 10 + num, 60, mPaint);
//                    canvas.restore();
                    holder.unlockCanvasAndPost(canvas);
                    printLog();
                    try {
                        Thread.sleep(200);
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
