package com.sum.library.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

public abstract class RecyclerDataHolder<T> {

    public static final int HOLDER_NORMAL = 0;
    public static final int HOLDER_SELECT = 2;
    public static final int HOLDER_UN_SELECT = 1;

    private T mData = null;
    private int mId;
    private int mHolderState = 0;

    public RecyclerDataHolder(T data) {
        mData = data;
        mId = super.hashCode();
    }

    public int getId() {
        return mId;
    }

    public abstract ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position);

    public abstract void onBindViewHolder(Context context, int position, ViewHolder vHolder, T data);

    public int getType() {
        return 0;
    }

    public T getData() {
        return mData;
    }

    public int getHolderState() {
        return mHolderState;
    }

    public void setHolderState(int holderState) {
        this.mHolderState = holderState;
    }
}
