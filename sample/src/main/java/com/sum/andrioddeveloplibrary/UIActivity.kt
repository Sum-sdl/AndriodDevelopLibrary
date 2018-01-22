package com.sum.andrioddeveloplibrary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.sum.library.ui.image.AppImageUtils
import com.sum.library.ui.image.preview.ImagePreviewActivity
import kotlinx.android.synthetic.main.activity_ui.*
import java.io.File


class UIActivity : AppCompatActivity() {

    private val mData: ArrayList<String> = arrayListOf()

    private var mPhoto: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)


        btn_1.setOnClickListener {
            val list = arrayListOf<String>()
            list.addAll(mData)
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
            mPhoto = AppImageUtils.systemTakePhoto(this, 2)
        }

        btn_4.setOnClickListener {
            AppImageUtils.appImageAlbum(this, 10, 2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == 1) {
            val uri = AppImageUtils.systemChooseImageIntentImagePath(data)
            if (uri != null) {
                tv_xc.append(uri)
            }
        } else if (requestCode == 2) {

        } else if (requestCode == 10) {
            val extra = data?.getStringArrayListExtra("images")
            mData.addAll(extra!!)

            val text = StringBuilder()
            extra.forEach {
                text.append("$it\n")
            }
            tv_xc_2.text = text

            if (!TextUtils.isEmpty(text)) {
                /*val uri = Uri.fromFile(File(extra?.get(0)))
                val target = Uri.parse("file://" + AppFileConfig.getImageDirectoryFile())
                UCrop.of(uri, target)
                        .withAspectRatio(16f, 9f)
                        .start(this)*/
//                AppImageUtils.appImageCrop(this, extra!![0], 11, 0f, null)
                AppImageUtils.appImageCrop(this, extra[0], 11)
            }
        } else if (requestCode == 11) {
            tv_xc_2.append("\n")
            tv_xc_2.append(AppImageUtils.appImageCropIntentPath(data))
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit)
    }
}
