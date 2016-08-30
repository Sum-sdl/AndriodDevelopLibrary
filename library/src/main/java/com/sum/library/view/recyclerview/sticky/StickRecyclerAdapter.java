package com.sum.library.view.recyclerview.sticky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sum.library.view.recyclerview.RecyclerAdapter;
import com.sum.library.view.recyclerview.RecyclerDataHolder;

import java.util.List;

/**
 * Created by Summer on 2016/8/27.
 */
public abstract class StickRecyclerAdapter<T> extends RecyclerAdapter<T> implements StickyHeadView {

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
    public abstract void onBindHeaderViewHolder(View holder, int position, T data);


    @Override
    public long getHeadId(int position) {
        return headId(position, queryDataHolder(position).getData());
    }

    @Override
    public View onCreateHeadView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId(), parent, false);
    }

    @Override
    public void onBindHeadView(View headView, int position) {
        onBindHeaderViewHolder(headView, position, queryDataHolder(position).getData());
    }

    /**
     * 获取headId对应的第一个Item的pos
     */
    public int getTargetHeadPos(long headId) {
        int count = getItemCount();
        int targetPos = -1;
        for (int i = 0; i < count; i++) {
            long id = getHeadId(i);
            if (id == headId) {
                targetPos = i;
                break;
            }
        }
        return targetPos;
    }
}
