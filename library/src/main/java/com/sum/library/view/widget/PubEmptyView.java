package com.sum.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sum.library.R;

import java.lang.ref.WeakReference;

/**
 * Created by sdl on 2018/5/8.
 * ViewStub 扩展
 */
public class PubEmptyView extends View {

    private WeakReference<View> mInflatedViewRef;

    private int mInflatedId;
    private String mEmptyText;
    private int mEnptyImageRes;

    public PubEmptyView(Context context) {
        this(context, null, 0);
    }

    public PubEmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PubEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PubEmptyView);
        mEmptyText = array.getString(R.styleable.PubEmptyView_pub_empty_text);
        mEnptyImageRes = array.getResourceId(R.styleable.PubEmptyView_pub_empty_img, -1);
        mInflatedId = array.getResourceId(R.styleable.PubEmptyView_pub_empty_layout,R.layout.pub_empty_view);
        array.recycle();
        setVisibility(GONE);
        setWillNotDraw(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
