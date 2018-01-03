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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.sum.library.R

/**
 * Created by sdl on 2018/1/2.
 */
open class PubTitleView : FrameLayout {

    constructor(context: Context) : super(context) {
        initLayout(context, R.layout.pub_title)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PubTitleView)
        val layoutId = array.getResourceId(R.styleable.PubTitleView_title_view, R.layout.pub_title)
        initLayout(context, layoutId)
        mTitle?.text = array.getString(R.styleable.PubTitleView_title_name)

        val dark = array.getBoolean(R.styleable.PubTitleView_title_dark, false)
        if (dark) {
            val drawable = mTitleBack?.drawable
            if (drawable != null) {
                DrawableCompat.setTint(drawable, Color.WHITE)
            }
            mTitle?.setTextColor(Color.WHITE)
        }
        array.recycle()
    }

    private var mTitle: TextView? = null//标题

    private var mTitleBack: ImageView? = null//返回

    private var mTitleRightContent: LinearLayout? = null//右侧按钮

    private var mTitleBgView: View? = null//背景

    open fun getTitleText(): TextView? = mTitle

    open fun getTitleBackImage(): ImageView? = mTitleBack

    open fun getTitleBgView(): View? = mTitleBgView

    private fun initLayout(context: Context, layoutId: Int) {
        val view = LayoutInflater.from(context).inflate(layoutId, this, true)
        initView(view)
    }

    private fun initView(view: View) {
        mTitle = view.findViewById(R.id.pub_title_text)
        mTitleBack = view.findViewById(R.id.pub_title_back)
        mTitleRightContent = view.findViewById(R.id.pub_title_right)
        mTitleBgView = view.findViewById(R.id.pub_title_content)
        defaultSetting()
    }

    private fun defaultSetting() {
        mTitleBack?.setOnClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
    }

    //设置标题
    open fun setTitle(title: CharSequence?) {
        mTitle?.text = title
    }

    //添加文本按钮
    fun addRightTextButton(btnName: String, clickListener: OnClickListener?) {
        val view = TextView(context)
        view.text = btnName
        view.textSize = 14f
        view.gravity = Gravity.CENTER
        view.setPadding(0, 0, SizeUtils.dp2px(12f), 0)
        view.setTextColor(ContextCompat.getColor(context, R.color.pub_title_text_right_color))
        view.setOnClickListener(clickListener)
        addRightView(view)
    }

    //添加图片按钮
    fun addRightImageButton(imageSrc: Int, clickListener: OnClickListener?) {
        val view = ImageView(context)
        view.setImageResource(imageSrc)
        view.scaleType = ImageView.ScaleType.CENTER
        view.setPadding(0, 0, SizeUtils.dp2px(12f), 0)
        view.setOnClickListener(clickListener)
        addRightView(view)
    }

    //添加按钮
    open fun addRightView(view: View) {
        mTitleRightContent?.addView(view, 0, ViewGroup.LayoutParams(-2, -1))
    }

}