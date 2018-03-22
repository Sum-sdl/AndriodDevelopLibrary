package com.sum.library.view.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sum.library.R;
import com.sum.library.view.SwipeRefresh.MaterialProgressDrawable;


/**
 * Created by Sum on 15/12/17.
 */
public class DialogMaker {


    public static void showAlterDialog(Context context, String Title, String message, DialogInterface.OnClickListener cancel, DialogInterface.OnClickListener ok) {
        AlertDialog dialog = getDialog(context)
                .setTitle(Title)
                .setMessage(message)
                .setNegativeButton("取消", cancel)
                .setPositiveButton("确定", ok)
                .create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showAlterDialog(Context context, String Title, String message, String negative, DialogInterface.OnClickListener cancel, String positive, DialogInterface.OnClickListener ok) {
        AlertDialog dialog = getDialog(context)
                .setTitle(Title)
                .setMessage(message)
                .setNegativeButton(negative, cancel)
                .setPositiveButton(positive, ok)
                .create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context);
    }


    public static AlertDialog showLoadingDialog(Context context, String content) {
        return showLoadingDialog(context, content, R.style.dialog_no_bg);
    }

    public static AlertDialog showLoadingDialog(Context context, String content, int style) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, style);
        View view = LayoutInflater.from(context).inflate(R.layout.pub_loading_view, null);

        ImageView imageView = view.findViewById(R.id.loading);
        View viewParent = view.findViewById(R.id.load_content);

        TextView tv = view.findViewById(R.id.loading_hint);
        if (!TextUtils.isEmpty(content)) {
            tv.setText(content);
        } else {
            tv.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        view.setLayoutParams(params);

        MaterialProgressDrawable drawable = new MaterialProgressDrawable(context, viewParent);
        int color1 = ContextCompat.getColor(context, R.color.colorPrimary);
//        int color2 = ContextCompat.getColor(context, R.color.colorPrimary);
        drawable.setColorSchemeColors(color1);
        imageView.setImageDrawable(drawable);
        drawable.setAlpha(255);
        drawable.start();
        dialog.setView(view);

        AlertDialog res = dialog.create();
        res.setCancelable(true);
        res.setCanceledOnTouchOutside(false);

        return res;
    }


    public static ProgressDialog showProgress(Context context, CharSequence title, CharSequence message, boolean cancelable) {
        final ProgressDialog dialog = ProgressDialog.show(context, title, message);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        return dialog;
    }
}
