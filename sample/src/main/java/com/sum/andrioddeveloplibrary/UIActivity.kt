package com.sum.andrioddeveloplibrary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.sum.library.ui.image.AppImageUtils
import com.sum.library.ui.image.photoAlbum.AlbumInfo
import com.sum.library.ui.image.photoAlbum.PhotoAlbumActivity
import com.sum.library.ui.image.preview.ImagePreviewActivity
import com.yalantis.ucrop.UCrop
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
            AppImageUtils.systemTakePhoto(this, 2, null)
        }

        btn_4.setOnClickListener {
            PhotoAlbumActivity.open(this, AlbumInfo())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == 1) {

        } else if (requestCode == 2) {

        } else if (requestCode == 10) {
            val extra = data?.getStringArrayListExtra("images")

            val text = StringBuilder()
            extra?.forEach {
                text.append("file:$it\n")
            }
            tv_xc_2.text = text

            if (!TextUtils.isEmpty(text)) {
                /*val uri = Uri.fromFile(File(extra?.get(0)))
                val target = Uri.parse("file://" + AppFileConfig.getImageDirectoryFile())
                UCrop.of(uri, target)
                        .withAspectRatio(16f, 9f)
                        .start(this)*/
//                AppImageUtils.appImageCrop(this, extra!![0], 11, 0f, null)
                AppImageUtils.appImageCrop(this, extra!![0], 11)
            }
        } else if (requestCode == 11) {
            val output = UCrop.getOutput(data!!)
            tv_xc_2.append("\n")
            tv_xc_2.append(output?.path)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit)
    }
}
