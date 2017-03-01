package com.sum.library.app.common;


import com.sum.library.view.SwipeRefresh.SwipeRefreshLayout;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayoutDirection;

/**
 * Created by Summer on 2016/10/14.
 */

public class RefreshViewImpl implements RefreshView, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefresh;

    private RefreshLoadListener mListener;

    //统一管理分页数据
    private int mPageIndex = 1;

    private int mPageSize = 10;

    private int mTotalSize = 10;

    public RefreshViewImpl(RefreshLoadListener listener) {
        this.mListener = listener;
    }

    @Override
    public void initRefresh(SwipeRefreshLayout refresh, SwipeRefreshLayoutDirection direction) {
        if (refresh == null) {
            return;
        }
        mRefresh = refresh;
//        refresh.setColorSchemeResources(R.color.colorAccent);
        refresh.setDirection(direction);
        refresh.setOnRefreshListener(this);//子类重设置该监听
    }

    @Override
    public void refreshTop() {
        mPageIndex = 1;
        if (mRefresh != null && !mRefresh.isRefreshing()) {
            mRefresh.post(new Runnable() {
                @Override
                public void run() {
                    mRefresh.setRefreshing(true);
                }
            });
        }

        if (mListener != null) {
            mListener.onRefreshLoadData();//统一刷新数据方法
        }
    }

    @Override
    public void refreshMore() {
     /*   if (mPageIndex * mPageSize >= mTotalSize) {
            if (mRefresh != null && mRefresh.isRefreshing()) {
                mRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefresh.setRefreshing(false);
                    }
                });
            }
            if (mListener != null) {
                mListener.onRefreshNoMore();
            }
        } else */
        {
            mPageIndex++;
            if (mListener != null) {
                mListener.onRefreshLoadData();
            }
        }
    }

    @Override
    public void setTotalSize(int total) {
        mTotalSize = total;
    }

    @Override
    public int getPageIndex() {
        return mPageIndex;
    }

    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if (direction == SwipeRefreshLayoutDirection.TOP) {
            refreshTop();
        } else if (direction == SwipeRefreshLayoutDirection.BOTTOM) {
            refreshMore();
        }
    }
}
