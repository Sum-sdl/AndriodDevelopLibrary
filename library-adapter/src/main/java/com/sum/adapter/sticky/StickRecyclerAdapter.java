package com.sum.adapter.sticky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sum.adapter.RecyclerAdapter;
import com.sum.adapter.RecyclerDataHolder;

import java.util.List;

/**
 * @author Summer
 * @date 2016/8/27
 */
public abstract class StickRecyclerAdapter<T> extends RecyclerAdapter implements StickyHeadView {

    public StickRecyclerAdapter() {
        super();
    }

    public StickRecyclerAdapter(List<RecyclerDataHolder> recyclerDataHolders) {
        super(recyclerDataHolders);
    }

    /**
     * @return 头部布局的id
     */
    public abstract int headLayoutId();

    /**
     * 统一数据类型的唯一特征
     *
     * @param position 数据源所在的位置
     * @param data     每项的数据源中存放该数据属于的一组类型
     * @return 区分不同数据源的一个id（String.chartAt(0)）
     */
    public abstract long headId(int position, T data);

    /**
     * 头部view的数据绑定
     */
    public abstract void onBindHeaderViewHolder(View holder, int position, T data);

    @Override
    public void firstHead(long fHeadId, int fPosition) {

    }

    @Override
    public long getHeadId(int position) {
        return headId(position, (T) getItemData(position));
    }

    @Override
    public View onCreateHeadView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(headLayoutId(), parent, false);
    }

    @Override
    public void onBindHeadView(View headView, int position) {
        onBindHeaderViewHolder(headView, position, (T) getItemData(position));
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
