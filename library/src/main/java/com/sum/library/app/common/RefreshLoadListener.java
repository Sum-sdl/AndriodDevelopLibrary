package com.sum.library.app.common;

/**
 * Created by Sum on 16/6/20.
 */
public interface RefreshLoadListener {

    /**
     * 加载网络数据
     */
    void onRefreshLoadData();

    void onRefreshNoMore();

}
