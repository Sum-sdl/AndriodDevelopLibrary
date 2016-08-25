package com.sum.library.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext = null;
    protected List<RecyclerDataHolder> mHolders;
    private int mCurPosition;

    public RecyclerAdapter(Context context) {
        this(context, null);
        mHolders = new ArrayList<>();
    }

    public RecyclerAdapter(Context context, List<RecyclerDataHolder> holders) {
        if (context == null)
            throw new NullPointerException();
        mContext = context;
        if (holders != null) {
            mHolders = new ArrayList<>(holders.size() + 10);
            mHolders.addAll(holders);
        }
        setHasStableIds(true);
    }

    public void setDataHolders(List<RecyclerDataHolder> holders) {
        if (holders == null)
            mHolders = new ArrayList<>();
        else {
            mHolders = new ArrayList<>(holders.size() + 10);
            mHolders.addAll(holders);
        }
        notifyDataSetChanged();
    }

    public void addDataHolder(RecyclerDataHolder holder) {
        mHolders.add(holder);
        notifyItemInserted(mHolders.size() - 1);
    }

    public void addDataHolder(int location, RecyclerDataHolder holder) {
        mHolders.add(location, holder);
        notifyItemInserted(location);
    }

    public void addDataHolder(List<RecyclerDataHolder> holders) {
        int location = mHolders.size();
        mHolders.addAll(holders);
        notifyItemRangeInserted(location, holders.size());
    }

    public void addDataHolder(int location, List<RecyclerDataHolder> holders) {
        mHolders.addAll(location, holders);
        notifyItemRangeInserted(location, holders.size());
    }

    public void removeDataHolder(int location) {
        if (location < mHolders.size()) {
            mHolders.remove(location);
            notifyItemRemoved(location);
        }
    }

    public void removeDataHolder(RecyclerDataHolder holder) {
        int index = mHolders.indexOf(holder);
        if (index != -1) {
            mHolders.remove(index);
            notifyItemRemoved(index);
        }
    }

    public RecyclerDataHolder queryDataHolder(int location) {
        return mHolders.get(location);
    }

    public int queryDataHolder(RecyclerDataHolder holder) {
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

        RecyclerDataHolder holder = queryDataHolder(position);

        holder.onBindViewHolder(mContext, position, arg0, holder.getData());
    }

}
