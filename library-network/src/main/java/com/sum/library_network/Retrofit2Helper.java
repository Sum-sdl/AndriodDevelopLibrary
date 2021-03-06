package com.sum.library_network;

import com.google.gson.Gson;
import com.sum.library_network.converter.StringConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
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

    //网络请求类
    private Retrofit mRetrofit;
    //构建请求基础参数
    private OkHttpClient.Builder mClientBuilder;
    private String mBaseUrl;

    //数据转换处理类
    private List<Converter.Factory> mConverter = new ArrayList<>();

    private Retrofit2Helper() {
        mClientBuilder = buildDefaultOkHttpClient();
    }

    private OkHttpClient.Builder buildDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS);
        return builder;
    }

    /**
     * 必须调用一次初始化
     */
    public void init() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(mBaseUrl);
        //自动将ResponseBody转成对象,自动将请求体转成json
        //将请求体@body注解的对转成Json数据传输
        if (!mConverter.isEmpty()) {
            for (Converter.Factory factory : mConverter) {
                builder.addConverterFactory(factory);
            }
        }
        builder.client(mClientBuilder.build());
        mRetrofit = builder.build();
    }

    //直接默认设置
    public Retrofit2Helper setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    public Retrofit2Helper addConvert(Converter.Factory factory) {
        mConverter.add(factory);
        return this;
    }


    public Retrofit2Helper addDefaultConvert() {
        mConverter.add(new StringConverterFactory());
        mConverter.add(GsonConverterFactory.create(new Gson()));
        return this;
    }


    //添加自定义插值器
    public Retrofit2Helper addInterceptor(Interceptor interceptor) {
        mClientBuilder.addInterceptor(interceptor);
        return this;
    }

    //信任全部证书
    public Retrofit2Helper setCertificates() {
        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory(null, null, null,
                null);
        mClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    //所有对Builder的设置必须在init方法之前调用
    public OkHttpClient.Builder getClientBuilder() {
        return mClientBuilder;
    }

    //直接设置默认请求类
    public void resetDefaultRetrofit(Retrofit.Builder builder) {
        mRetrofit = builder.build();
    }

    public static Retrofit getRetrofit() {
        return Retrofit2Helper.getInstance().mRetrofit;
    }
}
