package com.sum.andrioddeveloplibrary

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.activity.FragmentManagerActivity
import com.sum.library.utils.Logger
import com.sum.library.view.widget.PubTitleView
import com.sum.library_ui.web.WebActivity
import kotlinx.android.synthetic.main.activity_widget_use.*
import q.rorbin.badgeview.QBadgeView

class LibWidgetUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.RED)
        setContentView(R.layout.activity_widget_use)
        ActivityCompat.setExitSharedElementCallback(this, exitElementCallback)
        Logger.e("LibWidgetUseActivity onCreate")

        val view: PubTitleView = pub_view

        view.addRightTextButton("Github", View.OnClickListener {
            WebActivity.startCommonUrl(this, "https://github.com/Sum-sdl", "")
        })


        title_2.addRightTextButton("按钮", View.OnClickListener {
            WebActivity.startCommonUrl(this, "https://github.com/Sum-sdl", "")
        })


        btn_1.setOnClickListener {
            startActivity(Intent(this, FragmentManagerActivity::class.java))
        }

        iv_2.setOnClickListener {
            //            anim()
            if (pub_empty_view.visibility != View.VISIBLE) {
                pub_empty_view.visibility = View.VISIBLE
            } else {
                pub_empty_view.visibility = View.GONE
            }
        }

        pub_empty_view.setEmptyViewClickListener { ToastUtils.showLong("EmptyView Click") }

        val sp = SpanUtils().append("Hi ! Hello\n").append("World").setForegroundColor(Color.RED)
        pub_empty_view.setEmptyText(sp.create())

        val tv = TextView(this)
        tv.text = "add View"
        tv.setPadding(0, 20, 0, 20)
        pub_empty_view.addEmptyView(tv)

        iv_1.setOnClickListener {

            val activity = this@LibWidgetUseActivity
            val intent = Intent(this, LibUIActivity::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, iv_1, "IMG_TRANSITION"
                    )
                ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
            } else {
                activity.startActivity(intent)
            }
        }

        //num
        val mBadgeWork = QBadgeView(this).setBadgeTextSize(7f, true)
            .setBadgeGravity(Gravity.TOP or Gravity.END)
            .setBadgePadding(2f, true)
            .setOnDragStateChangedListener { _, _, _ -> }
            .bindTarget(mr_btn.ivParent)

        mBadgeWork.badgeNumber = 99


    }

    private var reenterState: Bundle? = null

    private val exitElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(
            names: MutableList<String>,
            sharedElements: MutableMap<String, View>
        ) {
            if (reenterState != null) {
                val trant = reenterState!!.getString("tag")

                names.clear()
                names.add(trant)

                sharedElements.clear()
                sharedElements.put(trant, iv_2)
                reenterState = null
            }
        }
    }


    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        reenterState = Bundle(data?.extras)
//        ActivityCompat.postponeEnterTransition(this)
//        ActivityCompat.startPostponedEnterTransition(this@LibWidgetUseActivity)
        /* iv_2.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
             override fun onPreDraw(): Boolean {
                 iv_2.viewTreeObserver.removeOnPreDrawListener(this)
                 ActivityCompat.startPostponedEnterTransition(this@LibWidgetUseActivity)
                 return true
             }
         })*/
    }


    private fun anim() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(
                this, iv_2, "iv_2"
            )
        } else {
            null
        }
        val intent = Intent(this, LibUIActivity::class.java)
        intent.putExtra("url", 1)
        startActivity(intent, options?.toBundle())
    }
}
