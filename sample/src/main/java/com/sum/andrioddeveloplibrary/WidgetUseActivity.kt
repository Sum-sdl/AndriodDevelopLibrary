package com.sum.andrioddeveloplibrary

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.blankj.utilcode.util.ToastUtils
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
         view.addRightTextButton("分享", View.OnClickListener {
             ToastUtils.showLong("分享")
         })

        view.addRightImageButton(R.mipmap.ic_edit_delete, null)
        view.addRightTextButton("网页连接", View.OnClickListener {
            WebActivity.open(this, "网页", "https://github.com/ongakuer/PhotoDraweeView/tree/master/sample/src/main/java/me/relex/photodraweeview/sample", null, null)
        })



        btn_1.setOnClickListener {
            startActivity(Intent(this, FragmentManagerActivity::class.java))
        }

        iv_2.setOnClickListener {
            anim()
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
