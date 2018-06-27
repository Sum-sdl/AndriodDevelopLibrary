package com.sum.andrioddeveloplibrary

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.library.ui.image.AppImageUtils
import com.sum.library.ui.web.WebActivity
import kotlinx.android.synthetic.main.activity_ui.*
import java.io.File


class UIActivity : BaseAppActivity() {

    override fun initParams() {

        btn_1.setOnClickListener {

            val list = arrayListOf<String>()
            list.addAll(mData)
            list.addAll(getAll())
            list.addAll(getAll())
            list.addAll(getAll())
            list.addAll(getAll())
            list.addAll(getAll())
            list.addAll(getAll())
            list.addAll(getAll())
            list.addAll(getAll())
            list.addAll(getAll())
            AppImageUtils.appImagePreview(this, list)
        }

        //系统相册
        btn_2.setOnClickListener {
            AppImageUtils.systemChooseImage(this, 1)
        }

        //系统拍照
        btn_3.setOnClickListener {
            mPhoto = AppImageUtils.systemTakePhoto(this, 2)
        }

        //自定义相册
        btn_4.setOnClickListener {
            AppImageUtils.appImageAlbum(this, 10, 6)
        }

        btn_5.setOnClickListener {
            val test = com.sum.andrioddeveloplibrary.testview.BasePushMessage()
            for (i in 1..100) {
                test.addOneMessage(i)
            }
        }

        btn_6.setOnClickListener {
            WebActivity.open(this, "", "https://aznapi.house365.com/Home/Information/lists", null, null)
        }
    }

    private fun getAll(): ArrayList<String> {
        val list = arrayListOf<String>()
        list.add("http://img31.house365.com/M02/01/72/rBEBYFTTb52AKGnpAAGRhUbP6bI584.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5qAGnRYAAda-f68kug942.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5iAetrnAAGSIF0wiv0935.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5iAUIriAAJYXBzYogs778.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAJDA5AAMhtOOBMJA321.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAZ7LcAAM8wtR8X-E576.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAcaZdAAMWXiLK-Nk224.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mADsEOAALPYONdMdk748.jpg")
        return list
    }

    override fun getLayoutId(): Int = R.layout.activity_ui

    private val mData: ArrayList<String> = arrayListOf()

    private var mPhoto: File? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        mPhoto = savedInstanceState?.getSerializable("file") as File?
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("file", mPhoto)

    }

    private fun updateImageShow(file: String?) {
        if (file != null) {
            iv_2.post {
                iv_2.setImageURI(Uri.fromFile(File(file)), null)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == 1) {
            val uri = AppImageUtils.systemChooseImageIntentImagePath(data)
            if (!TextUtils.isEmpty(uri)) {
                AppImageUtils.appImageCrop(this, uri, 11)
            }

            updateImageShow(uri)

        } else if (requestCode == 2) {
            updateImageShow(mPhoto?.path)
        } else if (requestCode == 10) {
            val extra = data?.getStringArrayListExtra("images")
            mData.addAll(extra!!)

            val text = StringBuilder()
            extra.forEach {
                text.append("$it\n")
            }
            tv_xc_2.text = text

        } else if (requestCode == 11) {
            tv_xc_2.append("\n")
            tv_xc_2.append("剪裁图片地址：")
            tv_xc_2.append(AppImageUtils.appImageCropIntentPath(data))
            updateImageShow(AppImageUtils.appImageCropIntentPath(data))
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit)
    }
}
