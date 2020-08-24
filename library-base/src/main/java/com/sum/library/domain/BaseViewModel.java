package com.sum.library.domain;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Created by sdl on 2019/2/18.
 * 数据管理类，用户获取数据并通知给UI
 */
public abstract class BaseViewModel<T extends BaseRepository> extends ViewModel {

    private MutableLiveData<ActionState> mUiState = new MutableLiveData<>();

    private boolean mHasRegisterAction;

    //数据源处理类
    private BaseRepository mRepository;

    public BaseViewModel() {
        mRepository = buildRepository();
    }

    //子类获取数据仓库类
    public T getRepository() {
        if (mRepository != null) {
            mRepository = buildRepository();
        }
        return (T) mRepository;
    }

    //构建数据管理
    public abstract T buildRepository();
//    private <T> T createInstance(Class<T> repositoryClass) {
//        if (repositoryClass == null) {
//            return null;
//        }
//        try {
//            return repositoryClass.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException("Cannot create an instance of " + repositoryClass, e);
//        }
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRepository != null) {
            mRepository.onCleared();
        }
    }

    //统一界面状态注册
    @MainThread
    public void registerActionState(LifecycleOwner lifecycleOwner, Observer<ActionState> observer) {
        if (mHasRegisterAction) {
            return;
        }
        mHasRegisterAction = true;

        mUiState.observe(lifecycleOwner, observer);
        //首次注册后，必须通过mState发送一次状态，mRepository才能将仓库中的状态发送给界面的observer
        mUiState.setValue(ActionState.obtain(ActionState.REGISTER));
    }

    public void sendActionState(int state) {
        mUiState.postValue(ActionState.obtain(state));
    }

    public void sendActionState(int state, String msg) {
        ActionState action = ActionState.obtain(state);
        action.setMsg(msg);
        mUiState.postValue(action);
    }

    public void sendActionState(ActionState state) {
        mUiState.postValue(state);
    }

    //获取发送事件的LiveData
    public LiveData<ActionState> getLiveData() {
        return mUiState;
    }
}
