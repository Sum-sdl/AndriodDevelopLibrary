package com.sum.cache.util;

import android.util.Log;


/**
 * Logger
 **/
public class CacheLog {

    /**
     * log的开关
     */
    private static boolean isDebug = false;

    /**
     * log的输出tag
     */
    private static String TAG = "Cache";


    /**
     * 设置log的TAG
     **/
    public static void setTAG(String tAG, boolean isDebug) {
        TAG = tAG;
        CacheLog.isDebug = isDebug;
    }

    /**
     * 正常调试log输出
     *
     * @param msg msg log的内容
     **/
    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }


    /**
     * 异常log输出
     *
     * @param msg log的内容
     **/
    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    /**
     * <w>
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }


}
