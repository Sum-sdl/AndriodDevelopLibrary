package com.sum.library.ui.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sum.library.R;
import com.sum.library.app.BaseActivity;
import com.sum.library.ui.web.sonic.SonicRuntimeImpl;
import com.sum.library.ui.web.sonic.SonicSessionClientImpl;
import com.sum.library.view.widget.PubTitleView;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sdl on 2018/1/2.
 */
public class WebActivity extends BaseActivity {

    public WebView mWeb;
    public SmoothProgressBar mProgress;
    public TextView mTitle;
    public PubTitleView mTitleView;
    public WebJavascriptInterface mJs;

    private SonicSession sonicSession;
    private SonicSessionClientImpl sonicSessionClient = null;

    public static void open(Context c, String title, String url, WebJavascriptInterface js, String jsName) {
        Intent intent = new Intent(c, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("WebJavascriptInterface", js);
        intent.putExtra("WebJavascriptInterfaceName", jsName);
        c.startActivity(intent);
    }

    public static void open(Context c, String url) {
        open(c, null, url, null, null);
    }

    public static void open(Context c, String url, String title) {
        open(c, title, url, null, null);
    }

    @Override
    protected int statusBarColor() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
        sonicSessionClient = new SonicSessionClientImpl();
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
        sonicSession = SonicEngine.getInstance().createSession(getIntent().getStringExtra("url"), sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient);
        } else {
            finish();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    public void showTitle(boolean show) {
        if (show) {
            mTitleView.setVisibility(View.VISIBLE);
            findViewById(R.id.title_line).setVisibility(View.VISIBLE);
        } else {
            mTitleView.setVisibility(View.GONE);
            findViewById(R.id.title_line).setVisibility(View.GONE);
        }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    public void initParams() {
        mTitleView = findViewById(R.id.pub_title_view);
        showTitle(getIntent().getBooleanExtra("show_title", true));
        mProgress = findViewById(R.id.web_progress);

        mTitle = mTitleView.getTitleText();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            mTitle.setText(getIntent().getStringExtra("title"));
        }
        mWeb = findViewById(R.id.pub_web_view);
        mWeb.setSaveEnabled(true);
        mWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWeb.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWeb.setVerticalFadingEdgeEnabled(false);
        mWeb.setHorizontalFadingEdgeEnabled(false);

        mWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgress.setShowProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
                    mTitle.setText(getIntent().getStringExtra("title"));
                } else {
                    mTitle.setText(title);
                }
            }
        });

        WebSettings settings = mWeb.getSettings();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        }

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
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowContentAccess(true);
        settings.setNeedInitialFocus(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);

        settings.setAllowFileAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);

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
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });

        // 设置cookies
        if (getIntent().getStringExtra("cookieUrl") != null) {
            HashMap<String, String> cookies = new HashMap<>();
            ArrayList<String> cookieValues = getIntent().getStringArrayListExtra("cookieValues");
            for (int i = 0; i < cookieValues.size() / 2; i++) {
                cookies.put(cookieValues.get(i * 2), cookieValues.get(i * 2 + 1));
            }
            // cookies同步方法要在WebView的setting设置完之后调用，否则无效。
            syncCookie(this, getIntent().getStringExtra("cookieUrl"), cookies);
        }
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(mWeb);
            sonicSessionClient.clientReady();
        } else {
            mWeb.loadUrl(getIntent().getStringExtra("url"));
        }

    }

    private void syncCookie(Context context, String url, HashMap<String, String> cookies) {
        // 如果API是21以下的话，需要在CookieManager前加
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        // 注意使用for循环进行setCookie(String url, String value)调用。网上有博客表示使用分号手动拼接的value值会导致cookie不能完整设置或者无效
        for (Object o : cookies.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String value = entry.getKey() + "=" + entry.getValue();
            cookieManager.setCookie(url, value);
        }
        // 如果API是21以下的话,在for循环结束后加
        CookieSyncManager.getInstance().sync();
    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        if (mWeb != null) {
            mWeb.destroy();
            mWeb = null;
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
