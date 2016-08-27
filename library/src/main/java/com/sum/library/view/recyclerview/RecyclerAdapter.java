package com.sum.library.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext = null;
    protected List<RecyclerDataHolder<T>> mHolders;
    private int mCurPosition;

    public RecyclerAdapter(Context context) {
        this(context, null);
        mHolders = new ArrayList<>();
    }

    public RecyclerAdapter(Context context, List<RecyclerDataHolder<T>> holders) {
        if (context == null)
            throw new NullPointerException();
        mContext = context;
        if (holders != null) {
            mHolders = new ArrayList<>(holders.size() + 10);
            mHolders.addAll(holders);
        }
        setHasStableIds(true);
    }

    public void setDataHolders(List<RecyclerDataHolder<T>> holders) {
        if (holders == null)
            mHolders = new ArrayList<>();
        else {
            mHolders = new ArrayList<>(holders.size() + 10);
            mHolders.addAll(holders);
        }
        notifyDataSetChanged();
    }

    public void addDataHolder(RecyclerDataHolder<T> holder) {
        mHolders.add(holder);
        notifyItemInserted(mHolders.size() - 1);
    }

    public void addDataHolder(int location, RecyclerDataHolder<T> holder) {
        mHolders.add(location, holder);
        notifyItemInserted(location);
    }

    public void addDataHolder(List<RecyclerDataHolder<T>> holders) {
        int location = mHolders.size();
        mHolders.addAll(holders);
        notifyItemRangeInserted(location, holders.size());
    }

    public void addDataHolder(int location, List<RecyclerDataHolder<T>> holders) {
        mHolders.addAll(location, holders);
        notifyItemRangeInserted(location, holders.size());
    }

    public void removeDataHolder(int location) {
        if (location < mHolders.size()) {
            mHolders.remove(location);
            notifyItemRemoved(location);
        }
    }

    public void removeDataHolder(T holder) {
        int index = mHolders.indexOf(holder);
        if (index != -1) {
            mHolders.remove(index);
            notifyItemRemoved(index);
        }
    }

    public RecyclerDataHolder<T> queryDataHolder(int location) {
        return mHolders.get(location);
    }

    public int queryDataHolder(T holder) {
        return mHolders.indexOf(holder);
    }

    @Override
    public final int getItemCount() {
        return mHolders.size();
    }

    @Override
    public final long getItemId(int position) {
        return queryDataHolder(position).getId();
    }

    @Override
    public final int getItemViewType(int position) {
        mCurPosition = position;
        return queryDataHolder(position).getType();
    }

    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return queryDataHolder(mCurPosition).onCreateViewHolder(mContext, parent, mCurPosition);
    }

    @Override
    public final void onBindViewHolder(ViewHolder arg0, int position) {
        RecyclerDataHolder<T> holder = queryDataHolder(position);
        holder.onBindViewHolder(mContext, position, arg0, holder.getData());
    }

}
