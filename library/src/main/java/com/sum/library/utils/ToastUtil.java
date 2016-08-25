package com.sum.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Sum on 15/11/23.
 */
public class ToastUtil {

    private static Context mContext;

    private static Toast mToast;

    public static void init(Context context) {
        mContext = context;
    }

    public static void showToastShort(String msg) {
        String text = TextUtils.isEmpty(msg) ? "" : msg;

        int duration = Toast.LENGTH_SHORT;

        if (null != mToast) {
            mToast.setText(text);
            mToast.setDuration(duration);
        } else {
            mToast = Toast.makeText(mContext, text, duration);
        }
        mToast.show();
    }

    public static void showToastLong(String msg) {
        Toast.makeText(mContext,
                TextUtils.isEmpty(msg) ? "" : msg,
                Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(int msgId) {
        String msg = mContext.getResources().getString(msgId);
        Toast.makeText(mContext,
                TextUtils.isEmpty(msg) ? "" : msg,
                Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(int msgId) {
        String msg = mContext.getResources().getString(msgId);
        Toast.makeText(mContext,
                TextUtils.isEmpty(msg) ? "" : msg,
                Toast.LENGTH_LONG).show();
    }
}
