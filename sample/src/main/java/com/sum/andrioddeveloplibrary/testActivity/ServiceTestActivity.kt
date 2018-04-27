package com.sum.andrioddeveloplibrary.testActivity

import android.content.Intent
import com.sum.andrioddeveloplibrary.R
import com.sum.andrioddeveloplibrary.service.TestIntentService
import com.sum.library.app.BaseActivity
import kotlinx.android.synthetic.main.activity_test_service.*

//Service功能测试
class ServiceTestActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_test_service

    override fun initParams() {

        btn_3.setOnClickListener {

            val intent = Intent(this, TestIntentService::class.java)

            startService(intent)
        }

    }


}
