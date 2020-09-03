package com.sum.library_network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sdl on 2017/12/28.
 * 统一参数添加
 */

public abstract class PubParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            request = this.addGetParamsSign(request);
        } else if (request.method().equals("POST")) {
            request = this.addPostParamsSign(request);
        }
        return chain.proceed(request);
    }

    private Request addGetParamsSign(Request request) {
        HttpUrl httpUrl = request.url();
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        Set<String> nameSet = httpUrl.queryParameterNames();

        //GET查询参数
        ArrayList<String> nameList = new ArrayList<>(nameSet);

        TreeMap<String, String> oldParams = new TreeMap<>();
        for (int i = 0; i < nameList.size(); ++i) {
            String name = nameList.get(i);
            //同个key对应多个value
            httpUrl.queryParameterValues(name);
            String value = httpUrl.queryParameterValues(name).size() > 0 ?
                    httpUrl.queryParameterValues(name).get(0) : "";
            oldParams.put(name, value);
        }
        String nameKeys = Arrays.asList(nameList).toString();
        addPubParams(oldParams);
        //过滤重复数据
        for (Map.Entry<String, String> entry : oldParams.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            if (!nameKeys.contains(entry.getKey())) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        httpUrl = urlBuilder.build();

        Request.Builder builder = request.newBuilder();
        HashMap<String, String> headers = addPubHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = builder.url(httpUrl).build();
        return request;
    }

    private Request addPostParamsSign(Request request) {
        //form表单格式
        if (request.body() instanceof FormBody) {
            //完全新创建一个查询参数体
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = (FormBody) request.body();
            TreeMap<String, String> oldParams = new TreeMap<>();
            for (int i = 0; i < formBody.size(); ++i) {
                oldParams.put(formBody.name(i), formBody.value(i));
            }
            this.addPubParams(oldParams);
            for (Map.Entry<String, String> entry : oldParams.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                bodyBuilder.add(entry.getKey(), entry.getValue());
            }
            formBody = bodyBuilder.build();
            Request.Builder builder = request.newBuilder();
            HashMap<String, String> headers = addPubHeaders();
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            request = builder.post(formBody).build();
        } else {
            //原生数据
            Request.Builder builder = request.newBuilder();
            //增加公共头部参数
            HashMap<String, String> headers = addPubHeaders();
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            request = builder.build();
        }
        return request;
    }

    /**
     * 添加统一请求参数
     */
    public abstract void addPubParams(TreeMap<String, String> params);

    /**
     * 添加统一head参数
     */
    public abstract HashMap<String, String> addPubHeaders();
}
