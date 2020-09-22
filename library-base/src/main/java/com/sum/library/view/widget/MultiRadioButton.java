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

    //tab显示的图片
    private Drawable mImageResDefault = null, mImageResChecked = null;

    //文本颜色
    private int mColorDefault, mColorChecked;
    //图片Tint颜色
    private int mImageNorColor, mImageCheckedColor;

    private int mTextSize;

    private boolean mDefaultChecked = false;

    private String mName;

    //处理选中状态下的图片操作
    private int mShowType;//0:颜色, 1:图片 3：关闭图片选择 4：全部关闭选中

    public MultiRadioButton(Context context) {
        this(context, null, 0);
    }

    public MultiRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        updateUi(mDefaultChecked);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultiRadioButton);
        //读取数据
        mShowType = 0;

        //文本颜色
        mColorDefault = array.getColor(R.styleable.MultiRadioButton_multiColorDefault, 0xfff);
        mColorChecked = array.getColor(R.styleable.MultiRadioButton_multiColorChecked, 0xff0);
        //图片颜色
        mImageNorColor = array.getColor(R.styleable.MultiRadioButton_multiColorImageDefault, -1);
        if (mImageNorColor == -1) {
            mImageNorColor = mColorDefault;
        }
        mImageCheckedColor = array.getColor(R.styleable.MultiRadioButton_multiColorImageChecked, -1);
        if (mImageCheckedColor == -1) {
            mImageCheckedColor = mColorChecked;
        }

        //图片状态
        int imageResDefault = array.getResourceId(R.styleable.MultiRadioButton_multiDrawableDefault, -1);
        if (imageResDefault != -1) {
            mImageResDefault = ContextCompat.getDrawable(context, imageResDefault);
        }
        int imageResChecked = array.getResourceId(R.styleable.MultiRadioButton_multiDrawableChecked, -1);
        if (imageResChecked != -1) {
            mImageResChecked = ContextCompat.getDrawable(context, imageResChecked);
        }
        if (imageResChecked != -1) {
            mShowType = 1;
        }

        //获取手动设置的状态
        int type = array.getInt(R.styleable.MultiRadioButton_multiCheckedType, -1);
        if (type != -1) {
            mShowType = type;
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
            updateUi(checked);
        }
    }

    private void updateUi(boolean checked) {
        //不显示选中色
        if (mShowType != 4) {
            if (checked) {
                mTextView.setTextColor(mColorChecked);
            } else {
                mTextView.setTextColor(mColorDefault);
            }
        }
        //Tint处理
        if (mShowType == 0) {
            int color = mImageNorColor;
            if (checked) {
                color = mImageCheckedColor;
            }
            if (mImageResDefault != null) {
                DrawableCompat.setTint(mImageResDefault, color);
            }
            mImageView.setImageDrawable(mImageResDefault);
        } else if (mShowType == 1) {
            //图片处理
            if (checked) {
                mImageView.setImageDrawable(mImageResDefault);
            } else {
                mImageView.setImageDrawable(mImageResChecked);
            }
        } else if (mShowType == 3 || mShowType == 4) {
            //只显示图片，不增加着色
            mImageView.setImageDrawable(mImageResDefault);
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

    //更新显示内容
    public void refreshUi() {
        updateUi(mChecked);
    }

    public MultiRadioButton setColorDefault(int mColorDefault) {
        this.mColorDefault = mColorDefault;
        return this;
    }

    public MultiRadioButton setColorChecked(int mColorChecked) {
        this.mColorChecked = mColorChecked;
        return this;
    }

    public MultiRadioButton setImageNorColor(int mImageNor) {
        this.mImageNorColor = mImageNor;
        return this;
    }

    public MultiRadioButton setImageCheckedColor(int mImageChecked) {
        this.mImageCheckedColor = mImageChecked;
        return this;
    }

    public MultiRadioButton setImageResDefault(Drawable mImageResDefault) {
        this.mImageResDefault = mImageResDefault;
        return this;
    }

    public MultiRadioButton setImageResChecked(Drawable mImageResChecked) {
        this.mImageResChecked = mImageResChecked;
        return this;
    }

    public MultiRadioButton setShowType(int mShowType) {
        this.mShowType = mShowType;
        return this;
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
