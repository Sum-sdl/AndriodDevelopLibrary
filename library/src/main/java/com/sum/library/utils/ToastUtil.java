package com.sum.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Sum on 15/11/23.
 * 注意SnackBar的使用
 */
public class ToastUtil {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void showToastShort(String msg) {
        if (mContext == null) {
            return;
        }
        Toast.makeText(mContext, TextUtils.isEmpty(msg) ? "" : msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(String msg) {
        if (mContext == null) {
            return;
        }
        Toast.makeText(mContext,TextUtils.isEmpty(msg) ? "" : msg,Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(int msgId) {
        if (mContext == null) {
            return;
        }
        String msg = mContext.getResources().getString(msgId);
        Toast.makeText(mContext, TextUtils.isEmpty(msg) ? "" : msg,Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(int msgId) {
        if (mContext == null) {
            return;
        }
        String msg = mContext.getResources().getString(msgId);
        Toast.makeText(mContext, TextUtils.isEmpty(msg) ? "" : msg,Toast.LENGTH_LONG).show();
    }
}
