package com.sum.library.app.common;

import com.sum.library.domain.ActionState;

/**
 * Created by sdl on 2020/8/24
 */
public interface ICommonActive {

    /**
     * 统一处理通用操作
     *
     * @param state     ViewModel 发送的状态数据
     * @param uiAction 扩展操作
     */
    void doActionCommand(ActionState state, UiAction uiAction);

    /**
     * 当前处理使用的加载框操作接口
     */
    LoadingView getLoadingView();
}
