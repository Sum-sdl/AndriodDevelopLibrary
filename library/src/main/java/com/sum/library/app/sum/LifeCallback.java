package com.sum.library.app.sum;

/**
 * Created by Summer on 2016/9/10.
 */
public interface LifeCallback {
    void onEnter(Object data);

    void onLeave();

    void onBack();

    void onBackWithData(Object data);

    boolean processBackPressed();
}
