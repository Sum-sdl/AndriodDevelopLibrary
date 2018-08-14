package com.sum.library.view.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
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
open class PubTitleView : LinearLayout {

    constructor(context: Context) : super(context) {
        initLayout(context, R.layout.pub_title_view)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PubTitleView)
        val layoutId = array.getResourceId(R.styleable.PubTitleView_title_view, R.layout.pub_title_view)
        initLayout(context, layoutId)

        mTitle?.text = array.getString(R.styleable.PubTitleView_title_name)
        val resourceId = array.getResourceId(R.styleable.PubTitleView_title_back_img, -1)
        if (resourceId != -1) {
            mTitleBack?.setImageResource(resourceId)
        }

        val white = array.getBoolean(R.styleable.PubTitleView_title_white, false)
        if (white) {
            val drawable = mTitleBack?.drawable
            if (drawable != null) {
                DrawableCompat.setTint(drawable, Color.WHITE)
            }
            mTitle?.setTextColor(Color.WHITE)
        } else {
            val drawable = mTitleBack?.drawable
            if (drawable != null) {
                DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.pub_title_text_color))
            }
            mTitle?.setTextColor(ContextCompat.getColor(context, R.color.pub_title_text_color))
        }
        array.recycle()
    }

    private lateinit var mView: View

    private var mTitle: TextView? = null//标题

    private var mTitleBack: ImageView? = null//返回

    private var mTitleRightContent: LinearLayout? = null//右侧按钮

    private var mTitleBgView: View? = null//背景

    open fun getTitleText(): TextView? = mTitle

    open fun getTitleBackImage(): ImageView? = mTitleBack

    open fun getTitleBgView(): View? = mTitleBgView

    private fun initLayout(context: Context, layoutId: Int) {
        orientation = LinearLayout.VERTICAL
        val view = LayoutInflater.from(context).inflate(layoutId, this, true)
        initView(view)
        mView = view
    }

    private fun initView(view: View) {
        mTitle = view.findViewById(R.id.pub_title_text)
        mTitleBack = view.findViewById(R.id.pub_title_back)
        mTitleRightContent = view.findViewById(R.id.pub_title_right)
//        mTitleBgView = view.findViewById(R.id.pub_title_content)
        mTitleBgView = this
        defaultSetting()
    }

    private fun defaultSetting() {
        mTitleBack?.setOnClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
    }

    //插入一个状态栏高度的view
    fun addStatusBarHeight() {
        val space = View(context)
        addView(space, 0, LayoutParams(-1, BarUtils.getStatusBarHeight()))
    }

    //设置标题
    open fun setTitle(title: CharSequence?) {
        mTitle?.text = title
    }

    //添加文本按钮
    fun addRightTextButton(btnName: String, clickListener: View.OnClickListener?): TextView {
        val view = TextView(context)
        view.text = btnName
        view.textSize = 14f
        view.gravity = Gravity.CENTER
        view.setPadding(0, 0, SizeUtils.dp2px(12f), 0)
        view.setTextColor(ContextCompat.getColor(context, R.color.pub_title_text_right_color))
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
    open fun addRightView(view: View) {
        mTitleRightContent?.addView(view, 0, ViewGroup.LayoutParams(-2, -1))
    }

}