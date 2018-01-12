package com.sum.library.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by sdl on 2017/12/27.
 */

public class AppUtils {

    /**
     * 通知到系统上下文中
     */
    public static void displayToGallery(Context context, File photoFile) {
        if (photoFile == null || !photoFile.exists()) {
            return;
        }
        String photoPath = photoFile.getAbsolutePath();
        String photoName = photoFile.getName();
        // 其次把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
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

    public static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }


    /**
     * 状态栏设置为黑字字体
     */
    public static void setDark(Activity activity) {
        String brand = Build.BRAND;
        if (brand.indexOf("Xiaomi") != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMstatusBarDarkMode(activity);
            } else {
                setMIStatusBarDarkMode(true, activity);
            }
        } else if (brand.indexOf("Meizu") != -1) {
            StatusbarColorUtils.setStatusBarDarkIcon(activity, true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setMstatusBarDarkMode(activity);
        }
    }

    private static void setMstatusBarDarkMode(Activity activity) {
        View decor = activity.getWindow().getDecorView();
        int ui = decor.getSystemUiVisibility();
        // 设置浅色状态栏时的界面显示
        ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        // 设置深色状态栏时的界面显示
//            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decor.setSystemUiVisibility(ui);
    }

    /**
     * 小米修改状态栏字体颜色
     */
    private static void setMIStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setColor(Activity activity, int color) {
        //设置contentview为fitsSystemWindows
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        if (viewGroup.getChildAt(0) != null) {
            viewGroup.getChildAt(0).setFitsSystemWindows(true);
        }
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            //将状态栏设置成全透明
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((params.flags & bits) == 0) {
                params.flags |= bits;
                //如果是取消全透明，params.flags &= ~bits;
                window.setAttributes(params);
            }

            //给statusbar着色
            View view = new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, com.blankj.utilcode.util.BarUtils.getStatusBarHeight()));
            view.setBackgroundColor(color);
            viewGroup.addView(view);

            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 去掉系统状态栏下的windowContentOverlay
                View v = window.findViewById(android.R.id.content);
                if (v != null) {
                    v.setForeground(null);
                }
            }
        }
    }
}
