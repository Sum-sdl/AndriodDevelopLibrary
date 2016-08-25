package com.sum.library.view;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by Sum on 15/12/7.
 */
public class ViewHelper {

    public static void setViewVisible(View view, boolean show) {
        if (view == null) {
            return;
        }
        if (show) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void setViewGone(View view, boolean gone) {
        if (view == null) {
            return;
        }
        if (gone) {
            if (view.getVisibility() != View.GONE) {
                view.setVisibility(View.GONE);
            }
        } else {
            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void KeyBoardShow(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void KeyBoardHide(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 计算当然值占据比例
     *
     * @param value 当前进度
     * @param min   最小进度
     * @param max   最大进度
     * @return 占据比例 0-1
     */
    public static float getProgress(int value, int min, int max) {
        if (min == max) {
            throw new IllegalArgumentException("Max (" + max + ") cannot equal min (" + min + ")");
        }

        return (value - min) / (float) (max - min);
    }

    /**
     * 可以用做比例x目标值获取当前值
     *
     * @param value  当前进度
     * @param target 目标进度
     * @return 0 - 1 进度比例
     */
    public static float progressPercent(int value, int target) {
        float progress = getProgress(value, 0, target);
        //最小为0：0%
        float curProgress = Math.max(progress, 0);
        //最大是1：100%
        return Math.min(curProgress, 1);
    }


    /**
     * @param fraction   进度比例（0-1）
     * @param startValue 开始色值
     * @param endValue   结束色值
     * @return 当前进度的色值
     */
    public static int rgbEvaluate(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }
}
