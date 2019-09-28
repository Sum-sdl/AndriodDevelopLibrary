package jetpack.demo.base;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

/**
 * Created by sdl on 2018/7/10.
 * 统一创建管理固定一组数据ViewModel，注意ViewModelStore内部实现
 */
public class ViewModelOwner implements ViewModelStoreOwner {

    private ViewModelStore store = new ViewModelStore();

    private static ViewModelOwner instance;

    private void ViewModelInstance() {

    }

    public static ViewModelOwner instance() {
        if (instance == null) {
            instance = new ViewModelOwner();
        }
        return instance;
    }

    public ViewModelProvider.Factory factory() {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(Utils.getApp());
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return store;
    }
}
