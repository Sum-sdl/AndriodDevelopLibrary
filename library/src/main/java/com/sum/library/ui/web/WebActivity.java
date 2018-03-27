package com.sum.library.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sum.library.R;
import com.sum.library.app.BaseActivity;
import com.sum.library.view.widget.PubTitleView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sdl on 2018/1/2.
 */
public class WebActivity extends BaseActivity {

    public WebView mWeb;
    public TextView mTitle;
    public PubTitleView mTitleView;

    WebJavascriptInterface mJs;

    public static void open(Context c, String title, String url, WebJavascriptInterface js, String jsName) {
        Intent intent = new Intent(c, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("WebJavascriptInterface", js);
        intent.putExtra("WebJavascriptInterfaceName", jsName);
        c.startActivity(intent);
    }

    @Override
    protected int statusBarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initParams() {
        mTitleView = findViewById(R.id.pub_title_view);
        mTitle = mTitleView.getTitleText();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            mTitle.setText(getIntent().getStringExtra("title"));
        }
        mWeb = findViewById(R.id.pub_web_view);
        mWeb.setSaveEnabled(true);
        mWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
                    mTitle.setText(getIntent().getStringExtra("title"));
                } else {
                    mTitle.setText(title);
                }
            }
        });
        mWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings settings = mWeb.getSettings();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        }

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setNeedInitialFocus(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);

        settings.setJavaScriptEnabled(true);
        mJs = getIntent().getParcelableExtra("WebJavascriptInterface");
        if (mJs != null) {
            mJs.addContext(this);
            mJs.addWebView(mWeb);
            mWeb.addJavascriptInterface(mJs, getIntent().getStringExtra("WebJavascriptInterfaceName"));
        }
        mWeb.removeJavascriptInterface("searchBoxJavaBridge_");
        mWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        mWeb.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onDestroy() {
        if (mWeb != null) {
            ViewParent parent = mWeb.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWeb);
            }
            mWeb.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWeb.getSettings().setJavaScriptEnabled(false);
            mWeb.clearHistory();
            mWeb.clearView();
            mWeb.removeAllViews();
            try {
                mWeb.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWeb.canGoBack()) {
            mWeb.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (mJs != null) {
                for (Method method : mJs.getClass().getDeclaredMethods()) {
                    String name = method.getName();
                    if (name.startsWith("onActivityResult_") && name.split("_")[1].equals("" + requestCode)) {
                        try {
                            method.invoke(mJs);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
