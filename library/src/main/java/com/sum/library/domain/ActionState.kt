package com.sum.library.domain

import android.support.v4.util.Pools

/**
 * Created by sdl on 2018/7/13.
 */
class ActionState(var state: Int) : Cloneable {

    var msg: String? = null

    var other: Any? = null

    var error: Throwable? = null

    override fun clone(): ActionState {
        return super.clone() as ActionState
    }

    companion object {

        private val pools = Pools.SynchronizedPool<ActionState>(10)

        private val sPool = ActionState(0)

        fun obtain(state: Int): ActionState {
            var acquire = pools.acquire()

            if (acquire == null) {
                acquire = sPool.clone()
            }
            acquire.state = state
            acquire.msg = null
            acquire.error = null

            return acquire
        }

        fun release(state: ActionState) {
            pools.release(state)
        }

        //REGISTER:首次注册界面观察者发送
        internal const val REGISTER = 0

        //Toast提示
        const val TOAST = 1

        //进度条提示
        const val DIALOG_PROGRESS_SHOW = 2
        //加载中
        const val DIALOG_LOADING = 3
        //关闭对话框
        const val DIALOG_HIDE = 4

        //网络请求开始
        const val NET_START = 5
        //网络请求结束
        const val NET_FINISH = 6
        //网络请求异常
        const val NET_ERROR = 7
    }
}
