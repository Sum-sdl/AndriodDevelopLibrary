package com.sum.andrioddeveloplibrary.testActivity

import android.content.Intent
import com.sum.andrioddeveloplibrary.R
import com.sum.andrioddeveloplibrary.service.TestIntentService
import com.sum.andrioddeveloplibrary.service.TestJobIntentService
import com.sum.andrioddeveloplibrary.service.TestService
import com.sum.library.app.BaseActivity
import com.sum.library.utils.Logger
import kotlinx.android.synthetic.main.activity_test_service.*

//Service功能测试
class ServiceTestActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_test_service

    override fun initParams() {

        btn_3.setOnClickListener {

            val intent = Intent(this, TestJobIntentService::class.java)

            startService(intent)
        }
        btn_2.setOnClickListener {

            val intent = Intent(this, TestIntentService::class.java)

            startService(intent)
        }
        btn_1.setOnClickListener {

            val intent = Intent(this, TestService::class.java)

            startService(intent)
        }

        btn_4.setOnClickListener {
            Thread({
                Logger.e("child thread start")
                val intent = Intent(this, TestService::class.java)
                startService(intent)
            }).start()
        }

    }


}
