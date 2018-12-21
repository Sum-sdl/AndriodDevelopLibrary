package com.sum.library.view.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.sum.library.R

/**
 * Created by sdl on 2018/1/2.
 * 自定义title
 */
class PubTitleView : LinearLayout {

    constructor(context: Context) : super(context) {
        initLayoutView(context, R.layout.pub_title_view)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PubTitleView)
        val layoutId = array.getResourceId(R.styleable.PubTitleView_title_view, R.layout.pub_title_view)
        initLayoutView(context, layoutId)

        //内容
        mTitleTextView?.text = array.getString(R.styleable.PubTitleView_title_default_name)

        //字体大小
        val defaultSize = context.resources.getDimensionPixelSize(R.dimen.pub_title_text_size)
        val size = array.getDimensionPixelSize(R.styleable.PubTitleView_title_default_name_text_size, defaultSize).toFloat()
        mTitleTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)

        //返回键
        val resourceId = array.getResourceId(R.styleable.PubTitleView_title_default_back_img, -1)
        if (resourceId != -1) {
            mTitleBackImage?.setImageResource(resourceId)
        }
        //返回键展示
        val backShow = array.getBoolean(R.styleable.PubTitleView_title_default_back_img_show, true)
        if (backShow) {
            mTitleBackImage?.visibility = View.VISIBLE
        } else {
            mTitleBackImage?.visibility = View.GONE
        }

        //背景色
        val bgColor = array.getColor(R.styleable.PubTitleView_title_default_bg_color, -1)
        val bgRes = array.getResourceId(R.styleable.PubTitleView_title_default_bg_resource, -1)
        if (bgRes != -1) {
            setBackgroundResource(bgRes)
        } else if (bgColor != -1) {
            setBackgroundColor(bgColor)
        }


        //状态栏适配
        if (array.getBoolean(R.styleable.PubTitleView_title_default_adjust_bar, false)) {
            val barColor = array.getColor(R.styleable.PubTitleView_title_default_adjust_bar_color, -1)
            if (barColor != -1) {
                addStatusBarHeight(barColor)
            } else {
                addStatusBarHeight()
            }
        }

        //主题色
        val white = array.getBoolean(R.styleable.PubTitleView_title_default_white_theme, false)
        if (white) {
            val drawable = mTitleBackImage?.drawable
            if (drawable != null) {
                DrawableCompat.setTint(drawable, Color.WHITE)
            }
            mTitleTextView?.setTextColor(Color.WHITE)
        } else {
            //字体颜色
            val titleColor = array.getColor(R.styleable.PubTitleView_title_default_name_text_color, ContextCompat.getColor(context, R.color.pub_title_text_color))
            mTitleTextView?.setTextColor(titleColor)
        }

        //返回键tint
        val backImageTint = array.getColor(R.styleable.PubTitleView_title_default_back_img_tint, -1)
        if (backImageTint != -1) {
            val drawable = mTitleBackImage?.drawable
            if (drawable != null) {
                DrawableCompat.setTint(drawable, backImageTint)
            }
        }
        array.recycle()
    }

    private lateinit var mView: View

    var mTitleTextView: TextView? = null//标题

    var mTitleBackImage: ImageView? = null//返回

    private var mTitleRightContent: LinearLayout? = null//右侧按钮

    private fun initLayoutView(context: Context, layoutId: Int) {
        orientation = LinearLayout.VERTICAL
        val view = LayoutInflater.from(context).inflate(layoutId, this, true)
        initView(view)
        mView = view
    }

    private fun initView(view: View) {
        mTitleTextView = view.findViewById(R.id.pub_title_text)
        mTitleBackImage = view.findViewById(R.id.pub_title_back)
        mTitleRightContent = view.findViewById(R.id.pub_title_right)
        defaultSetting()
    }

    private fun defaultSetting() {
        mTitleBackImage?.setOnClickListener {
            if (context is Activity) {
                (context as Activity).onBackPressed()
            }
        }
    }

    //插入一个状态栏高度的view
    private fun addStatusBarHeight() {
        val space = View(context)
        addView(space, 0, LayoutParams(-1, BarUtils.getStatusBarHeight()))
    }

    private fun addStatusBarHeight(color: Int) {
        val space = View(context)
        space.setBackgroundColor(color)
        addView(space, 0, LayoutParams(-1, BarUtils.getStatusBarHeight()))
    }

    //设置标题
    fun setTitle(title: CharSequence?) {
        val old = mTitleTextView?.text
        if (title == old) {
            return
        }
        mTitleTextView?.text = title
    }

    //添加文本按钮
    fun addRightTextButton(btnName: String, clickListener: View.OnClickListener?): TextView {
        return addRightTextButton(btnName, R.color.pub_title_text_right_color, clickListener)
    }

    //添加文本按钮
    fun addRightTextButton(btnName: String, colorRes: Int, clickListener: View.OnClickListener?): TextView {
        val view = TextView(context)
        view.text = btnName
        view.textSize = 14f
        view.gravity = Gravity.CENTER
        view.setPadding(0, 0, SizeUtils.dp2px(12f), 0)
        view.setTextColor(ContextCompat.getColor(context, colorRes))
        view.setOnClickListener(clickListener)
        addRightView(view)
        return view
    }

    //添加图片按钮
    fun addRightImageButton(imageSrc: Int, clickListener: View.OnClickListener?): ImageView {
        val view = ImageView(context)
        view.setImageResource(imageSrc)
        view.scaleType = ImageView.ScaleType.CENTER
        view.setPadding(0, 0, SizeUtils.dp2px(12f), 0)
        view.setOnClickListener(clickListener)
        addRightView(view)
        return view
    }

    //添加按钮
    fun addRightView(view: View): View {
        mTitleRightContent?.addView(view, 0, ViewGroup.LayoutParams(-2, -1))
        return view
    }

}