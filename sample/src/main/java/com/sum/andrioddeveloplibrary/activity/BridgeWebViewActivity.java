package com.sum.andrioddeveloplibrary.activity;

import com.blankj.utilcode.util.LogUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.sum.andrioddeveloplibrary.R;
import com.sum.library.app.BaseActivity;

/**
 * Created by sdl on 2018/11/29.
 */
public class BridgeWebViewActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_brige_web_view;
    }

    BridgeWebView webView;

    @Override
    protected void initParams() {
        webView = findViewById(R.id.bridge_web_View);
        findViewById(R.id.b1).setOnClickListener(v -> load());
        findViewById(R.id.b2).setOnClickListener(v -> sendToWeb());
        findViewById(R.id.b3).setOnClickListener(v -> webView.send("Send Hello"));

        //注册JS调用本地事件
        //window.WebViewJavascriptBridge.callHandler(
        //    'submitFromWeb'
        //    , {'param': '中文测试submitFromWeb'}
        //    , function(responseData) {
        //    }
        //);
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtils.e(data);
                function.onCallBack("native 返回给web");
            }
        });

        //默认处理类,用于处理web的通过send发送的数据
        //window.WebViewJavascriptBridge.send

        //webView.send("Send Hello")
        // bridge.init中处理App的send发出数据
        webView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtils.e(data);
                function.onCallBack("App To Js");
            }
        });
        load();


    }

    //需要web中手动注册 bridge.registerHandler对应的方法
    private void sendToWeb() {
        //connectWebViewJavascriptBridge(function(bridge) {
        //            bridge.init(function(message, responseCallback) {
        //                console.log('JS got a message', message);
        //                var data = {
        //                    'Javascript Responds': '测试中文!'
        //                };
        //                if (responseCallback) {
        //                    console.log('JS responding with', data);
        //                    responseCallback(data);
        //                }
        //            });
        //            bridge.registerHandler("functionInJs", function(data, responseCallback) {
        //                document.getElementById("show").innerHTML = ("data from App: = " + data);
        //                if (responseCallback) {
        //                    var responseData = "Javascript Says Right back aka!";
        //                    responseCallback(responseData);
        //                }
        //            });
        //        })
        webView.callHandler("functionInJs", "Send Native Data To JS", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                LogUtils.e("执行js后给客户端->" + data);
            }
        });
    }

    private void load() {
        webView.loadUrl("file:///android_asset/bridge.html");
    }

}
