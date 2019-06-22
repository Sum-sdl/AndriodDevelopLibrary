package com.sum.andrioddeveloplibrary.view_delegate.mvp

import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.library.domain.mvp.AppViewDelegate

/**
 * Created by sdl on 2019-06-22.
 */

class UiKonlinDelegate : AppViewDelegate() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initParams(view: View?) {
        view?.findViewById<View>(R.id.b1)?.setOnClickListener {
            ToastUtils.showLong("Tip 1")
        }
        //无法使用直接的布局id
//        b20.setOnClickListener { }

    }
}