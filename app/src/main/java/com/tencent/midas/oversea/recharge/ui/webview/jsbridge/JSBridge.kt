package com.tencent.midas.oversea.recharge.ui.webview.jsbridge

import android.os.Build
import android.text.TextUtils
import android.webkit.JavascriptInterface
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.midas.oversea.recharge.ui.webview.WrapperWebView
import java.util.HashMap

class JSBridge(val webView: WrapperWebView) {

    //JSPlugin容器，key:js调用方法，value:相应JSPlugin
    private var jsPluginMap: MutableMap<String, BaseJSPlugin>? = null

    init {
        webView.addJavascriptInterface(this, HYBRID_BRIDGE_NAME)
    }

    /**
     * 注册jsPlugin
     * @param jsFunction
     * @param jsPlugin
     */
    fun registerJSPlugin(jsFunction: String, jsPlugin: BaseJSPlugin?) {
        if (jsPluginMap == null) {
            jsPluginMap = HashMap()
        }

        if (!TextUtils.isEmpty(jsFunction) && jsPlugin != null) {
            jsPluginMap!![jsFunction] = jsPlugin
        }
    }

    /**
     * 分发JS请求，异步
     * @param functionName
     * @param callbackId
     * @param params
     */
    private fun dispatchJSRequest(functionName: String, callbackId: String, params: String) {
        //run ui-thread
        webView.post {
            val jsPlugin = jsPluginMap!![functionName]
            try {
                if (jsPlugin != null) {
                    jsPlugin.callbackId = callbackId
                    jsPlugin.requestParams = params
                    jsPlugin.setJSBridge(this@JSBridge)
                    jsPlugin.jsCallNative(callbackId, params)
                } else {
                    callbackJS(callbackId, JSCallbackType.FAIL, null)
                }
            } catch (e: Exception) {
                callbackJS(callbackId, JSCallbackType.FAIL, null)
            }
        }
    }

    /**
     * JS调用Android，通过该接口分发
     * @param functionName
     * @param callbackId
     * @param params
     */
    @JavascriptInterface
    fun sendMessage(functionName: String, callbackId: String, params: String) {
        dispatchJSRequest(functionName, callbackId, params)
    }

    /**
     * android call js ：android调用js统一调用此方法
     * @param callbackId
     * @param callbackFunctionName
     * @param params
     */
    fun callbackJS(callbackId: String, callbackFunctionName: JSCallbackType, params: String?) {

        //callback protocol
        val jsSB = StringBuilder()
        jsSB.append("MidasReChargeApp.callBackFromNative('")
        jsSB.append(callbackId)
        jsSB.append("','")
        jsSB.append(callbackFunctionName.value)
        jsSB.append(if (TextUtils.isEmpty(params)) "')" else "','$params')")

        //run in ui-thread
        webView.post {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(jsSB.toString(), ValueCallback {
                    //ignore
                })
            } else {
                webView.loadUrl("javascript:$jsSB")
            }
        }
    }

    companion object {
        //注入给JS调用的对象名称
        const val HYBRID_BRIDGE_NAME = "MidasReChargeAppNative"
    }
}
