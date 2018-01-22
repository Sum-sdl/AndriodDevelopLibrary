package com.sum.andrioddeveloplibrary

import android.content.Intent
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
//        view.setTitle("Hello Title Title Title")
        /* view.addRightTextButton("分享", View.OnClickListener {
             ToastUtils.showLong("分享")
         })
 */
        view.addRightImageButton(R.mipmap.ic_edit_delete, null)
        view.addRightTextButton("网页连接", View.OnClickListener {
            WebActivity.open(this, "网页", "https://github.com/ongakuer/PhotoDraweeView/tree/master/sample/src/main/java/me/relex/photodraweeview/sample", null, null)
        })



        btn_1.setOnClickListener {
            startActivity(Intent(this, FragmentManagerActivity::class.java))
        }

    }
}
