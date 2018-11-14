package com.sum.andrioddeveloplibrary.net.interceptorTest;


/**
 * Created by sdl on 2018/11/14.
 * 链式调用，思考流程
 * <p>
 * Interceptor 接受一个链Chain，
 * 该链是用来获取当前参数，并传递给下一个
 */
public interface CInterceptor {

    String intercept(InterceptorChain chain);

    interface InterceptorChain {
        String request();

        String proceed(String request);
    }
}
