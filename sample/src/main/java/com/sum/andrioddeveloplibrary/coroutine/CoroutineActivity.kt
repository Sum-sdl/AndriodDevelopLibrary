package com.sum.andrioddeveloplibrary.coroutine

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseActivity
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main

/**
 * Created by sdl on 2018/12/21.
 */
class CoroutineActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_coroutine

    override fun initParams() {

        b1.setOnClickListener {
            test()
        }
        b2.setOnClickListener {
            test2()
        }
        b3.setOnClickListener {
            test3()
        }
    }

    private fun test3() {
        GlobalScope.launch(Dispatchers.Main) {
            tv.text = "test3 测试开始"
            val res = async(Dispatchers.Default) {
                LogUtils.e("sleep -> start")
                delay(2000)
                LogUtils.e("sleep -> finish")
                return@async "GlobalScope.launch(Dispatchers.Main)"
            }.await()
            tv.text = res
        }
    }

    private fun test() {
        tv.text = "测试开始 main"
        val uiScope = CoroutineScope(Dispatchers.Main)//设置协程执行的线程
        uiScope.launch {
            val deffer = async(Dispatchers.Main) {
                LogUtils.e("thread sleep 4s-> start")
                delay(4000)
                LogUtils.e("sleep 4s-> finish")
                return@async "延迟了4秒"
            }
            val coroutineResult = deffer.await()
            tv.text = coroutineResult
        }
    }

    private fun test2() {
        tv.text = "测试开始 IO"
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            val deffer = async(Dispatchers.IO) {
                LogUtils.e("thread sleep 2-> start")
                delay(2000)
                LogUtils.e("sleep 2-> finish")
                return@async "延迟了2秒"
            }
//            error 子线程操作view 非Dispatchers.Main线程的设置都会挂
            tv.text = deffer.await()

            tv.text = async(Dispatchers.Main) {
                LogUtils.e("thread sleep 3s-> start")
                delay(3000)
                LogUtils.e("sleep 3s-> finish")
                return@async "延迟了3秒"
            }.await()

            tv.text = async(Dispatchers.Default) {
                LogUtils.e("thread sleep 4s-> start")
                delay(4000)
                LogUtils.e("sleep 4s-> finish")
                return@async "延迟了4秒 全部执行结束"
            }.await()
            ToastUtils.showLong(tv.text.toString())
        }
    }
}