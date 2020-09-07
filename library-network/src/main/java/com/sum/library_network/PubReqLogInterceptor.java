package com.sum.library_network;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by sdl on 2020/8/13
 */
public class PubReqLogInterceptor implements Interceptor {

    private boolean mReqLog = true;

    private boolean mPostManLog = false;

    //postman 请求体
    public PubReqLogInterceptor postManLog() {
        mPostManLog = true;
        return this;
    }

    public PubReqLogInterceptor logClose() {
        mReqLog = false;
        mPostManLog = false;
        return this;
    }

    //Tag标签
    public static String TAG = "AppNet";

    private static volatile int index = 100;

    private static int getIndex() {
        return index++;
    }

    protected boolean needLog() {
        return mReqLog;
    }

    protected boolean needPostManLog() {
        return mPostManLog;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        if (needLog()) {

            int index = getIndex();
            if (needLog()) {
                printLog(
                        "req(" + index + ")->{\n" +
                                "head:[" + request.headers().toString() + "] " + request.method()
                                + ",url:" + request.url().toString() + "}");
            }
            //get请求,参数在url中
            //post只有打印body体内容
            if (request.method().equals("POST")) {
                request = this.addPostParamsSign(request, index);
            }
            //返回体打印
            response = chain.proceed(request);
            if (needLog()) {
                printLog("rsp(" + index + ")->" + unicodeToString(getBodyString(response)));
            }
        } else {
            response = chain.proceed(request);
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
        Buffer buffer = source.getBuffer();
        Charset charset = StandardCharsets.UTF_8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            contentType.charset(charset);
        }
        String len;
        if (responseBody.contentLength() == -1) {
            len = "len:" + buffer.size();
        } else {
            len = "len:" + responseBody.contentLength();
        }
        return len + ";" + buffer.clone().readString(charset);
    }

    private Request addPostParamsSign(Request request, int index) {
        if (request.body() instanceof FormBody) {
            //完全新创建一个查询参数体
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = (FormBody) request.body();
            TreeMap<String, String> oldParams = new TreeMap<>();
            for (int i = 0; i < formBody.size(); ++i) {
                oldParams.put(formBody.name(i), formBody.value(i));
            }

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
                printLog("req(" + index + ") params->\n" + content);
            }
            if (needLog() && needPostManLog()) {
                printLog("req(" + index + ") postman->" + array.toString());
            }
            formBody = bodyBuilder.build();

            Request.Builder builder = request.newBuilder();

            request = builder.post(formBody).build();
        } else {
            //构建新想请求
            Request.Builder builder = request.newBuilder();
            //请求体
            RequestBody body = request.body();
            long contentLength = 0;
            String content = "";
            if (body != null) {
                try {
                    contentLength = body.contentLength();
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);
                    //编码设为UTF-8
                    MediaType contentType = body.contentType();
                    if (contentType != null) {
                        contentType.charset(StandardCharsets.UTF_8);
                    }
                    content = "len:" + contentLength + ";" + buffer.readString(StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (needLog()) {
                printLog("req(" + index + ") params->" + content);
            }
            request = builder.build();
        }
        return request;
    }

    //日志内容，可以手动重写
    protected void printLog(String log) {
        Log.d(TAG, log);
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
}
