package com.sum.library.view.widget

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.TextUtils
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
        val name = array.getString(R.styleable.PubTitleView_title_name)
        if (!TextUtils.isEmpty(name)) {
            mTitle.text = name
        }

        array.recycle()
    }

    lateinit var mTitle: TextView

    lateinit var mTitleBack: ImageView

     lateinit var mTitleRightContent: LinearLayout

    lateinit var mTitleBgView: View

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
        mTitleBack.setOnClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
    }

    fun setTitle(title: CharSequence?) {
        mTitle.text = title
    }

    fun addRightTextButton(btnName: String, clickListener: OnClickListener) {
        val view = TextView(context)
        view.text = btnName
        view.textSize = 14f
        view.gravity = Gravity.CENTER
        view.setPadding(0, 0, SizeUtils.dp2px(12f), 0)
        view.setTextColor(ContextCompat.getColor(context, R.color.pub_title_text_right_color))
        view.setOnClickListener(clickListener)
        mTitleRightContent.addView(view, 0, ViewGroup.LayoutParams(-2, -1))
    }

    fun addRightImageButton(imageSrc: Int, clickListener: OnClickListener) {
        val view = ImageView(context)
        view.setImageResource(imageSrc)
        view.scaleType = ImageView.ScaleType.CENTER
        view.setPadding(0, 0, SizeUtils.dp2px(12f), 0)
        view.setOnClickListener(clickListener)
        mTitleRightContent.addView(view, 0, ViewGroup.LayoutParams(-2, -1))
    }

}