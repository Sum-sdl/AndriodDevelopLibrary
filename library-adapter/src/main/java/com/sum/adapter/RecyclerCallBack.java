package com.sum.adapter;

/**
 * @author Sdl
 * @date 15/11/27
 */
public interface RecyclerCallBack<T> {

    /**
     * @param tag      其他标识
     * @param position 数据源位置
     * @param data     对应数据项
     */
    void onItemClick(int tag, int position, T data);

}
