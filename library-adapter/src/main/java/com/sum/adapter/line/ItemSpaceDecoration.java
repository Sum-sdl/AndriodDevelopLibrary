package com.sum.adapter.line;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sum on 18/7/30.
 * 很有用的类，用来设置RecyclerView的行间距
 */

public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {

    private int mSpace = 10;

    public ItemSpaceDecoration() {
    }

    public ItemSpaceDecoration(int space) {
        mSpace = space;
    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = 1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (spanCount == 1) {
            if (parent.getLayoutManager().canScrollHorizontally()) {
                outRect.set(0, 0, mSpace, 0);
            } else {
                outRect.set(0, 0, 0, mSpace);
            }
            //最后一行忽略
            if (itemPosition == childCount - 1) {
                outRect.set(0, 0, 0, 0);
            }
            return;
        }

        int left = mSpace;
        int bottom = mSpace;
        if (itemPosition % spanCount == 0) {
            left = 0;
        }
        int lastRawStartIndex = childCount - (childCount % spanCount == 0 ? 3 : childCount % spanCount);
        if (itemPosition >= lastRawStartIndex) {// 如果是最后一行，则不需要绘制底部
            bottom = 0;
        }
        outRect.set(left, 0, 0, bottom);
    }
}