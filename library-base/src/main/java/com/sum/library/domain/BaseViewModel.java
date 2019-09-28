package com.sum.library.domain;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.annotation.MainThread;

/**
 * Created by sdl on 2019/2/18.
 * UI处理类
 */
public abstract class BaseViewModel<T extends BaseRepository> extends ViewModel {

    private MutableLiveData<ActionState> mUiState = new MutableLiveData<>();
    protected T mRepository;
    private boolean mHasRegisterAction;

    public BaseViewModel() {
        mRepository = createInstance(getRepositoryClass());
    }

    //ui数据源获取实现类
    protected abstract Class<T> getRepositoryClass();

    private <T> T createInstance(Class<T> repositoryClass) {
        try {
            return repositoryClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot create an instance of " + repositoryClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create an instance of " + repositoryClass, e);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    //统一界面状态注册
    @MainThread
    public void registerActionState(LifecycleOwner lifecycleOwner, Observer<ActionState> observer) {
        if (mRepository == null) {
            return;
        }
        if (mHasRegisterAction) {
            return;
        }
        mHasRegisterAction = true;
        LiveData<ActionState> switchMap = Transformations.switchMap(mUiState, new Function<ActionState, LiveData<ActionState>>() {
            @Override
            public LiveData<ActionState> apply(ActionState input) {
                return mRepository.registerActionState(input);
            }
        });
        switchMap.observe(lifecycleOwner, observer);

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
}
