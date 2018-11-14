package com.sum.andrioddeveloplibrary.net.interceptorTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdl on 2018/11/14.
 */
public class Test {


    public void start() {
        List<CInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new NameInterceptor());
        interceptors.add(new PhoneInterceptor());

        RealInterceptor start = new RealInterceptor(interceptors, "Start", 0);

        String result = start.procced();

        System.out.print(result);
    }


    public static void main(String[] args) throws InterruptedException {

        new Test().start();

    }
}
