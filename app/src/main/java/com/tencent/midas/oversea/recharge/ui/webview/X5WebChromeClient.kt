package com.tencent.midas.oversea.recharge.ui.webview

import android.widget.ProgressBar
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.midas.oversea.recharge.comm.MLog

class X5WebChromeClient(webView: WrapperWebView) : WebChromeClient() {

    private val mProgressBar: ProgressBar? =webView.progressBar

    override fun onReceivedTitle(view: WebView?, title: String?) {
        MLog.d("X5WebChromeClient", "onReceivedTitle: " + title!!)

        // android 6.0 以下通过title获取
        //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        //            if (title.contains("404") || title.contains("500") || title.contains("Error")) {
        //                if(mListener != null){
        //                    mListener.onError("");
        //                }
        //            }
        //        }

        super.onReceivedTitle(view, title)
    }

    override fun onConsoleMessage(cm: ConsoleMessage?): Boolean {
        if (null != cm) {
            MLog.i("X5WebChromeClient", cm.message() + " -- From line "
                    + cm.lineNumber() + " of "
                    + cm.sourceId())
        }
        return super.onConsoleMessage(cm)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        val currentProgress = mProgressBar!!.progress
        if (currentProgress >= 100) {
            // 开启属性动画让进度条平滑消失
            AnimationUtils.startDismissAnimation(mProgressBar, mProgressBar.progress)
        } else {
            // 开启属性动画让进度条平滑递增
            AnimationUtils.startProgressAnimation(mProgressBar, currentProgress, newProgress)
        }

        super.onProgressChanged(view, newProgress)
    }
}
