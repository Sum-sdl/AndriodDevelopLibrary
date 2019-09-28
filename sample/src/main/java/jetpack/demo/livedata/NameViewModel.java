package jetpack.demo.livedata;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/7/9.
 */
public class NameViewModel extends ViewModel implements LifecycleObserver {

    //1.一个ViewModel对应一个界面,负责为UI准备数据
    //2.定义一个方法返回一个MutableLiveData，LiveData观察一个数据，数据变更会通知Observer修改
    //3.添加观察者observer，支持多个观察者

    private MutableLiveData<String> mCurrentName;


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Logger.e("NameViewModel onCreate");

        getCurrentName().setValue("onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        getCurrentName().setValue("onDestroy");
        Logger.e("NameViewModel onDestroy");
    }


    //数据源转换
//    public final LiveData<String> postalCode =
//            Transformations.switchMap(mCurrentName, (address) -> {
//                return r
//            });

    public MutableLiveData<String> getCurrentName() {
        if (mCurrentName == null) {
            mCurrentName = new MutableLiveData<>();
        }
        return mCurrentName;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Logger.e("NameViewModel onCleared");
    }
}
