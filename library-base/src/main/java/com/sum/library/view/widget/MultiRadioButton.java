package com.sum.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.sum.library.R;

/**
 * Created by Summer on 2016/9/8.
 */
public class MultiRadioButton extends FrameLayout implements Checkable {

    private boolean mChecked = false;

    private AppCompatImageView mImageView;

    private TextView mTextView;

    private FrameLayout mIvParent;

    private int mImageResDefault = -1, mImageResChecked = -1;

    private int mColorDefault, mColorChecked;

    private int mTextSize;

    private boolean mDefaultChecked = false;

    private String mName;

    private int mShowType;//0:颜色 1:图片

    private Context mContext;

    public MultiRadioButton(Context context) {
        this(context, null, 0);
    }

    public MultiRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.cus_multi_radio_button, this, true);
        mImageView = findViewById(R.id.multi_iv_image);
        mTextView = findViewById(R.id.multi_tv_name);
        mIvParent = findViewById(R.id.multi_iv_parent);

        loadAttrs(context, attrs);

        if (mTextSize > 0) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        if (!TextUtils.isEmpty(mName)) {
            mTextView.setText(mName);
        }
        //初始化
        setChecked(mDefaultChecked);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultiRadioButton);
        //读取数据
        mShowType = 0;

        mColorDefault = array.getColor(R.styleable.MultiRadioButton_multiColorDefault, 0xfff);

        mColorChecked = array.getColor(R.styleable.MultiRadioButton_multiColorChecked, 0xff0);

        mImageResDefault = array.getResourceId(R.styleable.MultiRadioButton_multiDrawableDefault, -1);

        mImageResChecked = array.getResourceId(R.styleable.MultiRadioButton_multiDrawableChecked, -1);

        if (mImageResChecked != -1) {
            mShowType = 1;
        }
        mName = array.getString(R.styleable.MultiRadioButton_multiText);
        mDefaultChecked = array.getBoolean(R.styleable.MultiRadioButton_multiChecked, false);

        mTextSize = array.getDimensionPixelSize(R.styleable.MultiRadioButton_multiTextSize, 0);


        MarginLayoutParams params = (MarginLayoutParams) mImageView.getLayoutParams();
        int iv_width = array.getDimensionPixelSize(R.styleable.MultiRadioButton_multiDrawableWidth, -1);
        int iv_height = array.getDimensionPixelSize(R.styleable.MultiRadioButton_multiDrawableWidth, -1);
        int iv_mar_l = array.getDimensionPixelSize(R.styleable.MultiRadioButton_multiDrawableMarginLeft, 0);
        int iv_mar_r = array.getDimensionPixelSize(R.styleable.MultiRadioButton_multiDrawableMarginRight, 0);
        int iv_mar_t = array.getDimensionPixelSize(R.styleable.MultiRadioButton_multiDrawableMarginTop, 0);
        int iv_mar_b = array.getDimensionPixelSize(R.styleable.MultiRadioButton_multiDrawableMarginBottom, 0);
        if (iv_width != -1) {
            params.width = iv_width;
        }
        if (iv_height != -1) {
            params.height = iv_height;
        }
        params.topMargin = iv_mar_t;
        params.bottomMargin = iv_mar_b;
        params.leftMargin = iv_mar_l;
        params.rightMargin = iv_mar_r;
        mImageView.setLayoutParams(params);
        array.recycle();
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (mChecked) {
                mTextView.setTextColor(mColorChecked);
            } else {
                mTextView.setTextColor(mColorDefault);
            }
            //Tint处理
            if (mShowType == 0) {
                int color = mColorDefault;
                if (mChecked) {
                    color = mColorChecked;
                }
                Drawable drawable = ContextCompat.getDrawable(mContext, mImageResDefault);
                if (drawable != null) {
                    DrawableCompat.setTint(drawable, color);
                }
                mImageView.setImageDrawable(drawable);
            } else {
                //图片处理
                if (mChecked) {
                    mImageView.setImageResource(mImageResDefault);
                } else {
                    mImageView.setImageResource(mImageResChecked);
                }
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    //调整图片位置
    public FrameLayout getIvParent() {
        return mIvParent;
    }

    public AppCompatImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

}
