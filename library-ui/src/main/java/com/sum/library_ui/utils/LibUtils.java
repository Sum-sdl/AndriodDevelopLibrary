package com.sum.library_ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sdl on 2019-06-19.
 */
public class LibUtils {
    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Value of px to value of dp.
     *
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void transparentStatusBar(final Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int vis = window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.getDecorView().setSystemUiVisibility(option | vis);
            } else {
                window.getDecorView().setSystemUiVisibility(option);
            }
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setFullScreen(@NonNull final Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    /**
     * Set the screen to landscape.
     *
     * @param activity The activity.
     */
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Set the screen to portrait.
     *
     * @param activity The activity.
     */
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Return whether screen is landscape.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Return whether screen is portrait.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 获得最接近屏幕的尺寸
     */
    public static Camera.Size getCurrentScreenSize(Context context, List<Camera.Size> sizeList) {
        if (sizeList != null && sizeList.size() > 0) {
            int screenHeight = getScreenHeight(context);
            int screenWidth = getScreenWidth(context);
            if (isLandscape(context)) {
                screenHeight = getScreenWidth(context);
                screenWidth = getScreenHeight(context);
            }
            ChoiceSizeBean[] arry = new ChoiceSizeBean[sizeList.size()];
            int temp = 0;
            for (Camera.Size size : sizeList) {
                arry[temp++] = new ChoiceSizeBean(size.height, size.width);
            }
            Arrays.sort(arry, Collections.reverseOrder());
            // 选择比例接近的尺寸
            ArrayList<ChoiceSizeBean> tmp = new ArrayList<>();
            for (int i = 0; i < arry.length; i++) {
                // 排除比例不同的
                Log.d("CameraUtils", arry[i].getHeight() + " " + arry[i].getWidth() + " " + arry[i].getWidth() * 1.0f / arry[i].getHeight());
                // 比例差值不能太大
                if (Math.abs(arry[i].getWidth() * 1.0f / arry[i].getHeight() - 16f / 9) > 0.1) {
                    continue;
                }
                tmp.add(arry[i]);
            }
            if (tmp.size() == 0) {
                return null;
            }
            // 根据屏幕尺寸进行筛选
            ChoiceSizeBean largeLast = tmp.get(0);
            for (ChoiceSizeBean choiceSizeBean : tmp) {
                if (screenWidth <= choiceSizeBean.getHeight() && screenHeight <= choiceSizeBean.getWidth()) {
                    if (choiceSizeBean.compareTo(largeLast) <= 0) {
                        largeLast = choiceSizeBean;
                    }
                }
            }
            ChoiceSizeBean smallLast = tmp.get(tmp.size() - 1);
            for (ChoiceSizeBean choiceSizeBean : tmp) {
                if (screenWidth >= choiceSizeBean.getHeight() && screenHeight >= choiceSizeBean.getWidth()) {
                    if (choiceSizeBean.compareTo(smallLast) >= 0) {
                        smallLast = choiceSizeBean;
                    }
                }
            }
            // 最终选择
            ChoiceSizeBean last = null;
            if (largeLast != null) {
                // 判断最大的尺寸是不是超过2倍屏幕
                if (largeLast.getWidth() > 2 * screenHeight || largeLast.getHeight() >= 2 * screenWidth) {
                    largeLast = null;
                } else {
                    last = largeLast;
                }
            }
            if (largeLast == null) {
                last = smallLast;
            }

            for (Camera.Size size : sizeList) {
                if (size.width == last.getWidth() && size.height == last.getHeight()) {
                    return size;
                }
            }
        }
        return null;
    }

    private static class ChoiceSizeBean implements Comparable<ChoiceSizeBean> {
        private int height;
        private int width;

        private ChoiceSizeBean(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        @Override
        public int compareTo(ChoiceSizeBean o) {
            return width * height - o.getWidth() * o.getHeight();
        }
    }

    private static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        if (file.exists() && !file.delete()) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * Save the bitmap.
     *
     * @param src     The source of bitmap.
     * @param file    The file.
     * @param format  The format of the image.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean save(final Bitmap src,
                               final File file,
                               final Bitmap.CompressFormat format,
                               final boolean recycle) {
        if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file)) return false;
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

}
