package com.sum.andrioddeveloplibrary.net

import com.blankj.utilcode.util.FileIOUtils
import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library.ui.web.WebActivity
import kotlinx.android.synthetic.main.activity_net.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class NetActivity : BaseAppActivity() {

    override fun getLayoutId(): Int = R.layout.activity_net

    override fun initParams() {
        request_net_post.setOnClickListener {
            net_post()
        }

        request_net_get.setOnClickListener {
            net_get()
        }

        request_net_upload.setOnClickListener {
            netUpload()
        }

        xutils_net_webview.setOnClickListener {
            val url = "http://m.aizuna.com/index.php?m=Home&c=AznSpring&referer_id=12"
            WebActivity.open(this@NetActivity, url)
        }

        bt_dialog1.setOnClickListener { mPresent.loadingView.showLoading() }
        bt_dialog2.setOnClickListener { mPresent.loadingView.showLoading("Loading...") }
        bt_dialog3.setOnClickListener { mPresent.loadingView.showProgressLoading("Progress Loading...", true) }
    }

    private fun net_post() {
        mPresent.loadingView.showLoading("加载中...")

//        mRetrofit.create(Api::class.java)
//                .getExampleValue("getProRecommend")
//                .enqueue(object : Callback(){})
    }

    private fun net_get() {
        mPresent.loadingView.showLoading("加载中...")
    }


    private fun netUpload() {
        val path = ""

        //字符串
        val name = RequestBody.create(MediaType.parse("text/plain"), "name_test")

        //文件流
        val stream = FileIOUtils.readFile2BytesByStream(path)
        val file = RequestBody.create(MediaType.parse("application/octet-stream"), stream)
        //文件流说明部分
        val part = MultipartBody.Part.createFormData("file", "self.mov", file)

        mRetrofit.create(Api::class.java).testFileUpload1(name, part)


    }

}
