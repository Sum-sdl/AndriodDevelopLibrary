package com.sum.library.app.common;

import com.sum.library.domain.ActionState;
import com.sum.library.domain.BaseViewModel;

/**
 * Created by sdl on 2018/7/16.
 */
public interface UiAction {

    /**
     * 界面对应的ViewModel
     */
    BaseViewModel getViewModel();

    /**
     * 扩展ActionState的其他操作
     */
    void expandActionDeal(ActionState state);
}
