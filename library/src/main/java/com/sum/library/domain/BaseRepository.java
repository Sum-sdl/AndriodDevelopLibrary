package com.sum.library.domain;

import android.arch.lifecycle.MutableLiveData;

/**
 * Created by sdl on 2019/2/18.
 */
public class BaseRepository {

    private MutableLiveData<ActionState> mActionLiveData;

    public BaseRepository() {
        mActionLiveData = new MutableLiveData<>();
    }

    //发送界面操作状态
    public void sendActionState(int state) {
        sendActionState(ActionState.obtain(state));
    }

    public void sendActionState(int state, String msg) {
        ActionState action = ActionState.obtain(state);
        action.setMsg(msg);
        sendActionState(action);
    }

    private void sendActionState(ActionState state) {
        mActionLiveData.postValue(state);
    }

    //viewModel发送界面操作状态
    MutableLiveData<ActionState> registerActionState(ActionState state) {
        mActionLiveData.postValue(state);
        return mActionLiveData;
    }
}
