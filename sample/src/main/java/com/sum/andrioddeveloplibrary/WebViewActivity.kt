package com.sum.andrioddeveloplibrary

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web_view.*





class WebViewActivity : AppCompatActivity() {

    lateinit var mWebView:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        mWebView = pub_web_view

        val localWebSettings = this.mWebView.getSettings()
        localWebSettings.setJavaScriptEnabled(true)
        localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true)
        localWebSettings.setUseWideViewPort(true)
        localWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
        localWebSettings.setDisplayZoomControls(false)
        localWebSettings.setAllowFileAccess(true)
        localWebSettings.setBuiltInZoomControls(true)
        localWebSettings.setSupportZoom(true)
        localWebSettings.setDomStorageEnabled(true)
        localWebSettings.setLoadWithOverviewMode(true)
        localWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR)
        val localDisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(localDisplayMetrics)

        this.mWebView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(paramAnonymousWebView: WebView, paramAnonymousString: String): Boolean {
                paramAnonymousWebView.loadUrl(paramAnonymousString)
                return true
            }
        })

        mWebView.loadUrl("http://m.018929.com/index.php/phone/userReg")
    }
}
