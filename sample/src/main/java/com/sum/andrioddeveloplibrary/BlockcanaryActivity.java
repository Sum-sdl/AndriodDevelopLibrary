package com.sum.andrioddeveloplibrary;

import android.view.Choreographer;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.app.BaseActivity;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by sdl on 2018/12/28.
 * 线程卡顿
 */
public class BlockcanaryActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blockcanary;
    }

    @Override
    protected void initParams() {
        //线程优先级 -20 到 19；由大到小
//        HandlerThread
        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(2000);
                    ToastUtils.showShort("finish sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Choreographer
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                LogUtils.e("time:" + frameTimeNanos);
            }
        });
    }

    private void web() {
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
            }
        };
    }
}
