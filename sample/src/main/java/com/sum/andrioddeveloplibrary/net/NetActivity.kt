package com.sum.andrioddeveloplibrary.net

import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseActivity
import com.sum.library.net.Retrofit2Helper
import kotlinx.android.synthetic.main.activity_net.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_net

    override fun initParams() {
        request_net.setOnClickListener {
            net()
        }
    }

    private fun net() {
        showLoading("加载中...")
        Retrofit2Helper.getRetrofit()
                .create(Api::class.java)
                .getExampleValue("getProRecommend")
                .enqueue(object : Callback<Respone> {
                    override fun onFailure(call: Call<Respone>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<Respone>?, response: Response<Respone>?) {
                        val body = response?.body()
                        text.text = body?.toString()
//                        hideLoading()
                    }
                })

    }
}
