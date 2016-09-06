package com.sum.andrioddeveloplibrary.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.sum.library.utils.Logger;

/**
 * Created by Summer on 2016/9/6.
 */
public class TestCustomViewGroup extends ViewGroup {


    // 1.创建一个画笔
    private Paint mPaint = new Paint();

    // 2.初始化画笔
    private void initPaint() {
        mPaint.setColor(Color.BLACK);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px


        start = new PointF(0, 0);
        end = new PointF(0, 0);
        control = new PointF(0, 0);
    }

    private int mWidth, mHeight;


    private PointF start, end, control;


    public TestCustomViewGroup(Context context) {
        super(context);
    }

    public TestCustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TestCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int centerX, centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        centerX = w / 2;
        centerY = h / 2;

        start.x = centerX - 200;
        start.y = centerY;

        end.x = centerX + 200;
        end.y = centerY;

        control.x = centerX;
        control.y = centerY + 100;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        control.x = event.getX();
        control.y = event.getY();

        invalidate();

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //动态的来设置子view的layout位置
        Logger.e(changed + " onLayout left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        t_translate(canvas);
//        t_scale2(canvas);
//        t_rotate(canvas);
//        t_circle(canvas);
//        t_drawPic(canvas);
//        t_drawPath(canvas);
        t_drawPath2(canvas);

    }

    private void t_drawPath2(Canvas canvas) {
        mPaint.setStrokeWidth(30);
        //绘制控制点
        canvas.drawPoint(start.x, start.y, mPaint);
        canvas.drawPoint(end.x, end.y, mPaint);
        canvas.drawPoint(control.x, control.y, mPaint);

        //绘制辅助线
        mPaint.setStrokeWidth(5);
        canvas.drawLine(start.x,start.y,control.x,control.y,mPaint);
        canvas.drawLine(end.x,end.y,control.x,control.y,mPaint);

        //二阶曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        Path path = new Path();
        path.moveTo(start.x, start.y);
//        path.quadTo(control.x, control.y, end.x, end.y);
        //三阶曲线
        path.cubicTo(control.x, control.y, 0,0,end.x, end.y);
        canvas.drawPath(path, mPaint);
    }

    private void t_drawPath(Canvas canvas) {
        //画布中心位移到屏幕中心
        canvas.translate(mWidth / 2, mHeight / 2);

        Path path = new Path();
        //设置下个点的起点
        path.moveTo(50, 50);
        path.lineTo(200, 400);
        //设置上个点的最后一点
        path.setLastPoint(200, 100);
        path.lineTo(200, 0);

        //链接第一个点和最后一个点
        path.close();

        canvas.drawPath(path, mPaint);

    }


    private void t_drawPic(Canvas canvas) {
        //无效
        Picture picture = new Picture();
        //录制内容
        Canvas recording = picture.beginRecording(500, 500);
        recording.drawColor(Color.YELLOW);

        recording.translate(250, 250);
//
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        recording.drawCircle(0, 0, 100, paint);
        picture.endRecording();

        //绘制录制内容
        canvas.drawPicture(picture, new RectF(0, 0, 500, 500));

        canvas.drawCircle(300, 300, 100, mPaint);
    }

    private void t_rotate(Canvas canvas) {
        //画布中心位移到屏幕中心
        canvas.translate(mWidth / 2, mHeight / 2);

        Rect rect1 = new Rect(-400, -400, 400, 400);
//        Rect rect = new Rect(-400, -400, 400, 400);

        mPaint.setColor(Color.YELLOW);

        canvas.drawCircle(0, 0, 400, mPaint);
        for (int i = 0; i < 360; i += 20) {
            canvas.drawLine(0, -400, 0, -300, mPaint);
            //画布缩小
            canvas.rotate(20);
        }
    }

    private void t_scale2(Canvas canvas) {
        canvas.save();
        //画布中心位移到屏幕中心
        canvas.translate(mWidth / 2, mHeight / 2);
        Rect rect = new Rect(-200, -400, 200, 400);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        for (int i = 0; i < 20; i++) {
            canvas.drawRect(rect, mPaint);
            //画布缩小
            canvas.scale(0.9f, 0.9f);
        }
        canvas.restore();
    }

    private void t_scale(Canvas canvas) {

        canvas.save();
        //画布中心位移到屏幕中心
        canvas.translate(mWidth / 2, mHeight / 2);
        //矩阵
        Rect rect = new Rect(0, -400, 400, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(rect, mPaint);
        //画布缩小
        canvas.scale(0.5f, 0.5f, 200, 100);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rect, mPaint);

        canvas.restore();
        mPaint.setColor(Color.BLACK);
        rect.set(0, 0, 400, 400);
        canvas.drawRect(rect, mPaint);
    }

    private void t_circle(Canvas canvas) {
        mPaint.setColor(Color.RED);
        //绘制圆
        canvas.drawCircle(500, 300, 100, mPaint);

        mPaint.setColor(Color.BLACK);
//        mPaint.setStrokeMiter(0.4f);
        mPaint.setStyle(Paint.Style.STROKE);//描边
        //绘制椭圆(一个矩阵的内切圆)
        canvas.drawOval(new RectF(10, 10, 210, 110), mPaint);

        //绘制圆弧 (中心点的使用)
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(new RectF(10, 110, 210, 310), 0, 90, true, mPaint);

    }

    private void t_translate(Canvas canvas) {
        //translate 移动坐标系，相对于上一个点的位置坐移动

        // 在坐标原点绘制一个黑色圆形
        mPaint.setColor(Color.BLACK);
        canvas.translate(200, 200);
        canvas.drawCircle(0, 0, 100, mPaint);

        // 在坐标原点绘制一个蓝色圆形
        mPaint.setColor(Color.BLUE);
        canvas.translate(200, 400);
        canvas.drawCircle(0, 0, 100, mPaint);
    }
}
