package com.sum.andrioddeveloplibrary.net.interceptorTest;


/**
 * Created by sdl on 2018/11/14.
 */
public class NameInterceptor implements CInterceptor {

    @Override
    public String intercept(InterceptorChain chain) {
        //当前的请求数据
        String request = chain.request();
        String addName = request + ",add name";
        System.out.println("NameInterceptor intercept req");

        //传递个下一个拦截器处理
        String proceed = chain.proceed(addName);
        System.out.println("NameInterceptor intercept proceed");
        //最后的返回结果
        return proceed + " 1";
    }
}
