package com.sum.library.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private View[] mParams;

    protected Context mContext;

    private View mParent;

    public RecyclerViewHolder(View view) {
        super(view);
        mParent = view;
        mContext = view.getContext();
    }

    public <T> T _findViewById(int id) {
        return (T) mParent.findViewById(id);
    }

    public RecyclerViewHolder setParams(View... params) {
        this.mParams = params;
        return this;
    }

    public View getParam(int index) {
        return mParams[index];
    }

}
