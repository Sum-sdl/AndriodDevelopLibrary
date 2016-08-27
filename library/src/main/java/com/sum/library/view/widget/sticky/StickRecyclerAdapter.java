package com.sum.library.view.widget.sticky;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sum.library.view.recyclerview.RecyclerAdapter;
import com.sum.library.view.recyclerview.RecyclerDataHolder;
import com.sum.library.view.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * Created by Summer on 2016/8/27.
 */
public abstract class StickRecyclerAdapter extends RecyclerAdapter implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public StickRecyclerAdapter(Context context) {
        super(context);
    }

    public StickRecyclerAdapter(Context context, List<RecyclerDataHolder> holders) {
        super(context, holders);
    }

    //布局Id
    public abstract int layoutId();

    //头部view的数据绑定
    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position, Object o);

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId(), parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindHeaderViewHolder(holder, position, queryDataHolder(position).getData());
    }

}
