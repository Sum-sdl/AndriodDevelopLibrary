package add_class.utils;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sum on 18/7/30.
 */

public class GirdViewItemSpaceDecoration extends RecyclerView.ItemDecoration {

    private int mSpace = 10;

    public GirdViewItemSpaceDecoration() {
    }

    public GirdViewItemSpaceDecoration(int space) {
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
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

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

        int right = mSpace;
        int bottom = mSpace;
        //最后一列
        if ((itemPosition + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
            right = 0;
        }
        //最后一行
        int lastRawStartIndex = childCount - (childCount % spanCount == 0 ? 3 : childCount % spanCount);
        if (itemPosition >= lastRawStartIndex) {// 如果是最后一行，则不需要绘制底部
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }
}