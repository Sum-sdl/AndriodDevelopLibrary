package com.sum.library.ui.web;

import android.app.Activity;
import android.os.Parcelable;
import android.webkit.WebView;

/**
 * Created by sdl on 2018/1/2.
 */
public interface WebJavascriptInterface extends Parcelable {

    void addContext(Activity context);

    void addWebView(WebView webView);
}
