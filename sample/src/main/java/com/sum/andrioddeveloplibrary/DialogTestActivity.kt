package com.sum.andrioddeveloplibrary

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.library.app.BaseBottomSheetFragment
import com.sum.library.app.BaseDialogFragment
import com.sum.library.utils.Logger
import com.sum.library.utils.TaskExecutor
import com.sum.library.view.sheet.DialogChooseView
import com.sum.library.view.sheet.DialogTimeChooseView
import kotlinx.android.synthetic.main.activity_dialog_test.*

class DialogTestActivity : AppCompatActivity(), ItemListDialogFragment.Listener {
    override fun onItemClicked(position: Int) {
        ToastUtils.showShort("pos=$position")
        if (position == 1) {
            startActivity(
                Intent(
                    this,
                    LibUIActivity::class.java
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_test)

        bt1.setOnClickListener {
            ItemListDialogFragment.newInstance(20)
                .show(supportFragmentManager, "3")
        }
        bt2.setOnClickListener {
            var time = ""
            if (it.tag is String) {
                time = it.tag as String
            }
            DialogTimeChooseView.Builder()
                .setShowHours()
                .setCurrentTime(time)
                .setTitle("选择时间")
                .setListener { _, content ->
                    it.tag = content
                    ToastUtils.showLong("time = $content")
                }.showFast(this)

        }

        bt5.setOnClickListener {
            var time = "2018-08-18"
            if (it.tag is String) {
                time = it.tag as String
            }
            DialogTimeChooseView.Builder()
                .setCurrentTime(time)
                .setListener { _, content ->
                    it.tag = content
                    ToastUtils.showLong("date = $content")
                }.showFast(this)

        }

        bt51.setOnClickListener {
            var time = ""
            if (it.tag is String) {
                time = it.tag as String
            }
            DialogTimeChooseView.Builder()
                .setCurrentTime(time)
                .setShowTimeAndHours()
                .setTitle(time)
                .setClickDismiss(false)
                .setListener { _, content ->
                    it.tag = content
                    ToastUtils.showLong("date = $content")
                }.showFast(this)

        }
        bt52.setOnClickListener {
            var time = ""
            if (it.tag is String) {
                time = it.tag as String
            }
            DialogTimeChooseView.Builder()
                .setCurrentTime(time)
                .setShowMonth()
                .setTitle(time)
                .setClickDismiss(true)
                .setListener { _, content ->
                    it.tag = content
                    ToastUtils.showLong("date = $content")
                }.showFast(this)

        }

        bt31.setOnClickListener { _ ->
            DialogChooseView().setMessage("message").setTitle("Test Title")
                .setPosListener { ToastUtils.showLong("pos") }
                .showFast(this)
        }
        bt3.setOnClickListener { _ ->
            DialogChooseView().setMessage("message").setTitle("Test Title")
                .setPos("立即更新").setCancel(false).setNeedHideButtonWhenEmpty(true)
                .setPosListener { ToastUtils.showLong("Next") }
                .setNegListener {
                    ToastUtils.showLong("update")
                    it.dismiss()
                }
                .showFast(this)
        }

        bt73.setOnClickListener { _ ->
            DialogChooseView().setMessage("message")
                .setPos("立即更新2").setNeg("取消").setCancel(false).setNeedHideButtonWhenEmpty(true)
                .setPosListener {
                    ToastUtils.showLong("升级")
                }
                .setNegListener { ToastUtils.showLong("Cancel") }
                .show(supportFragmentManager, "show")
        }
        bt74.setOnClickListener {
            TaskExecutor.mainThread({ showTipDelay() }, 1000)
        }

        bt4.setOnClickListener {
            var index = 0
            if (it.tag is Int) {
                index = it.tag as Int
            }
            val data = arrayOf(
                "单选1",
                "单选2",
                "单选3",
                "单选4",
                "单选5",
                "单选6",
                "单选7",
                "单选8",
                "单选9",
                "单选10",
                "单选11",
                "单选12"
            )
            DialogTimeChooseView.Builder().setCustomItems(data).setChooseIndex(index)
                .setListener { pos, content ->
                    it.tag = pos
                    ToastUtils.showLong("pos=$pos,content$content")
                }.showFast(this)
        }

        bt6.setOnClickListener {
            //            DialogF().show(supportFragmentManager, "test")
            val d = DialogF()
//            d.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_no_bg)

            d.showFast(this)
        }

        bt61.setOnClickListener {
            val navBarVisible = BarUtils.isNavBarVisible(this)
            val su = BarUtils.isSupportNavBar()
            Logger.e("" + navBarVisible + "," + su)
        }

        bt62.setOnClickListener {
            val d = BottomDialog()
//            d.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_no_bg)
            d.showFast(this)
        }
    }

    private fun showTipDelay() {
        DialogChooseView().setMessage("message")
            .setPos("立即更新3").setNeg("neg").setNeedHideButtonWhenEmpty(true)
            .setPosListener {
                it.dismiss()
                ToastUtils.showLong("升级")
            }
            .setNegListener { ToastUtils.showLong("Cancel") }
            .showFast(this)
        Logger.e("show11112222")
    }

    class BottomDialog : BaseBottomSheetFragment() {
        override fun getLayoutId(): Int = R.layout.bsv_bottom_in_view2

        override fun initParams(view: View?) {
            //不支持滚动
            isCancelable = false
        }

        override fun getDialogNoBg(): Boolean {
            return true
        }

        override fun getDialogBgIsTransparent(): Boolean = true
    }


    class DialogF : BaseDialogFragment() {
        override fun getLayoutId(): Int = R.layout.bsv_bottom_in_view

        //动画参考：R.style.dialog_anim_bottom
        override fun getDialogShowAnimation(): Int {
            return 0
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setNeedBottomAnim()
        }

        override fun getDialogNoBg(): Boolean =false

        override fun initParams(view: View) {
            view.findViewById<View>(R.id.fl_content).setOnClickListener { dismiss() }
            view.findViewById<View>(R.id.btn_cancel).setOnClickListener {
                startActivity(
                    Intent(
                        view.context,
                        LibUIActivity::class.java
                    )
                )
            }

        }
    }
}
