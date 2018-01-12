package com.sum.andrioddeveloplibrary

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sum.library.ui.image.AppImageUtils
import com.sum.library.ui.image.preview.ImagePreviewActivity
import kotlinx.android.synthetic.main.activity_ui.*

class UIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)


        btn_1.setOnClickListener {
            val list = arrayListOf<String>()
            list.add("/storage/emulated/0/fgj/image/左右阳光2018-01-03 14:25:04.png")
            list.add("http://img31.house365.com/M02/01/72/rBEBYFTTb52AKGnpAAGRhUbP6bI584.jpg")
            list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5qAGnRYAAda-f68kug942.jpg")
            list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5iAetrnAAGSIF0wiv0935.jpg")
            list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5iAUIriAAJYXBzYogs778.jpg")
            list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAJDA5AAMhtOOBMJA321.jpg")
            list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAZ7LcAAM8wtR8X-E576.jpg")
            list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAcaZdAAMWXiLK-Nk224.jpg")
            list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mADsEOAALPYONdMdk748.jpg")
            ImagePreviewActivity.open(this, list)
        }

        btn_2.setOnClickListener {
            AppImageUtils.systemChooseImage(this, 1)
        }
        btn_3.setOnClickListener {
            AppImageUtils.systemTakePhoto(this, 1, null)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit)
    }
}
