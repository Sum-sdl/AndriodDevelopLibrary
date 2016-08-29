package com.sum.library.view.recyclerview.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;

public abstract class OnPageScrollListener extends OnScrollListener
{
    
    private LinearLayoutManager mManager;
    private int mRemainingCount;
    
    public OnPageScrollListener(LinearLayoutManager manager) {
        this(manager,0);
    }
    
    public OnPageScrollListener(LinearLayoutManager manager, int remainingCount) {
        if(manager == null) throw new NullPointerException("manager == null");
        if(remainingCount < 0) throw new IllegalArgumentException("remainingCount < 0");
        this.mManager = manager;
        this.mRemainingCount = remainingCount;
    }
    
    @Override
    public final void onScrollStateChanged(RecyclerView recyclerView, int newState)
    {
        super.onScrollStateChanged(recyclerView, newState);
    }
    
    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItem = mManager.findFirstVisibleItemPosition();
        int visibleItemCount = mManager.getChildCount();
        int totalItemCount = mManager.getItemCount();
        if (recyclerView.getVisibility() == View.GONE)
            return;
        if (visibleItemCount == 0)
            return;
        if (firstVisibleItem + visibleItemCount + mRemainingCount >= totalItemCount)
        {
            onPageScrolled(recyclerView);
        }else {
            onItemCount(firstVisibleItem, visibleItemCount, totalItemCount,dx,dy);
        }
    }

    public void onItemCount(int firstVisible,int visibleCount,int totalCount,int dx,int dy) {
    }

    public void onPageScrolled(RecyclerView recyclerView) {
    }
    
}
