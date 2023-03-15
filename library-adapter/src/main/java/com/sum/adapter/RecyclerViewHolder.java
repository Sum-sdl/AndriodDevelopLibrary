package com.sum.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sdl on 2016/8/29.
 * 基础的ViewHolder
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    private SparseArray<View> mViews;
    private View mRootView;

    public RecyclerViewHolder(View view) {
        super(view);
        mRootView = view;
        mContext = view.getContext();
        mViews = new SparseArray<>();
    }

    public <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mRootView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public int getColorByResId(int resId) {
        return ContextCompat.getColor(mContext, resId);
    }

    private void changeText(CharSequence newText, TextView tv) {
        CharSequence oldText = tv.getText();
        if (TextUtils.isEmpty(oldText)) {
            tv.setText(newText);
        } else if (!oldText.equals(newText)) {
            tv.setText(newText);
        }
    }

    public void setTextViewText(int textViewId, int resId) {
        CharSequence text = mContext.getResources().getText(resId);
        TextView tv = findViewById(textViewId);
        changeText(text, tv);
    }

    public void setTextViewText(int textViewId, CharSequence text) {
        setTextViewText(textViewId, text, -1);
    }

    public void setTextViewText(int textViewId, CharSequence text, int colorResId) {
        TextView tv = findViewById(textViewId);
        changeText(text, tv);
        if (colorResId != -1) {
            tv.setTextColor(getColorByResId(colorResId));
        }
    }

    public void setTextViewColor(int textViewId, int colorResId) {
        TextView tv = findViewById(textViewId);
        tv.setTextColor(getColorByResId(colorResId));
    }

}
