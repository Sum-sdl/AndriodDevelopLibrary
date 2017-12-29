package com.sum.andrioddeveloplibrary

import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.andrioddeveloplibrary.net.NetActivity
import com.sum.library.app.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ItemListDialogFragment.Listener {
    override fun onItemClicked(position: Int) {
        ToastUtils.showShort("click->" + position)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initParams() {
//        StatusBarUtil.darkMode(this)

        BarUtils.setStatusBarAlpha(this, 0)
//        StatusBarUtil.immersive(this, -0xff0100)

        b1.setOnClickListener { startActivity(StickyActivity::class.java) }
        b2.setOnClickListener { startActivity(NetActivity::class.java) }
        b3.setOnClickListener { startActivity(SwipeActivity::class.java) }
        b4.setOnClickListener { startActivity(CustomViewActivity::class.java) }

        b5.setOnClickListener {

            ItemListDialogFragment.newInstance(3).show(supportFragmentManager, "11")

        }

        b6.setOnClickListener { ToastUtils.showLong("b6") }
    }
}
