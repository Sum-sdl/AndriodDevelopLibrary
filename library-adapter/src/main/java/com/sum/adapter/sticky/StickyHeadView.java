package com.sum.adapter.sticky;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Summer on 2016/8/29.
 */
public interface StickyHeadView {

    /**
     * 根据Item的位置获取Head的Id，根据不同的Id来设置不同头部展示
     * headId>0
     */
    long getHeadId(int position);

    /**
     * 创建HeadView
     */
    View onCreateHeadView(ViewGroup parent);

    /**
     * 绑定HeadView数据展示
     */
    void onBindHeadView(View headView, int position);


    /**
     * 第一个头部的位置信息
     */
    void firstHead(long fHeadId, int fPosition);

    /**
     * 总数
     */
    int getItemCount();
}
