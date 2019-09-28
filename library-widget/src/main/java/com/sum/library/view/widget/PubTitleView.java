package com.sum.library.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sum.library.R;

/**
 * Created by sdl on 2019-06-19.
 */
public class PubTitleView extends LinearLayout {

    private Context context;

    public PubTitleView(Context context) {
        this(context, null, 0);
    }

    public PubTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PubTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PubTitleView);
        int layoutId = array.getResourceId(R.styleable.PubTitleView_title_view, R.layout.pub_title_view);
        initLayoutView(context, layoutId);

        //内容
        mTitleTextView.setText(array.getString(R.styleable.PubTitleView_title_default_name));

        //字体大小
        int defaultSize = context.getResources().getDimensionPixelSize(R.dimen.pub_title_text_size);
        int size = array.getDimensionPixelSize(R.styleable.PubTitleView_title_default_name_text_size, defaultSize);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        //返回键
        int resourceId = array.getResourceId(R.styleable.PubTitleView_title_default_back_img, -1);
        if (resourceId != -1) {
            mTitleBackImage.setImageResource(resourceId);
        }
        //返回键展示
        boolean backShow = array.getBoolean(R.styleable.PubTitleView_title_default_back_img_show, true);
        if (backShow) {
            mTitleBackImage.setVisibility(View.VISIBLE);
        } else {
            mTitleBackImage.setVisibility(View.GONE);
        }

        //背景色
        int bgColor = array.getColor(R.styleable.PubTitleView_title_default_bg_color, -1);
        //优先设置资源
        int bgRes = array.getResourceId(R.styleable.PubTitleView_title_default_bg_resource, -1);
        if (bgRes != -1) {
            setBackgroundResource(bgRes);
        } else if (bgColor != -1) {
            setBackgroundColor(bgColor);
        }

        //状态栏适配
        if (array.getBoolean(R.styleable.PubTitleView_title_default_adjust_bar, false)) {
            int barColor = array.getColor(R.styleable.PubTitleView_title_default_adjust_bar_color, -1);
            if (barColor != -1) {
                addStatusBarHeight(barColor);
            } else {
                addStatusBarHeight();
            }
        }

        //主题色
        boolean white = array.getBoolean(R.styleable.PubTitleView_title_default_white_theme, false);
        if (white) {
            Drawable drawable = mTitleBackImage.getDrawable();
            if (drawable != null) {
                DrawableCompat.setTint(drawable, Color.WHITE);
            }
            mTitleTextView.setTextColor(Color.WHITE);
        } else {
            //字体颜色
            int titleColor = array.getColor(R.styleable.PubTitleView_title_default_name_text_color, ContextCompat.getColor(context, R.color.pub_title_text_color));
            mTitleTextView.setTextColor(titleColor);
        }

        //返回键tint
        int backImageTint = array.getColor(R.styleable.PubTitleView_title_default_back_img_tint, -1);
        if (backImageTint != -1) {
            Drawable drawable = mTitleBackImage.getDrawable();
            if (drawable != null) {
                DrawableCompat.setTint(drawable, backImageTint);
            }
        }
        array.recycle();
    }


    private View mView;

    private TextView mTitleTextView = null;//标题

    public ImageView mTitleBackImage = null;//返回

    private LinearLayout mTitleRightContent = null;//右侧按钮

    public ImageView getTitleBackImage() {
        return mTitleBackImage;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    private void initLayoutView(Context context, int layoutId) {
        setOrientation(LinearLayout.VERTICAL);
        View view = LayoutInflater.from(context).inflate(layoutId, this, true);
        initView(view);
        mView = view;
    }


    private void initView(View view) {
        mTitleTextView = view.findViewById(R.id.pub_title_text);
        mTitleBackImage = view.findViewById(R.id.pub_title_back);
        mTitleRightContent = view.findViewById(R.id.pub_title_right);
        mTitleBackImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });
    }


    //插入一个状态栏高度的view
    private void addStatusBarHeight() {
        View space = new View(context);
        addView(space, 0, new LayoutParams(-1, getStatusBarHeight()));
    }

    private void addStatusBarHeight(int color) {
        View space = new View(context);
        space.setBackgroundColor(color);
        addView(space, 0, new LayoutParams(-1, getStatusBarHeight()));
    }

    private int dp2px(Float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


    //设置标题
    public void setTitle(CharSequence title) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
    }

    //添加文本按钮
    public TextView addRightTextButton(String btnName, OnClickListener clickListener) {
        return addRightTextButton(btnName, R.color.pub_title_text_right_color, clickListener);
    }

    //添加文本按钮
    public TextView addRightTextButton(String btnName, int colorRes, OnClickListener clickListener) {
        TextView view = new TextView(context);
        view.setText(btnName);
        view.setTextSize(14f);
        view.setGravity(Gravity.CENTER);
        view.setPadding(0, 0, dp2px(12f), 0);
        view.setTextColor(ContextCompat.getColor(context, colorRes));
        view.setOnClickListener(clickListener);
        addRightView(view);
        return view;
    }

    //添加图片按钮
    public ImageView addRightImageButton(int imageSrc, OnClickListener clickListener) {
        ImageView view = new ImageView(context);
        view.setImageResource(imageSrc);
        view.setScaleType(ImageView.ScaleType.CENTER);
        view.setPadding(0, 0, dp2px(12f), 0);
        view.setOnClickListener(clickListener);
        addRightView(view);
        return view;
    }

    //添加按钮
    public View addRightView(View view) {
        mTitleRightContent.addView(view, 0, new ViewGroup.LayoutParams(-2, -1));
        return view;
    }

    //添加按钮
    public View addRightView(View view, ViewGroup.LayoutParams params) {
        mTitleRightContent.addView(view, 0, params);
        return view;
    }

    //添加按钮
    public View addRightCustomView(View view) {
        mTitleRightContent.addView(view);
        return view;
    }

    public LinearLayout getTitleRightContent() {
        return mTitleRightContent;
    }

}
