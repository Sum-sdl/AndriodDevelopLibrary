package com.sum.andrioddeveloplibrary

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.library.view.sheet.BottomSheetView
import com.sum.library.view.sheet.DialogChooseView
import kotlinx.android.synthetic.main.activity_dialog_test.*

class DialogTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_test)

        bt1.setOnClickListener { ItemListDialogFragment.newInstance(3).show(supportFragmentManager, "3") }
        bt2.setOnClickListener { BottomSheetView().show(supportFragmentManager, "tag") }
        bt3.setOnClickListener {
            DialogChooseView().setMessage("message")
                    .setPos("下一步").setCancel(false)
                    .setPosListener { ToastUtils.showLong("Next") }
                    .show(supportFragmentManager, "show")

        }
    }
}
