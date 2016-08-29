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
public abstract class StickRecyclerAdapter<T> extends RecyclerAdapter<T> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {


    public StickRecyclerAdapter(Context context) {
        super(context);
    }

    public StickRecyclerAdapter(Context context, List<RecyclerDataHolder<T>> recyclerDataHolders) {
        super(context, recyclerDataHolders);
    }

    /**
     * @return 头部布局的id
     */
    public abstract int layoutId();

    /**
     * @param position 数据源所在的位置
     * @param data     每项的数据源中存放该数据属于的一组类型
     * @return 区分不同数据源的一个id
     */
    public abstract long headId(int position, T data);

    /**
     * 头部view的数据绑定
     */
    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position, T data);

    @Override
    public long getHeaderId(int position) {
        return headId(position, queryDataHolder(position).getData());
    }

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
