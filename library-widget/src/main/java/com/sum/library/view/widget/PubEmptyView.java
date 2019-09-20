package com.sum.library.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sum.library.R;

import java.lang.ref.WeakReference;

/**
 * Created by sdl on 2018/5/8.
 * ViewStub 扩展
 */
public class PubEmptyView extends View {

    private WeakReference<View> mInflatedViewRef;
    private Context mContext;
    private int mInflatedId;
    private CharSequence mEmptyText;
    private int mEmptyImageRes;
    private int mBgColor;
    private View mEmptyAddView;

    private OnClickListener mEmptyViewClick;

    public PubEmptyView(Context context) {
        this(context, null, 0);
    }

    public PubEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PubEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PubEmptyView);
        mEmptyText = array.getString(R.styleable.PubEmptyView_pub_empty_text);
        mEmptyImageRes = array.getResourceId(R.styleable.PubEmptyView_pub_empty_img, -1);
        mInflatedId = array.getResourceId(R.styleable.PubEmptyView_pub_empty_layout, R.layout.pub_empty_view);
        mBgColor = array.getColor(R.styleable.PubEmptyView_pub_empty_bg_color, -1);

        array.recycle();
        setVisibility(GONE);
        setWillNotDraw(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(0, 0);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
    }

    //空图片
    public void setEmptyImageRes(int mEmptyImageRes) {
        this.mEmptyImageRes = mEmptyImageRes;
        updateEmptyView();
    }

    //空文字
    public void setEmptyText(CharSequence mEmptyText) {
        this.mEmptyText = mEmptyText;
        updateEmptyView();
    }

    //添加内容view
    public void addEmptyView(View emptyView) {
        mEmptyAddView = emptyView;
        updateEmptyView();
    }

    public void setEmptyViewClickListener(OnClickListener emptyViewClick) {
        this.mEmptyViewClick = emptyViewClick;
        updateEmptyView();
    }

    @Override
    public void setVisibility(int visibility) {
        if (mInflatedViewRef != null) {
            View view = mInflatedViewRef.get();
            if (view != null) {
                view.setVisibility(visibility);
            } else {
                throw new IllegalStateException("setVisibility called on un-referenced view");
            }
        } else {
            super.setVisibility(visibility);
            if (visibility == VISIBLE || visibility == INVISIBLE) {
                emptyInflate();
            }
        }
    }

    @Override
    public int getVisibility() {
        if (mInflatedViewRef != null) {
            View view = mInflatedViewRef.get();
            if (view != null) {
                return view.getVisibility();
            }
        }
        return super.getVisibility();
    }

    private void emptyInflate() {
        final ViewParent viewParent = getParent();

        if (viewParent != null && viewParent instanceof ViewGroup) {
            if (mInflatedId != 0) {
                final ViewGroup parent = (ViewGroup) viewParent;
                final View view = inflateViewNoAdd(parent);
                replaceSelfWithView(view, parent);
                mInflatedViewRef = new WeakReference<>(view);
            } else {
                throw new IllegalArgumentException("PubEmptyView must have a valid layoutResource");
            }
        } else {
            throw new IllegalStateException("PubEmptyView must have a non-null ViewGroup viewParent");
        }
    }

    private void updateEmptyView() {
        if (mInflatedViewRef != null) {
            View view = mInflatedViewRef.get();
            if (view != null) {
                updateUI(view);
            }
        }
    }

    private View inflateViewNoAdd(ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).inflate(mInflatedId, parent, false);
        if (mInflatedId != NO_ID) {
            view.setId(mInflatedId);
        }
        updateUI(view);

        return view;
    }

    private void updateUI(View view) {
        //默认样式
        if (mInflatedId == R.layout.pub_empty_view) {
            ImageView image = view.findViewById(R.id.pub_empty_img);
            if (image != null && mEmptyImageRes != -1) {
                image.setImageResource(mEmptyImageRes);
            }
            TextView content = view.findViewById(R.id.pub_empty_text);
            if (content != null && !TextUtils.isEmpty(mEmptyText)) {
                content.setText(mEmptyText);
            }
            if (mBgColor != -1) {
                view.setBackgroundColor(mBgColor);
            }
            if (mEmptyAddView != null) {
                LinearLayout layout = view.findViewById(R.id.pub_empty_content);
                layout.addView(mEmptyAddView);
            }
        }
        view.setOnClickListener(mEmptyViewClick);
    }

    private void replaceSelfWithView(View view, ViewGroup parent) {
        final int index = parent.indexOfChild(this);
        parent.removeViewInLayout(this);
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            parent.addView(view, index, layoutParams);
        } else {
            parent.addView(view, index);
        }
    }


}
