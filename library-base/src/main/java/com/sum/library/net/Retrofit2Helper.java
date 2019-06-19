package com.sum.library.net;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
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
    private Retrofit mUploadFileRetrofit;

    private Retrofit2Helper() {
    }

    private OkHttpClient.Builder buildDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS);
        return builder;
    }

    //直接设置
    public void initRetrofit(Retrofit.Builder builder) {
        mRetrofit = builder.build();
    }

    //直接默认设置
    public void initRetrofit(String baseUrl) {
        initRetrofit(baseUrl, buildDefaultOkHttpClient().build(), new Gson());
    }

    //自己添加添加公共参数Interceptor
    public void initRetrofit(String baseUrl, Gson gson, Interceptor... interceptor) {
        OkHttpClient.Builder builder = buildDefaultOkHttpClient();
        if (interceptor != null && interceptor.length > 0) {
            for (Interceptor item : interceptor) {
                builder.addInterceptor(item);
            }
        }
        initRetrofit(baseUrl, builder.build(), gson);
    }

    private void initRetrofit(String baseUrl, OkHttpClient client, Gson gson) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        //自动将ResponseBody转成对象,自动将请求体转成json
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        builder.client(client);
        mRetrofit = builder.build();
    }

    //自定义上传文件操作信息
    public Retrofit initUploadFileRetrofit(String uploadUrl, int seconds) {
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

    public Retrofit initUploadFileRetrofit(Retrofit.Builder builder) {
        mUploadFileRetrofit = builder.build();
        return mUploadFileRetrofit;
    }

    public static Retrofit getRetrofit() {
        return Retrofit2Helper.getInstance().mRetrofit;
    }

    public static Retrofit getUploadFileRetrofit() {
        return Retrofit2Helper.getInstance().mUploadFileRetrofit;
    }

}
