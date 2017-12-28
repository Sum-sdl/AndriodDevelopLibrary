package com.sum.library.net.token;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sdl on 2017/12/28.
 * 统一参数添加
 */

public abstract class BaseDynamicInterceptor implements Interceptor {
    private HttpUrl httpUrl;

    public BaseDynamicInterceptor() {
    }

    private String parseUrl(String url) {
        if (!"".equals(url) && url.contains("?")) {
            url = url.substring(0, url.indexOf(63));
        }
        return url;
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            this.httpUrl = HttpUrl.parse(parseUrl(request.url().toString()));
            request = this.addGetParamsSign(request);
        } else if (request.method().equals("POST")) {
            this.httpUrl = request.url();
            request = this.addPostParamsSign(request);
        }

        return chain.proceed(request);
    }

    public HttpUrl getHttpUrl() {
        return this.httpUrl;
    }

    private Request addGetParamsSign(Request request) throws UnsupportedEncodingException {
        HttpUrl httpUrl = request.url();
        HttpUrl.Builder newBuilder = httpUrl.newBuilder();
        Set<String> nameSet = httpUrl.queryParameterNames();
        ArrayList<String> nameList = new ArrayList<>();
        nameList.addAll(nameSet);
        TreeMap<String, String> oldParams = new TreeMap<>();

        for (int i = 0; i < nameList.size(); ++i) {
            String value = httpUrl.queryParameterValues((String) nameList.get(i)) != null && httpUrl.queryParameterValues((String) nameList.get(i)).size() > 0 ? (String) httpUrl.queryParameterValues((String) nameList.get(i)).get(0) : "";
            oldParams.put(nameList.get(i), value);
        }
        String nameKeys = Arrays.asList(new ArrayList[]{nameList}).toString();
        addPubParams(oldParams);
        Iterator var9 = oldParams.entrySet().iterator();

        while (var9.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var9.next();
            String urlValue = URLEncoder.encode((String) entry.getValue(), "UTF-8");
            if (!nameKeys.contains((CharSequence) entry.getKey())) {
                newBuilder.addQueryParameter((String) entry.getKey(), urlValue);
            }
        }
        httpUrl = newBuilder.build();
        request = request.newBuilder().url(httpUrl).build();
        return request;
    }

    private Request addPostParamsSign(Request request) throws UnsupportedEncodingException {
        if (request.body() instanceof FormBody) {
            okhttp3.FormBody.Builder bodyBuilder = new okhttp3.FormBody.Builder();
            FormBody formBody = (FormBody) request.body();

            TreeMap<String, String> oldParams = new TreeMap<>();
            if (formBody != null) {
                for (int i = 0; i < formBody.size(); ++i) {
                    oldParams.put(formBody.encodedName(i), formBody.encodedValue(i));
                }
            }

            this.addPubParams(oldParams);
            Iterator var6 = oldParams.entrySet().iterator();

            while (var6.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) var6.next();
                String value = URLDecoder.decode((String) entry.getValue(), "UTF-8");
                bodyBuilder.addEncoded((String) entry.getKey(), value);
            }
            formBody = bodyBuilder.build();
            request = request.newBuilder().post(formBody).build();
        } else if (request.body() instanceof MultipartBody) {
            MultipartBody multipartBody = (MultipartBody) request.body();
            okhttp3.MultipartBody.Builder bodyBuilder = new okhttp3.MultipartBody.Builder();
            List<MultipartBody.Part> oldparts = multipartBody.parts();
            List<MultipartBody.Part> newparts = new ArrayList<>();
            newparts.addAll(oldparts);

            TreeMap<String, String> newParams = new TreeMap<>();
            this.addPubParams(newParams);
            Iterator var21 = newParams.entrySet().iterator();

            while (var21.hasNext()) {
                Map.Entry<String, String> stringEntry = (Map.Entry) var21.next();
                MultipartBody.Part part = MultipartBody.Part.createFormData((String) stringEntry.getKey(), (String) stringEntry.getValue());
                newparts.add(part);
            }

            var21 = newparts.iterator();

            while (var21.hasNext()) {
                MultipartBody.Part part = (MultipartBody.Part) var21.next();
                bodyBuilder.addPart(part);
            }

            multipartBody = bodyBuilder.build();
            request = request.newBuilder().post(multipartBody).build();
        } else if (request.body() != null) {
            TreeMap<String, String> params = new TreeMap<>();
            addPubParams(params);
            String url = createUrlFromParams(this.httpUrl.url().toString(), params);
            request = request.newBuilder().url(url).build();
        }
        return request;
    }

    private String createUrlFromParams(String url, Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf(38) <= 0 && url.indexOf(63) <= 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }
            Iterator var3 = params.entrySet().iterator();
            while (var3.hasNext()) {
                Map.Entry<String, String> urlParams = (Map.Entry) var3.next();
                String urlValues = (String) urlParams.getValue();
                String urlValue = URLEncoder.encode(urlValues, "UTF-8");
                sb.append((String) urlParams.getKey()).append("=").append(urlValue).append("&");
            }

            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException var7) {
            return url;
        }
    }

    /**
     * 添加统一请求参数
     */
    abstract void addPubParams(TreeMap<String, String> old);
}
