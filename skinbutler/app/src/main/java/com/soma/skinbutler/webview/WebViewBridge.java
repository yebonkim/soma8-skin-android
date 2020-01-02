package com.soma.skinbutler.webview;

import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;

/**
 * Created by yebonkim on 2017. 10. 28..
 */

public class WebViewBridge {
    Handler mHandler;

    public WebViewBridge() {
        mHandler = new Handler();
    }


    @JavascriptInterface
    public void setMessage(final String arg) {
        mHandler.post(new Runnable() {
            public void run() {
                int tag = Integer.parseInt(arg);
                switch(tag) {
                }
            }
        });
    }
}
