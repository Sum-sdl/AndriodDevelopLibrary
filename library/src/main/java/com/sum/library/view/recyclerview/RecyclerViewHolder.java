package com.sum.library.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private View[] mParams;

    protected Context mContext;

    public RecyclerViewHolder(View view) {
        super(view);
        mContext = view.getContext();
    }

    public RecyclerViewHolder setParams(View... params) {
        this.mParams = params;
        return this;
    }

    public View getParam(int index) {
        return mParams[index];
    }

}
