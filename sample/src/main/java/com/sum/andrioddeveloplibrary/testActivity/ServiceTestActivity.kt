package com.sum.andrioddeveloplibrary.testActivity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import com.sum.andrioddeveloplibrary.R
import com.sum.andrioddeveloplibrary.service.*
import com.sum.andrioddeveloplibrary.service.aidl.IServiceWorker
import com.sum.library.app.BaseActivity
import com.sum.library.utils.Logger
import kotlinx.android.synthetic.main.activity_test_service.*

//Service功能测试
class ServiceTestActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_test_service

    var mBooleanMsg = false
    var mBooleanAidl = false

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
            Thread {
                Logger.e("child thread start")
                val intent = Intent(this, TestService::class.java)
                startService(intent)
            }.start()
        }


        btn_5.setOnClickListener {
            val intent = Intent(this@ServiceTestActivity, RemoteService::class.java)
            bindService(intent, mConntion, Context.BIND_AUTO_CREATE)
            mBooleanMsg = true
        }

        //需要跨进程通信
        btn_7.setOnClickListener {
            mMsg?.send(Message.obtain())
        }


        btn_8.setOnClickListener {
            val intent = Intent(this@ServiceTestActivity, AidlService::class.java)
            bindService(intent, mWorkerConnection, Context.BIND_AUTO_CREATE)
            mBooleanAidl
        }

        btn_9.setOnClickListener {
            mWorker?.index?.let { it1 -> btn_9.text = "${it1}<-Helloworld" }
        }

        btn_10.setOnClickListener {
            mWorker?.addSize(0)
        }
    }


    private var mWorker: IServiceWorker? = null

    //向远处服务发送数据
    private var mMsg: Messenger? = null


    private val mConntion = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mMsg = Messenger(service)
        }
    }


    private val mWorkerConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mWorker = IServiceWorker.Stub.asInterface(service)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBooleanMsg) {
            unbindService(mConntion)
        }
        if (mBooleanAidl) {
            unbindService(mWorkerConnection)
        }
    }


}
