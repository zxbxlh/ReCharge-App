package com.tencent.midas.oversea.recharge.ui.webview

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ProgressBar
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

import java.util.Timer
import java.util.TimerTask


class X5WebViewClient(webView: WrapperWebView) : WebViewClient() {

    //WebView加载超时设置
    private var mTimer: Timer? = null
    private val timeOut = 15000   //15s

    private val mProgressBar: ProgressBar = webView.progressBar
    private var mListener: WrapperWebView.WrapperWebViewListener = webView.wrapperWebViewListener
    private val mWebViewClientPresenter: WebViewClientPresenter = WebViewClientPresenter(webView)


    internal var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                WEB_VIEW_TIMEOUT -> {
                    MLog.d(TAG, "Network timeout,please check your network setting.")
                    mListener?.onError("Network timeout,please check your network setting.")
                }
            }
        }
    }


    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        MLog.d(TAG, "shouldOverrideUrlLoading: $url")
        return super.shouldOverrideUrlLoading(view, url)
    }

    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        MLog.d(TAG, "shouldInterceptRequest: ${request.url}")

        //intercept
        val localResource = mWebViewClientPresenter.shouldInterceptRequest(request.url)
        if (null != localResource) {
            return localResource
        }

        return super.shouldInterceptRequest(view, request)
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
        MLog.d(TAG, "onPageStarted: $url")
        mListener.onPageStarted()

        mProgressBar.visibility = View.VISIBLE
        mProgressBar.alpha = 1.0f

        //开始计时
        mTimer = Timer()
        val it = object : TimerTask() {
            override fun run() {
                val mes = mHandler.obtainMessage(WEB_VIEW_TIMEOUT)
                mes.sendToTarget()

                //cancel timer
                mTimer?.cancel()
                mTimer?.purge()
            }
        }
        mTimer?.schedule(it, timeOut.toLong())
    }

    override fun onPageFinished(webView: WebView?, s: String?) {
        super.onPageFinished(webView, s)
        MLog.d(TAG, "onPageFinished")

        //cancel timer
        mTimer?.cancel()
        mTimer?.purge()

        mListener.onPageFinished()
    }

    override fun onReceivedError(webView: WebView, webResourceRequest: WebResourceRequest, webResourceError: WebResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError)

        val errorCode = webResourceError.errorCode
        MLog.d(TAG, "onReceivedError: $errorCode")
        if (errorCode == WebViewClient.ERROR_CONNECT || errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
            mListener.onError(webResourceError.description.toString())
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceivedHttpError(webView: WebView?, webResourceRequest: WebResourceRequest?, webResourceResponse: WebResourceResponse?) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse)
        val statusCode = webResourceResponse!!.statusCode
        MLog.d(TAG, "onReceivedHttpError: $statusCode")
    }

    companion object {
        const val TAG = "X5WebViewClient"
        private const val WEB_VIEW_TIMEOUT = 0x01
    }
}
