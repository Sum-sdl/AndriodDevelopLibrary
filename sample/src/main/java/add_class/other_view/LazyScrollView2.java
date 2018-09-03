package add_class.other_view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * Created by Sum on 15/12/4.
 */
public class LazyScrollView2 extends NestedScrollView {


    public final static int NONE = 0;
    /**
     * 按下
     */
    public final static int PRESS = 1;
    /**
     * 左移
     */
    public final static int LEFT = 2;
    /**
     * 右移
     */
    public final static int RIGHT = 3;
    /**
     * 上移
     */
    public final static int UP = 4;
    /**
     * 下移
     */
    public final static int DOWN = 5;
    /**
     * 长按
     */
    public final static int LONG_PRESS = 6;
    /**
     * 放大
     */
    public final static int AMPLIFICATION = 7;
    /**
     * 缩小
     */
    public final static int NARROW = 8;

    private static final float MIN_MOVE_DISTANCE = 15;

    private int mTouchMode;
    private float mStartX, mStartY;
    private long mStartTime;
    private float mFingerSpace;

    private GestureDetector mGestureDetector;


    public LazyScrollView2(Context context) {
        super(context);
        init(context);
    }

    public LazyScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(getContext(), new MyGestureListener());
    }

    private int mCurState;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mGestureDetector.onTouchEvent(event);
                mTouchMode = PRESS;
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mStartTime = System.currentTimeMillis();
                mFingerSpace = 0;
                mCurState = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                getGestures(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_CANCEL:
                mTouchMode = NONE;
                break;
        }

        if (mTouchMode == 2 || mTouchMode == 3) {
            return false;
        }
        if (mCurState > 0) {
            return false;
        }
        if (mTouchMode == 7 || mTouchMode == 8) {
            mCurState = 1;
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }


    private void getGestures(MotionEvent event) {
// 2个手指以上
        if (event.getPointerCount() >= 2) {
//先判断是否是缩放
            float x1 = event.getX(0) - event.getX(1);
            float y1 = event.getY(0) - event.getY(1);
//第一个手指和第二个手指的间距
            float value = (float) Math.sqrt(x1 * x1 + y1 * y1);
            if (mFingerSpace == 0) {
                mFingerSpace = value;
            } else {
//一段时间内，如果两值间的变化不大，则认为是移动，否则是；加时间限制是为了防止反应过快
                if (System.currentTimeMillis() - mStartTime > 50) {
//移动后两指的间距的变化值
                    float fingerDistanceChange = value - mFingerSpace;
//同时手指间的间距变化大于最小距离时就认为是缩放
                    if (Math.abs(fingerDistanceChange) > MIN_MOVE_DISTANCE) {
                        float scale = value / mFingerSpace;
                        if (scale > 1) {
                            mTouchMode = AMPLIFICATION;
                        } else {
                            mTouchMode = NARROW;
                        }
                        mStartTime = System.currentTimeMillis();
                        mStartX = event.getX();
                        mStartY = event.getY();
                        mFingerSpace = value;
//当第一个手指200毫秒内移动MIN_MOVE_DISTANCE倍距离，同时手指间的间距变化小于4倍距离时为移动
                    }
                }
                return;
            }
        }
//判断是否长按,x方向移动小于最小距离同时y方向小于最小距离，
        float offsetX = Math.abs(event.getX() - mStartX);
        float offsetY = Math.abs(event.getY() - event.getY());
        long time = System.currentTimeMillis() - mStartTime;
        if (time > 1500 && Math.abs(offsetX) < MIN_MOVE_DISTANCE && Math.abs(offsetY) < MIN_MOVE_DISTANCE) {
            mTouchMode = LONG_PRESS;
            return;
        }
//移动时区分上下还是左右
        if (System.currentTimeMillis() - mStartTime > 50) {
            float xDistance = event.getX() - mStartX;
            float yDistance = event.getY() - mStartY;
            if (Math.abs(xDistance) > Math.abs(yDistance)) {
                if (xDistance > 0) {
                    mTouchMode = RIGHT;
                } else {
                    mTouchMode = LEFT;
                }
            } else {
                if (yDistance > 0) {
                    mTouchMode = DOWN;
                } else {
                    mTouchMode = UP;
                }
            }
            mStartTime = System.currentTimeMillis();
            mStartX = event.getX();
            mStartY = event.getY();
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (mTouchMode == AMPLIFICATION || mTouchMode == NARROW) {
                return;
            }
            mTouchMode = LONG_PRESS;
        }
    }

}
