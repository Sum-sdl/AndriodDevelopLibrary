package com.sum.library.net.callback;


import com.sum.library.utils.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sdl on 2017/12/29.
 * 基础处理
 */

public abstract class RetCallBack<T> implements retrofit2.Callback<T> {

    private Call<T> mCall;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        mCall = call;
        onSuccess(response.body());
        onFinally();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Logger.e(t.toString());
        onFinally();
    }


    public abstract void onSuccess(T response);

    public void onFinally() {
    }

    public Call<T> getCall() {
        return mCall;
    }
}
