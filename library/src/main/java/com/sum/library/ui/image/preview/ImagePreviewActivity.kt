package com.sum.library.ui.image.preview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.view.ViewPager
import com.sum.library.R
import com.sum.library.adapter.ViewPagerFragmentAdapter
import com.sum.library.app.BaseActivity
import com.sum.library.view.widget.PubTitleView
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreviewActivity : BaseActivity() {

    companion object {
        fun open(context: Context, list: ArrayList<String>) {
            if (list.isNotEmpty()) {
                val intent = Intent(context, ImagePreviewActivity::class.java)
                intent.putExtra("urls", list)
                context.startActivity(intent)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_image_preview

    override fun statusBarColor(): Int = Color.BLACK

    private lateinit var mTitle: PubTitleView

    override fun initParams() {
        mTitle = findViewById(R.id.pub_title_view)
        mTitle.getTitleBgView()?.setBackgroundColor(0x50000000)
        val list = intent.getStringArrayListExtra("urls")
        val size = list.size
        val fragments = arrayListOf<ImagePreviewFragment>()
        list.forEach {
            fragments.add(ImagePreviewFragment.instance(it, true))
        }

        image_view_pager.adapter = ViewPagerFragmentAdapter(supportFragmentManager, fragments, null)
        image_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mTitle.setTitle("${position + 1}/$size")
            }
        })
        image_indicator.setViewPager(image_view_pager)

        if (size > 1) {
            mTitle.setTitle("1/$size")
        }
    }


}
