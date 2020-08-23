package com.sum.library_ui.image.preview

import android.content.Context
import android.content.Intent
import androidx.viewpager.widget.ViewPager
import com.sum.library.app.BaseActivity
import com.sum.library.utils.simple.SimpleViewPagerFragmentAdapter
import com.sum.library.view.widget.PubTitleView
import com.sum.library_ui.R
import com.sum.library_ui.utils.LibUtils
import kotlinx.android.synthetic.main.ui_activity_image_preview.*

class ImagePreviewActivity : BaseActivity() {

    companion object {
        fun open(context: Context, list: ArrayList<String>, index: Int) {
            if (list.isNotEmpty()) {
                val intent = Intent(context, ImagePreviewActivity::class.java)
                intent.putExtra("urls", list)
                intent.putExtra("index", index)
                context.startActivity(intent)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.ui_activity_image_preview


    private lateinit var mTitle: PubTitleView

    override fun initParams() {
        mTitle = findViewById(R.id.pub_title_view)
        val list = intent.getStringArrayListExtra("urls")
        val size = list?.size ?: 0
        val fragments = arrayListOf<ImagePreviewFragment>()
        list?.forEach {
            fragments.add(ImagePreviewFragment.instance(it, true))
        }
        LibUtils.transparentStatusBar(this)
        val adapter = SimpleViewPagerFragmentAdapter(supportFragmentManager, fragments, null)
        image_view_pager.adapter = adapter
        image_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mTitle.setTitle("${position + 1}/$size")
            }
        })

        if (size > 1) {
            image_indicator.setViewPager(image_view_pager)
            mTitle.setTitle("1/$size")
        }
        val index = intent.getIntExtra("index", 0)
        image_view_pager.setCurrentItem(index, false)

    }
}
