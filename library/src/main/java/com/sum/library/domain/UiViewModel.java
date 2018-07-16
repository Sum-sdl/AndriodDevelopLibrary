package com.sum.library.domain;

/**
 * Created by sdl on 2018/7/16.
 */
public interface UiViewModel {
    <T extends BaseViewModel> T getViewModel();
}
