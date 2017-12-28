package com.sum.library.net;

/**
 * Created by sdl on 2017/12/27.
 */

public class Retrofit2Helper {

    private static final Object OBJECT = new Object();

    private static Retrofit2Helper mHelper;

    public static Retrofit2Helper getInstance() {
        if (mHelper == null) {
            synchronized (OBJECT) {
                if (mHelper == null) {
                    mHelper = new Retrofit2Helper();
                }
            }
        }
        return mHelper;
    }

    private Retrofit2Helper() {

    }
}
