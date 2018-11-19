package com.sum.andrioddeveloplibrary.net.interceptorTest;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

        //链式调用，
        new Test().start();

        Type type =new TypeToken<Test>(){}.getType();

    }
}
