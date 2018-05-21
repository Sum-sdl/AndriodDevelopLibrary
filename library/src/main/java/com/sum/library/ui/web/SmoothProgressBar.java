package com.sum.library.ui.web;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

/**
 * Created by sdl on 2018/5/21.
 */
public class SmoothProgressBar extends ProgressBar {

    public SmoothProgressBar(Context context) {
        super(context);
        setMax(100);
    }

    public SmoothProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMax(100);
    }

    public SmoothProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMax(100);
    }

    private int mCurProgress = 0;
    private ValueAnimator mAnim;

    public void setShowProgress(int progress) {
        int max = getMax();
        int cur = mCurProgress;
        if (cur == progress) {
            return;
        }
        if (cur > 0 && cur > progress) {
            setProgress(progress);
            return;
        }
        if (progress > max) {
            progress = max;
        }
        int sub = Math.abs(progress - cur);
        final long time = (long) ((sub / (float) max) * 3000);
        if (mAnim != null && mAnim.isRunning()) {
            mAnim.cancel();
        }
        final ValueAnimator animator = ObjectAnimator.ofInt(cur, progress);
        animator.setDuration(time);
        animator.addUpdateListener(animation -> {
            Integer pro = (Integer) animation.getAnimatedValue();
            setProgress(pro);
            if (pro == getMax()) {
                setVisibility(View.GONE);
            } else {
                setVisibility(View.VISIBLE);
            }
        });
        animator.setInterpolator(PROGRESS_ANIM_INTERPOLATOR);
        animator.start();
        mAnim = animator;
        mCurProgress = progress;
    }

    private static final DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR = new DecelerateInterpolator();
}
