package com.sum.library.view.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sum on 15/12/29.
 */
public class TagLayout extends ViewGroup {

    private Context mContext;
    private int mLineSpacing;
    private int mTagSpacing;
    private List<String> mTags;

    private onFasterClickListener listener;

    public TagLayout(Context context) {
        super(context);
        init(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > width) {
                childLeft = paddingLeft;
                childTop += mLineSpacing + lineHeight;
                lineHeight = childHeight;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + mTagSpacing;
        }
    }

    private void init(Context context) {
        mContext = context;
        mLineSpacing = 12;
        mTagSpacing = 10;
    }

    public void setArrayAdapter(List<String> adapter) {
        mTags = adapter;
        drawLayout();
    }

    private void drawLayout() {
        if (mTags == null || mTags.size() == 0) {
            return;
        }
        this.removeAllViews();
        for (int i = 0; i < mTags.size(); i++) {
            final String st = mTags.get(i);
            if (TextUtils.isEmpty(st)) {
                continue;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wantHeight = 0;
        int wantWidth = resolveSize(0, widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int childLeft = paddingLeft;
        int childTop = paddingTop;
        int lineHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View childView = getChildAt(i);
            LayoutParams params = childView.getLayoutParams();
            childView.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, params.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, params.height)
            );
            int childHeight = childView.getMeasuredHeight();
            int childWidth = childView.getMeasuredWidth();
            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > wantWidth) {
                childLeft = paddingLeft;
                childTop += mLineSpacing + childHeight;
                lineHeight = childHeight;
            }

            childLeft += childWidth + mTagSpacing;
        }
        wantHeight += childTop + lineHeight + paddingBottom;
        setMeasuredDimension(wantWidth, resolveSize(wantHeight, heightMeasureSpec));
    }

    public void setListener(onFasterClickListener listener) {
        this.listener = listener;
    }

    public interface onFasterClickListener {
        void onSearchClick(String text);
    }
}
