package com.sum.library.net;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by sdl on 2017/12/27.
 */

public class OkHttpHelper {


    public interface RequestListener {
        void onStart();

        void onSuccess(String string);

        void onError();
    }

    public interface OnDownloadListener {
        void onSuccess(String filePath);

        void updateProgress(int progress, long bytesRead, long contentLength);

        void onError();
    }


    private OkHttpClient mClient;

    public OkHttpHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        mClient = builder.build();
    }

    public void Get(){

    }
}
