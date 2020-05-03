package com.sum.andrioddeveloplibrary.api;

import com.blankj.utilcode.util.ToastUtils;
import com.zp.apt.annotation.ApiImpl;

/**
 * Created by sdl on 2020/5/3
 */
@ApiImpl
class ApiFun implements IApiFun {
    @Override
    public void toast() {
        ToastUtils.showShort("ApiFun Impl do");
    }
}
