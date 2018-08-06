package com.sum.andrioddeveloplibrary.net

import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library.ui.web.WebActivity
import kotlinx.android.synthetic.main.activity_net.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetActivity : BaseAppActivity() {
    override fun getLayoutId(): Int = R.layout.activity_net

    override fun initParams() {
        request_net.setOnClickListener {
            net()
        }

        xutils_net_webview.setOnClickListener {
            val url = ""
            WebActivity.open(this@NetActivity, url)
        }

        bt_dialog1.setOnClickListener { mPresent.loadingView.showLoading() }
        bt_dialog2.setOnClickListener { mPresent.loadingView.showLoading("Loading...") }
        bt_dialog3.setOnClickListener { mPresent.loadingView.showProgressLoading("Progress Loading...", true) }
    }

    private fun net() {
        mPresent.loadingView.showLoading("加载中...")
        mRetrofit.create(Api::class.java)
                .getExampleValue("getProRecommend")
                .enqueue(object : Callback<Any>{
                    override fun onFailure(call: Call<Any>?, t: Throwable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

}
