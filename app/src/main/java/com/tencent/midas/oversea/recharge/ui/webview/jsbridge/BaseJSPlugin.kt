package com.tencent.midas.oversea.recharge.ui.webview.jsbridge

import android.content.Context

import com.tencent.midas.oversea.recharge.ui.webview.WrapperWebView

abstract class BaseJSPlugin {

    private var mJSBridge: JSBridge? = null
    var callbackId: String? = null
    var requestParams: String? = null

    val webView: WrapperWebView?
        get() = if (mJSBridge != null) {
            mJSBridge!!.webView
        } else null

    val context: Context?
        get() = mJSBridge?.webView?.context ?: null

    fun setJSBridge(jsBridge: JSBridge) {
        mJSBridge = jsBridge
    }

    /**
     * 所有子类在此实现js调native业务逻辑，异步
     * @param callbackId
     * @param requestParams
     */
    abstract fun jsCallNative(callbackId: String, requestParams: String)
}
