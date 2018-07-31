package com.sum.andrioddeveloplibrary.net

import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library.net.callback.RetCallBack
import com.sum.library.ui.web.WebActivity
import kotlinx.android.synthetic.main.activity_net.*

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
                .enqueue(object : RetCallBack<Any>() {
                    override fun onSuccess(response: Any) {
//                        text.text = response
                    }

                    override fun onFinally() {
                        mPresent.loadingView.hideLoading()
                    }
                })
    }

}
