package com.sum.andrioddeveloplibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by sdl on 2018/5/22.
 */
public class WebActivity extends FragmentActivity {

    public static void open(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    WebView mWeb;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);

        WebView webView = new WebView(this);
        webView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        setContentView(webView);
        mWeb = webView;
        mWeb.setLayerType(1,null);
        mWeb.setSaveEnabled(true);
        mWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWeb.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWeb.setVerticalFadingEdgeEnabled(false);
        mWeb.setHorizontalFadingEdgeEnabled(false);

        WebSettings settings = mWeb.getSettings();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        }
        settings.setUserAgentString(settings.getUserAgentString() + "; android_app/1.0.0");
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setLoadsImagesAutomatically(true);

        //cache
        String str = getCacheDir().getAbsolutePath();
        settings.setDatabasePath(str);
        settings.setAppCachePath(str);
        settings.setAppCacheEnabled(true);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSaveFormData(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setNeedInitialFocus(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2) {
                super.onReceivedError(paramAnonymousWebView, paramAnonymousInt, paramAnonymousString1, paramAnonymousString2);
                paramAnonymousWebView.loadDataWithBaseURL(null, "<span style=\"color:#FFFFFF\"></span>", "text/html", "utf-8", null);
            }
        });
        mWeb.loadUrl(getIntent().getStringExtra("url"));
    }


    @Override
    protected void onDestroy() {
        if (mWeb != null) {
            mWeb.destroy();
            mWeb = null;
        }
        super.onDestroy();
    }

    private long mLastTime = 0;

    @Override
    public void onBackPressed() {
        if (mWeb.canGoBack()) {
            mWeb.goBack();
        } else {
            long cur = System.currentTimeMillis();
            if (cur - mLastTime < 3000) {
                finish();
            } else {
                Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT).show();
            }
            mLastTime = cur;
        }
    }
}
