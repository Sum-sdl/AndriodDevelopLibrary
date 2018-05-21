package com.sum.andrioddeveloplibrary

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sum.andrioddeveloplibrary.activity.FragmentManagerActivity
import com.sum.library.ui.web.WebActivity
import com.sum.library.utils.Logger
import com.sum.library.view.widget.PubTitleView
import kotlinx.android.synthetic.main.activity_widget_use.*

class WidgetUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_use)
        Logger.e("WidgetUseActivity onCreate")

        val view: PubTitleView = pub_view

        view.addRightTextButton("My Github", View.OnClickListener {
            WebActivity.open(this, "https://github.com/Sum-sdl")
        })

        pub_view2.addRightTextButton("Web", View.OnClickListener {
            WebActivity.open(this, "http://m.018929.com/index.php/phone/userReg")
        })

        pub_view2.addRightTextButton("活动页", View.OnClickListener {
            WebActivity.open(this, "http://m.aizuna.com/index.php?m=Home&c=AznSpring&referer_id=12")
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


    }

    private fun anim() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(
                    this, iv_2, "iv_2")
        } else {
            null
        }
        val intent = Intent(this, UIActivity::class.java)
        intent.putExtra("url", 1)
        startActivity(intent, options?.toBundle())
    }
}
