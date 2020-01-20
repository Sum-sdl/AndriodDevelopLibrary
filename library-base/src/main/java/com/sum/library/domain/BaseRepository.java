package com.sum.library.domain;

/**
 * Created by sdl on 2019/2/18.
 * 数据源的获取类，只有数据，没有跟UI交互的方式，与UI交互完全通过ViewModel层
 */
public class BaseRepository {

    public void onCleared() {
        //viewModel销毁调用
    }
}
