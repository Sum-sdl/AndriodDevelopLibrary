package com.sum.library.domain

import android.arch.lifecycle.*
import android.support.annotation.MainThread

/**
 * Created by sdl on 2018/7/13.
 */
abstract class BaseViewModel<T : BaseRepository> : ViewModel() {

    private var mHasRegisterAction = false

    protected val mRepository by lazy {
        getRepository()
    }

    protected abstract fun getRepository(): T

    private val mState = MutableLiveData<ActionState>()

    private val mStateLiveData by lazy {
        Transformations.switchMap(mState) {
            mRepository.registerActionState(it)
        }
    }

    //统一界面状态注册
    @MainThread
    fun registerActionState(lifecycleOwner: LifecycleOwner, observer: Observer<ActionState>) {
        if (mHasRegisterAction) {
            return
        }
        mHasRegisterAction = true

        mStateLiveData.observe(lifecycleOwner, observer)
        //首次注册后，必须通过mState发送一次状态，后面才能将仓库中的状态发送给界面的observer
        sendActionState(ActionState.REGISTER)
    }

    @MainThread
    fun sendActionState(state: Int) {
        mState.value = ActionState.obtain(state)
    }

    @MainThread
    fun sendActionState(state: ActionState) {
        mState.value = state
    }
}