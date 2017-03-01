package com.sum.library.app.common;


import com.sum.library.view.SwipeRefresh.SwipeRefreshLayout;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayoutDirection;

/**
 * Created by Summer on 2016/10/14.
 */

public interface RefreshView {

    //初始化
    void initRefresh(SwipeRefreshLayout refresh, SwipeRefreshLayoutDirection direction);

    //下拉刷新
    void refreshTop();

    //上拉更多
    void refreshMore();

    //设置数据总数
    void setTotalSize(int total);

    //获取页数
    int getPageIndex();
}
