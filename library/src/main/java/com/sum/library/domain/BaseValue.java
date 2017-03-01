package com.sum.library.domain;

/**
 * Created by Summer on 2016/10/14.
 */

public class BaseValue extends BaseContextPresenter {

    //Snack或Toast提示
    public static final int TYPE_TOAST = -1;
    //进度条提示
    public static final int TYPE_DIALOG_PROGRESS_SHOW = -2;
    //关闭对话框
    public static final int TYPE_DIALOG_HIDE = -3;
    //加载中
    public static final int TYPE_DIALOG_LOADING = -4;

    public BaseValue(ContextView view) {
        super(view);
    }
}
