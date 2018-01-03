package com.sum.library.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.sum.library.utils.sonic.SonicRuntimeImpl;
import com.sum.library.utils.sonic.SonicSessionClientImpl;
import com.sum.library.view.widget.PubTitleView;
import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sdl on 2018/1/2.
 */
public class WebActivity extends BaseActivity {

    public WebView mWeb;
    public TextView mTitle;
    public PubTitleView mTitleView;
    private SonicSession sonicSession;
    private SonicSessionClientImpl sonicSessionClient = null;

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
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
        sonicSessionClient = new SonicSessionClientImpl();
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
            @Override
            public String getCacheData(SonicSession session) {
                return null;
            }
        });
        sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
            @Override
            public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                return new OfflinePkgSessionConnection(WebActivity.this, session, intent);
            }
        });
        sonicSession = SonicEngine.getInstance().createSession(getIntent().getStringExtra("url"), sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient);
        } else {
            finish();
        }
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
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
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
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setNeedInitialFocus(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(mWeb);
            sonicSessionClient.clientReady();
        } else {
            mWeb.loadUrl(getIntent().getStringExtra("url"));
        }
    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
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

    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
        }

        @Override
        protected int internalConnect() {
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
        }
    }
}
