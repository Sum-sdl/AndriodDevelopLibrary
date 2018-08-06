package com.sum.library.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sdl on 2017/12/27.
 * Retrofit 快速初始化使用
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

    private Retrofit mRetrofit;
    private Retrofit.Builder mBuilder;

    private Retrofit mUploadFileRetrofit;

    private Retrofit2Helper() {
        //自动将ResponseBody转成对象
        mBuilder = new Retrofit.Builder();
        mBuilder.addConverterFactory(GsonConverterFactory.create());
    }

    //自己添加添加公共参数Interceptor
    public OkHttpClient.Builder buildDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS);
        return builder;
    }


    public void initRetrofit(String baseUrl, OkHttpClient client) {
        mBuilder.baseUrl(baseUrl);
        mBuilder.client(client);
        mRetrofit = mBuilder.build();
    }

    public Retrofit createUploadFile(String uploadUrl, int seconds) {
        if (mUploadFileRetrofit != null) {
            return mUploadFileRetrofit;
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(seconds, TimeUnit.SECONDS)
                .writeTimeout(seconds, TimeUnit.SECONDS)
                .readTimeout(seconds, TimeUnit.SECONDS)
                .build();

        mUploadFileRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(uploadUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mUploadFileRetrofit;
    }

    public static Retrofit getRetrofit() {
        return Retrofit2Helper.getInstance().mRetrofit;
    }

}
