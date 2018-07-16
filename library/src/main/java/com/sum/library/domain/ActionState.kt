package com.sum.library.domain

/**
 * Created by sdl on 2018/7/13.
 */
class ActionState(var state: Int) {

    var msg: String? = null

    var other: Any? = null

    companion object {

        private val sPool = ActionState(0)

        fun obtain(state: Int): ActionState {
            sPool.msg = null
            sPool.other = null
            sPool.state = state
            return sPool
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
    }
}
