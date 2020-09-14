package com.sum.library_ui.image.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager
import com.sum.library.app.BaseActivity
import com.sum.library.utils.LiveDataEventBus
import com.sum.library.utils.simple.SimpleViewPagerFragmentAdapter
import com.sum.library.view.widget.PubTitleView
import com.sum.library_ui.R
import com.sum.library_ui.utils.DragCloseHelper
import com.sum.library_ui.utils.LibUtils
import kotlinx.android.synthetic.main.ui_activity_image_preview.*


class ImagePreviewActivity : BaseActivity() {

    companion object {
        fun open(context: Context, list: ArrayList<String>, index: Int, shareView: View?) {
            if (list.isNotEmpty()) {
                var share: ActivityOptionsCompat? = null
                if (context is Activity && shareView != null) {
                    share = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context,
                        shareView,
                        "share_photo"
                    )
                }
                val intent = Intent(context, ImagePreviewActivity::class.java)
                intent.putExtra("urls", list)
                intent.putExtra("index", index)
                context.startActivity(intent, share?.toBundle())
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.ui_activity_image_preview


    private lateinit var mTitle: PubTitleView

    private lateinit var mDragCloseHelper: DragCloseHelper

    //当前页面
    private lateinit var mCurrentView: ImagePreviewFragment

    private var scrolling = false

    override fun initParams() {
        mTitle = findViewById(R.id.pub_title_view)
        val list = intent.getStringArrayListExtra("urls")
        val size = list?.size ?: 0
        val fragments = arrayListOf<ImagePreviewFragment>()
        list?.forEach {
            fragments.add(ImagePreviewFragment.instance(it, true))
        }
        LibUtils.transparentStatusBar(this)
        //页面集合
        val adapter = SimpleViewPagerFragmentAdapter(supportFragmentManager, fragments, null)
        image_view_pager.adapter = adapter
        image_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mTitle.setTitle("${position + 1}/$size")
                mCurrentView = fragments[position]
                LiveDataEventBus.with("preview_page_selected", Int::class.java).value = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                scrolling = state != 0
            }
        })

        if (size > 1) {
            image_indicator.setViewPager(image_view_pager)
            mTitle.setTitle("1/$size")
        }
        val index = intent.getIntExtra("index", 0)
        image_view_pager.setCurrentItem(index, false)
        mCurrentView = fragments[index]

        //处理view
        mDragCloseHelper = DragCloseHelper(this)
        mDragCloseHelper.setShareElementMode(true)
        mDragCloseHelper.setMaxExitY(200)
        mDragCloseHelper.setMinScale(0.5f)
        mDragCloseHelper.setDragCloseViews(rl_content, image_view_pager)
        mDragCloseHelper.setDragCloseListener(object : DragCloseHelper.DragCloseListener {
            override fun intercept(): Boolean {
                val scale = mCurrentView.getScale()
                //滚动中，发大缩小中，拦截缩放
                return scrolling || (scale < 0.99 || scale > 1.01);
            }

            override fun dragStart() {
                pub_title_view.visibility = View.INVISIBLE
                image_indicator.visibility = View.INVISIBLE
            }

            override fun dragging(percent: Float) {

            }

            override fun dragCancel() {
                image_indicator.visibility = View.VISIBLE
                pub_title_view.visibility = View.VISIBLE
            }

            override fun dragClose(isShareElementMode: Boolean) {
                //拖拽关闭，如果是共享元素的页面，需要执行activity的onBackPressed方法，注意如果使用finish方法，则返回的时候没有共享元素的返回动画
                if (isShareElementMode) {
                    onBackPressed()
                }
            }
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return if (mDragCloseHelper.handleEvent(event)) {
            true
        } else {
            super.dispatchTouchEvent(event)
        }
    }
}
