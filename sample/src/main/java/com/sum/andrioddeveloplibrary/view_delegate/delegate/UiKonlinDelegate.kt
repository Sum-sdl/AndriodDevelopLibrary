package com.sum.andrioddeveloplibrary.view_delegate.delegate

import android.support.v4.view.ViewPager
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.andrioddeveloplibrary.view_delegate.ViewDelegateFragment
import com.sum.library.app.delegate.BaseAppUiDelegate
import com.sum.library.utils.simple.SimpleViewPagerFragmentAdapter
import kotlinx.android.synthetic.main.activity_coordinator.view.*

/**
 * Created by sdl on 2019-06-22.
 */

class UiKonlinDelegate : BaseAppUiDelegate() {

    override fun getLayoutId(): Int = R.layout.activity_coordinator

    override fun needPrintLifeLog(): Boolean = true

    override fun initParams(view: View) {
        view.findViewById<View>(R.id.b1)?.setOnClickListener {
            ToastUtils.showLong("Tip 1")
        }
        //无法使用直接的布局id
//        b20.setOnClickListener { }

        val viewPager: ViewPager = view.view_pager

        val list = arrayListOf(ViewDelegateFragment(), ViewDelegateFragment(), ViewDelegateFragment())
        val listTitle = arrayListOf("0", "1", "2")
        viewPager.adapter = SimpleViewPagerFragmentAdapter(activity.supportFragmentManager, list, listTitle as MutableList<String>?)

        view.toolbar_tab.setupWithViewPager(viewPager)

    }
}