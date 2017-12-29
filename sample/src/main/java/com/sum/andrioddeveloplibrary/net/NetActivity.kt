package com.sum.andrioddeveloplibrary.net

import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library.net.callback.RetCallBack
import kotlinx.android.synthetic.main.activity_net.*

class NetActivity : BaseAppActivity() {
    override fun getLayoutId(): Int = R.layout.activity_net

    override fun initParams() {
        request_net.setOnClickListener {
            net()
//            addRequest()
        }
    }

    private fun net() {
        showLoading("加载中...")
        mRetrofit.create(Api::class.java)
                .getExampleValue("getProRecommend")
                .enqueue(object : RetCallBack<Respone>() {
                    override fun onSuccess(response: Respone?) {
                        text.text = response?.toString()
                    }
                })

    }
}
