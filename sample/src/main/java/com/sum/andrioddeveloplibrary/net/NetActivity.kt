package com.sum.andrioddeveloplibrary.net

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library_ui.image.AppImageUtils
import com.sum.library_ui.image.photoAlbum.AlbumInfo
import com.sum.library_ui.web.WebActivity
import kotlinx.android.synthetic.main.activity_net.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetActivity : BaseAppActivity() {

    override fun getLayoutId(): Int = R.layout.activity_net

    override fun initParams() {
        request_net_post.setOnClickListener {
            net_post()
        }
        request_net_post2.setOnClickListener {
            net_post2()
        }

        request_net_get.setOnClickListener {
            net_get()
        }

        request_net_upload.setOnClickListener {
            netUpload()
        }
        request_net_choose.setOnClickListener {
            AppImageUtils.appImageAlbum(this@NetActivity, 1)
        }

        xutils_net_webview.setOnClickListener {
            val url = "http://m.aizuna.com/index.php?m=Home&c=AznSpring&referer_id=12"
            WebActivity.startCommonUrl(this@NetActivity, url, "")
        }

        bt_dialog1.setOnClickListener { mUiActive.loadingView.showLoading() }
        bt_dialog2.setOnClickListener { mUiActive.loadingView.showLoading("Loading...") }
        bt_dialog3.setOnClickListener {
            mUiActive.loadingView.showProgressLoading(
                "Progress Loading...",
                true
            )
        }
    }

    private fun net_post() {
        mUiActive.loadingView.showLoading("加载中-Post...")
        val con =
            "{\"head\":{\"token\":\"4d40d6b05d67086ef1ac0c4c093155261b4db650e455e7a2\",\"client\":\"HongKongFocus\",\"version\":\"V1.2\"},\"message\":{\"userId\":\"ffffffff-8650-ca15-ffff-ffffd8967aa8\",\"userName\":\"我是一个农民\",\"userTag\":\"\",\"headUrl\":\"http:\\/\\/thirdqq.qlogo.cn\\/qqapp\\/1106296314\\/0182677DB9A9A4B450F70443EF9D9F66\\/100\",\"newType\":\"2\",\"newFollow\":false,\"pageNo\":1,\"pageSize\":10}}"
        val body = RequestBody.create(MediaType.parse("application/json"), con)

        mRetrofit.create(Api::class.java)
            .apiPost_Search(body)
            .enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>?, t: Throwable?) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(t.toString())
                }

                override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(response?.body().toString())
                }
            })
    }

    private fun net_post2() {
        //框架直接讲ReqBody对象转换成JSON，作为RequestBody传递
        mUiActive.loadingView.showLoading("加载中-Post2...")
        mRetrofit.create(Api::class.java)
            .apiPost_Search(ReqBody())
            .enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>?, t: Throwable?) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(t.toString())
                }

                override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(response?.body().toString())
                }
            })
    }


    private fun net_get() {
        mUiActive.loadingView.showLoading("加载中-Get...")
        val map = HashMap<String, String>().apply {
            put("key3","value3")
        }
        map.put("key1","value1")
        map.put("key2","value2")
        mRetrofit.create(Api::class.java)
            .apiGet(map)
            .enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>?, t: Throwable?) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(t.toString())
                }

                override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(response?.body().toString())
                }
            })
    }


    private fun netUpload() {
        val path = tv_file.text.toString().trim()
        if (TextUtils.isEmpty(path)) {
            ToastUtils.showLong("请选择图片")
            return
        }
        mUiActive.loadingView.showLoading("上传-Upload...")

        //字符串
        val name = RequestBody.create(MediaType.parse("text/plain"), "name_test")

        //文件流
        val stream = FileIOUtils.readFile2BytesByStream(path)
        val file = RequestBody.create(MediaType.parse("application/octet-stream"), stream)
        //文件流说明部分
        val part = MultipartBody.Part.createFormData("file", "self.jpg", file)

        mRetrofit.create(Api::class.java).testFileUpload1(name, part)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(t.toString())
                }

                override fun onResponse(
                    call: Call<ResponseBody>?,
                    response: Response<ResponseBody>?
                ) {
                    mUiActive.loadingView.hideLoading()
                    ToastUtils.showLong(response?.body().toString())
                }
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == AlbumInfo.Request_choose_photo) {
            val extra = data?.getStringArrayListExtra("images")
            tv_file.text = extra!![0]
        }
    }

    private fun test() {
        //设置请求缓存方案
        var req = Request.Builder().cacheControl(CacheControl.FORCE_CACHE)
    }

    private class ReqBody {
        var head = HeadBean()
        var message = MessageBean()

        class HeadBean {
            /**
             * token : 4d40d6b05d67086ef1ac0c4c093155261b4db650e455e7a2
             * client : HongKongFocus
             * version : V1.2
             */

            var token = "4d40d6b05d67086ef1ac0c4c093155261b4db650e455e7a2"
            var client = "HongKongFocus"
            var version = " V1.2"
        }

        class MessageBean {
            var userId = "ffffffff-8650-ca15-ffff-ffffd8967aa8"
            var userName = "我是一个农民"
            var userTag = ""
            var headUrl =
                " http://thirdqq.qlogo.cn/qqapp/1106296314/0182677DB9A9A4B450F70443EF9D9F66/100"
            var pageNo = "1"
            var newType = "2"
            var newFollow = "false"
        }
    }

}
