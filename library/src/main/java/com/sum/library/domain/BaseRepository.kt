package com.sum.library.domain

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sum.library.net.Retrofit2Helper
import retrofit2.Retrofit

/**
 * Created by sdl on 2018/7/13.
 */
open class BaseRepository {

    //界面操作状态(私有操作,避免直接访问)
    private val mActionLiveData by lazy {
        MutableLiveData<ActionState>()
    }

    protected val mRetrofit: Retrofit by lazy {
        Retrofit2Helper.getRetrofit()
    }

    //发送界面操作状态
    fun sendActionState(state: Int) {
        sendActionState(ActionState.obtain(state))
    }

    fun sendActionState(state: Int, msg: String) {
        val action = ActionState.obtain(state)
        action.msg = msg
        sendActionState(action)
    }

    fun sendActionState(state: ActionState) {
        mActionLiveData.postValue(state)
    }

    //viewModel发送界面操作状态
    fun registerActionState(state: ActionState): LiveData<ActionState> {
        mActionLiveData.postValue(state)
        return mActionLiveData
    }
}