package com.sum.library.net.token;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by sdl on 2017/12/28.
 * 统一参数添加
 */

public abstract class BaseDynamicInterceptor implements Interceptor {

    protected boolean needLog() {
        return false;
    }

    private static final String TAG = "app_net_log";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            request = this.addGetParamsSign(request);
        } else if (request.method().equals("POST")) {
            request = this.addPostParamsSign(request);
        }
        if (needLog()) {
            Log.d(TAG,
                    "req->method:" + request.method() +
                            " head:" + request.headers().toString() +
                            " url:" + request.url().toString());
        }
        Response response = chain.proceed(request);
        if (needLog()) {
            Log.d(TAG, "rsp->" + unicodeToString(getBodyString(response)));
        }
        return response;
    }


    private String getBodyString(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = null;
        if (responseBody != null) {
            source = responseBody.source();
        }
        if (source == null) {
            return null;
        }
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            contentType.charset(charset);
        }
        return buffer.clone().readString(charset);
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
            String value = httpUrl.queryParameterValues(name) != null
                    &&
                    httpUrl.queryParameterValues(name).size() > 0 ?
                    httpUrl.queryParameterValues(name).get(0) : "";
            oldParams.put(name, value);
        }
        String nameKeys = Arrays.asList(new ArrayList[]{nameList}).toString();
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
        if (request.body() instanceof FormBody) {
            //完全新创建一个查询参数体
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = (FormBody) request.body();
            TreeMap<String, String> oldParams = new TreeMap<>();
            if (formBody != null) {
                for (int i = 0; i < formBody.size(); ++i) {
                    oldParams.put(formBody.encodedName(i), formBody.encodedValue(i));
                }
            }
            this.addPubParams(oldParams);

            StringBuilder content = null;
            JsonArray array = null;
            if (needLog()) {
                content = new StringBuilder();
                array = new JsonArray();
            }
            for (Map.Entry<String, String> entry : oldParams.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                bodyBuilder.add(entry.getKey(), entry.getValue());
                if (needLog()) {
                    content.append(entry.getKey()).append("->").append(entry.getValue()).append("\n");
                    JsonObject object = new JsonObject();
                    object.addProperty("key", entry.getKey());
                    object.addProperty("value", entry.getValue());
                    object.addProperty("enabled", true);
                    array.add(object);
                }
            }
            if (needLog()) {
                Log.d(TAG, "post params->{\n" + content + "}");
                Log.d(TAG, "Postman params->" + array.toString());
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
        }
        return request;
    }


    private String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            String group = matcher.group(2);
            ch = (char) Integer.parseInt(group, 16);
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
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
