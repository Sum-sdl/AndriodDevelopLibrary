package com.sum.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sdl
 * @date 2016/8/29
 * 全局通用适配器
 */
public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<RecyclerDataHolder> mHolders;
    private int mCurPosition;

    public RecyclerAdapter() {
        mHolders = new ArrayList<>();
        setHasStableIds(true);
    }

    public RecyclerAdapter(List<RecyclerDataHolder> holders) {
        if (holders != null) {
            mHolders = new ArrayList<>(holders.size() + 10);
            mHolders.addAll(holders);
        }
        setHasStableIds(true);
    }

    public void setDataHolders(List<RecyclerDataHolder> holders) {
        if (holders == null) {
            mHolders = new ArrayList<>();
        } else {
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

    public List<RecyclerDataHolder> getDataHolders() {
        return mHolders;
    }

    public RecyclerDataHolder getDataHolder(int location) {
        if (location < mHolders.size() && location > 0) {
            return mHolders.get(location);
        }
        return null;
    }

    public <T> T getItemData(int location) {
        if (location < mHolders.size() && location > 0) {
            return (T) mHolders.get(location).getData();
        }
        return null;
    }

    protected RecyclerDataHolder queryDataHolder(int location) {
        return mHolders.get(location);
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

    @NonNull
    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerDataHolder holder = queryDataHolder(mCurPosition);
        View view = holder.onCreateView(parent.getContext(), parent);
        return holder.onCreateViewHolder(view, mCurPosition);
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder vHolder, int position) {
        RecyclerDataHolder holder = queryDataHolder(position);
        holder.addRecyclerAdapter(this);
        holder.onBindViewHolder(position, vHolder, holder.getData());
    }

}
