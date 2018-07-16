package jetpack.demo.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/7/9.
 * 通过注解调用对应生命周期方法
 */
public class MyObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Logger.e("MyObserver onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Logger.e("MyObserver onPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Logger.e("MyObserver onDestroy");
    }

}
