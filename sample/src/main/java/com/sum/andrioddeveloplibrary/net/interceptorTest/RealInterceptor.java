package com.sum.andrioddeveloplibrary.net.interceptorTest;

import java.util.List;

/**
 * Created by sdl on 2018/11/14.
 */
public class RealInterceptor implements CInterceptor.InterceptorChain {

    private List<CInterceptor> interceptors;
    private String name;
    private int index;

    public RealInterceptor(List<CInterceptor> interceptors, String name, int index) {
        this.interceptors = interceptors;
        this.name = name;
        this.index = index;
    }

    public String procced() {
        if (index >= interceptors.size()) {
            return name;
        }

        //生成一个新的拦截器
        RealInterceptor next = new RealInterceptor(interceptors, name, index + 1);
        CInterceptor current = interceptors.get(index);
        //执行当前的拦截，并将下一个拦截传递过去
        return current.intercept(next);
    }

    @Override
    public String request() {
        return name;
    }

    @Override
    public String proceed(String request) {
        name = request;
        return procced();
    }
}
