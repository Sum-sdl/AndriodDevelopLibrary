package com.sum.adapter.sticky;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Summer on 2016/8/29.
 */
public class StickyHeadDecoration extends RecyclerView.ItemDecoration {

    private StickyHeadView mHeadView;
    private HeadViewManager mHeadViewManager;
    private HeadViewPositionCalculator mHeadViewCalculator;
    private final SparseArray<Rect> mHeaderRectes = new SparseArray<>();

    private final Rect mTempRect = new Rect();

    public StickyHeadDecoration(StickyHeadView headView) {
        mHeadView = headView;
        mHeadViewManager = new HeadViewManager();
        mHeadViewCalculator = new HeadViewPositionCalculator();
    }

    //将View的margin保存到Rect中
    private static void resolveMargin(Rect margins, View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            margins.set(
                    marginLayoutParams.leftMargin,
                    marginLayoutParams.topMargin,
                    marginLayoutParams.rightMargin,
                    marginLayoutParams.bottomMargin);
        } else {
            margins.set(0, 0, 0, 0);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int itemPos = parent.getChildAdapterPosition(view);
        if (itemPos == RecyclerView.NO_POSITION) {
            return;
        }
        //判断当前位置是否需要HeadView
        if (mHeadViewCalculator.needStickHeadView(itemPos)) {
            View headView = mHeadViewManager.getHeadView(parent, itemPos);
            resolveMargin(mTempRect, headView);
            //HeadView 占位
            outRect.top = headView.getHeight() + mTempRect.top + mTempRect.bottom;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        //绘制View
        int childCount = parent.getChildCount();
        if (childCount <= 0 || mHeadView.getItemCount() <= 0) {
            return;
        }

        for (int i = 0; i < childCount; i++) {

            View childAt = parent.getChildAt(i);

            int position = parent.getChildAdapterPosition(childAt);

            if (position == RecyclerView.NO_POSITION) {
                continue;
            }

            boolean hasHeadView = mHeadViewCalculator.hasStickyHeadView(childAt, position);
            boolean needStickHeadView = mHeadViewCalculator.needStickHeadView(position);

            if (hasHeadView || needStickHeadView) {
                View headView = mHeadViewManager.getHeadView(parent, position);
                Rect rect = mHeaderRectes.get(position);
                if (rect == null) {
                    rect = new Rect();
                    mHeaderRectes.put(position, rect);
                }
                //初始化HeadView的位置
                mHeadViewCalculator.resolveHeadViewBound(parent, rect, headView, childAt, hasHeadView, position);
                drawHead(parent, headView, c, rect);
            }
        }
    }

    private void drawHead(RecyclerView recyclerView, View headView, Canvas canvas, Rect offset) {
        canvas.save();

        if (recyclerView.getLayoutManager().getClipToPadding()) {
            // Clip drawing of headers to the padding of the RecyclerView. Avoids drawing in the padding
            resolveMargin(mTempRect, headView);
            mTempRect.set(
                    recyclerView.getPaddingLeft(),
                    recyclerView.getPaddingTop(),
                    recyclerView.getWidth() - recyclerView.getPaddingRight() - mTempRect.right,
                    recyclerView.getHeight() - recyclerView.getPaddingBottom());
            canvas.clipRect(mTempRect);
        }

        canvas.translate(offset.left, offset.top);

        headView.draw(canvas);

        canvas.restore();
    }


    private class HeadViewPositionCalculator {

        //临时存储View的margin属性
        private final Rect mTempRect1 = new Rect();

        private final Rect mTempRect2 = new Rect();

        public boolean hasStickyHeadView(View itemView, int position) {
            resolveMargin(mTempRect1, itemView);
            return itemView.getTop() <= mTempRect1.top && mHeadView.getHeadId(position) >= 0;
        }

        //当前位置是否需要HeadView 当前位置和上一个位置的HeadId不一致
        public boolean needStickHeadView(int position) {
            if (indexOutOfBounds(position)) {
                return false;
            }
            //负数时候可以让Item不偏移
            long headerId = mHeadView.getHeadId(position);
            if (headerId < 0) {
                return false;
            }
            long nextHeadId = -1;
            int nextPos = position - 1;
            if (!indexOutOfBounds(nextPos)) {
                nextHeadId = mHeadView.getHeadId(nextPos);
            }
            return position == 0 || nextHeadId != headerId;
        }

        private boolean indexOutOfBounds(int position) {
            return position < 0 || position >= mHeadView.getItemCount();
        }

        //设置HeadView的边界
        public void resolveHeadViewBound(RecyclerView recyclerView, Rect bound, View headView, View itemView, boolean firstHeader, int pos) {
            //设置初始化view
            resolveMargin(mTempRect1, headView);
            int defaultX, defaultY;
            //item的左距离+headView的左距离
            defaultX = itemView.getLeft() + mTempRect1.left;
            //item的top-headView.height-headView.bottom
            defaultY = Math.max(itemView.getTop() - mTempRect1.bottom - headView.getHeight(), getListTop(recyclerView) + mTempRect1.top);
            //设置HeadView相对于ItemView的距离
            bound.set(defaultX, defaultY, defaultX + headView.getWidth(), defaultY + headView.getHeight());

            //两个HeadView位移到一起处理
            if (firstHeader && isStickyHeaderBeingPushedOffscreen(recyclerView, headView)) {

                View nextItem = getFirstViewUnObscuredByHeader(recyclerView, headView);
                if (nextItem == null) {
                    return;
                }
                int nextItemView = recyclerView.getChildAdapterPosition(nextItem);

                View nextHeadView = mHeadViewManager.getHeadView(recyclerView, nextItemView);

                resolveMargin(mTempRect1, nextHeadView);

                resolveMargin(mTempRect2, headView);

                int topOfStickyHeader = getListTop(recyclerView) + mTempRect2.top + mTempRect2.bottom;

                int shiftFromNextHeader = nextItem.getTop() - nextHeadView.getHeight() - mTempRect1.bottom - mTempRect1.top - headView.getHeight() - topOfStickyHeader;

                if (shiftFromNextHeader < topOfStickyHeader) {
                    bound.top += shiftFromNextHeader;
                }
            }
        }

        private boolean isStickyHeaderBeingPushedOffscreen(RecyclerView recyclerView, View stickyHeader) {

            View viewAfterHeader = getFirstViewUnObscuredByHeader(recyclerView, stickyHeader);

            int firstViewUnderHeaderPosition = recyclerView.getChildAdapterPosition(viewAfterHeader);
            if (firstViewUnderHeaderPosition == RecyclerView.NO_POSITION) {
                return false;
            }
            boolean pushing = false;
            if (firstViewUnderHeaderPosition > 0 && needStickHeadView(firstViewUnderHeaderPosition)) {
                View nextHeader = mHeadViewManager.getHeadView(recyclerView, firstViewUnderHeaderPosition);
                resolveMargin(mTempRect1, nextHeader);
                resolveMargin(mTempRect2, stickyHeader);

                int topOfNextHeader = viewAfterHeader.getTop() - mTempRect1.bottom - nextHeader.getHeight() - mTempRect1.top;
                int bottomOfThisHeader = recyclerView.getPaddingTop() + stickyHeader.getBottom() + mTempRect2.top + mTempRect2.bottom;
                //判断两个headView是否已经接触
                if (topOfNextHeader < bottomOfThisHeader) {
                    pushing = true;
                } else {
                    firstViewUnderHeaderPosition--;
                }
            }
            mHeadView.firstHead(mHeadView.getHeadId(firstViewUnderHeaderPosition), firstViewUnderHeaderPosition);
            return pushing;
        }

        //查询第一个未被遮盖的Item 的View
        private View getFirstViewUnObscuredByHeader(RecyclerView parent, View headView) {
            for (int i = 0; i >= 0 && i <= parent.getChildCount() - 1; i++) {
                View child = parent.getChildAt(i);
                if (!itemIsObscuredByHeader(parent, child, headView)) {
                    return child;
                }
            }
            return null;
        }

        private boolean itemIsObscuredByHeader(RecyclerView parent, View item, View header) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) item.getLayoutParams();

            resolveMargin(mTempRect1, header);

            int adapterPosition = parent.getChildAdapterPosition(item);

            if (adapterPosition == RecyclerView.NO_POSITION || mHeadViewManager.getHeadView(parent, adapterPosition) != header) {
                return false;
            }
            int itemTop = item.getTop() - layoutParams.topMargin;
            int headerBottom = header.getBottom() + mTempRect1.bottom + mTempRect1.top;
            if (itemTop > headerBottom) {
                return false;
            }

            return true;
        }

        private int getListTop(RecyclerView view) {
            if (view.getLayoutManager().getClipToPadding()) {
                return view.getPaddingTop();
            } else {
                return 0;
            }
        }
    }


    private class HeadViewManager {
        //缓存的HeadView
        private LongSparseArray<View> mCacheView = new LongSparseArray<>();

        public View getHeadView(RecyclerView parent, int position) {
            //headView 的Id
            long headId = mHeadView.getHeadId(position);

            View headView = mCacheView.get(headId);

            if (headView == null) {
                View view = mHeadView.onCreateHeadView(parent);
                mHeadView.onBindHeadView(view, position);
                headView = view;
                if (headView.getLayoutParams() == null) {
                    headView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                }
                int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

                int childWidth = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), headView.getLayoutParams().width);
                int childHeight = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), headView.getLayoutParams().height);

                headView.measure(childWidth, childHeight);

                headView.layout(0, 0, headView.getMeasuredWidth(), headView.getMeasuredHeight());


                mCacheView.put(headId, headView);
            }
            return headView;
        }

        public void clear() {
            mCacheView.clear();
        }
    }
}
