package com.sum.library.view.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.sum.library.R;
import com.sum.library.view.drawble.MaterialProgressDrawable;


/**
 * Created by Sum on 15/12/17.
 */
public class DialogMaker {

    public static AlertDialog showLoadingDialog(Context context, String content, boolean cancel) {
        return showLoadingDialog(context, content, R.style.dialog_no_bg, cancel);
    }

    private static AlertDialog showLoadingDialog(Context context, String content, int style, boolean cancel) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, style);
        View view = LayoutInflater.from(context).inflate(R.layout.pub_loading_view, null);

        ImageView imageView = view.findViewById(R.id.loading);
        View viewParent = view.findViewById(R.id.load_content);

        TextView tv = view.findViewById(R.id.loading_hint);
        if (!TextUtils.isEmpty(content)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(content);
        } else {
            tv.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        view.setLayoutParams(params);

        MaterialProgressDrawable drawable = new MaterialProgressDrawable(context, viewParent);
        int color1 = ContextCompat.getColor(context, R.color.pub_loading_bg_color);
        drawable.setColorSchemeColors(color1);
        imageView.setImageDrawable(drawable);
        drawable.setAlpha(255);
        drawable.start();
        dialog.setView(view);

        AlertDialog res = dialog.create();
        res.setCancelable(cancel);
        res.setCanceledOnTouchOutside(false);

        return res;
    }


    public static AlertDialog showLoadingDialog2(Context context, String content, boolean cancel) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.dialog_no_bg);
        View view = LayoutInflater.from(context).inflate(R.layout.pub_loading_view_2, null);

        ImageView imageView = view.findViewById(R.id.loading);
        View viewParent = view.findViewById(R.id.load_content);

        TextView tv = view.findViewById(R.id.loading_hint);
        if (!TextUtils.isEmpty(content)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(content);
        } else {
            tv.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        view.setLayoutParams(params);

        MaterialProgressDrawable drawable = new MaterialProgressDrawable(context, viewParent);
        int color1 = ContextCompat.getColor(context, R.color.pub_loading_bg_color2);
        drawable.setColorSchemeColors(color1);
        imageView.setImageDrawable(drawable);
        drawable.setAlpha(255);
        drawable.start();
        dialog.setView(view);

        AlertDialog res = dialog.create();
        res.setCancelable(cancel);
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
