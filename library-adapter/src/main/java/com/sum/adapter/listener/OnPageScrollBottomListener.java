package com.sum.adapter.listener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import android.view.View;

/**
 * Created by sdl on 2017/12/29.
 */
public class OnPageScrollBottomListener extends OnScrollListener {

    private LinearLayoutManager mManager;
    /**
     * 用作提前几个到底部
     */
    private int mRemainingCount;

    public OnPageScrollBottomListener(LinearLayoutManager manager) {
        this(manager, 0);
    }

    public OnPageScrollBottomListener(LinearLayoutManager manager, int remainingCount) {
        if (manager == null) {
            throw new NullPointerException("manager == null");
        }
        if (remainingCount < 0) {
            throw new IllegalArgumentException("remainingCount < 0");
        }
        this.mManager = manager;
        this.mRemainingCount = remainingCount;
    }

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (recyclerView.getVisibility() == View.GONE) {
            return;
        }
        int firstVisibleItem = mManager.findFirstVisibleItemPosition();
        int visibleItemCount = mManager.getChildCount();
        int totalItemCount = mManager.getItemCount();
        if (visibleItemCount == 0) {
            return;
        }
        if (firstVisibleItem + visibleItemCount + mRemainingCount >= totalItemCount) {
            onPageScrolledBottom();
        }
    }

    public void onPageScrolledBottom() {
    }

}
