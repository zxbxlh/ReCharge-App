package com.tencent.midas.oversea.recharge.ui.webview

import android.content.Context
import android.content.MutableContextWrapper
import android.view.ViewGroup
import java.util.ArrayList

/**
 * WebView池
 * @author zachzeng
 * 2019.2.27
 */
class WebViewPool private constructor() {
    private val valid: MutableList<WrapperWebView>
    private val sInUse: MutableList<WrapperWebView>
    private var maxCapacity = 1

    private var appContext: Context? = null

    val webView: WrapperWebView
        @Synchronized get() {
            var webView: WrapperWebView
            if (valid.isNotEmpty()) {
                webView = valid[0]
                valid.removeAt(0)
            } else {
                webView = WrapperWebView(MutableContextWrapper(appContext))
                val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                webView.layoutParams = layoutParams
            }

            sInUse.add(webView)
            webView.loadUrl("")
            return webView
        }

    init {
        valid = ArrayList()
        sInUse = ArrayList()
    }

    fun setMaxCapacity(maxCapacity: Int) {
        this.maxCapacity = maxCapacity
    }

    fun init(context: Context) {
        appContext = context.applicationContext

        for (i in 0 until maxCapacity) {
            val webView = WrapperWebView(MutableContextWrapper(appContext))
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            webView.layoutParams = layoutParams
            valid.add(webView)
        }
    }


    @Synchronized
    fun resetWebView(webView: WrapperWebView) {
        (webView.context as MutableContextWrapper).baseContext = appContext
        webView.reset()
        sInUse.remove(webView)
        if (valid.size < maxCapacity) {//保存个数不能大于池子的大小
            valid.add(webView)
        } else { // 扩容出来的临时webview直接回收
            webView.destroy()
        }
    }

    companion object {
        const val TAG = "WebViewPool"

        //单例
        val singleton:WebViewPool by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            WebViewPool()
        }
    }
}
