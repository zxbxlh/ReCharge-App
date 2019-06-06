package com.tencent.midas.oversea.recharge.ui.webview

import android.net.Uri
import android.text.TextUtils
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.midas.oversea.recharge.comm.MLog
import java.io.File
import java.io.FileInputStream


/**
 * WebViewClient逻辑处理
 */
class WebViewClientPresenter(webView: WrapperWebView) {

    //private val mProgressBar: ProgressBar = webView.progressBar

    /**
     * 判断Uri是否需要拦截
     *
     * @param uri 网络URI
     * @return 若需要拦截，返回WebResourceResponse；否则为null
     */
    fun shouldInterceptRequest(uri: Uri?): WebResourceResponse? {
        if (null == uri || null == uri.path) {
            return null
        }
        // 判断请求是以下["css","js","jpg","jpeg","png","gif"]的资源，走本地缓存逻辑
        return if (HybridCacheUtils.mountedSDCard() && HybridCacheUtils.needCache(uri)) {
            insteadOfCache(uri)
        } else null
    }


    /**
     * 检查是否有缓存，如有则读取本地缓存，否则缓存资源到本地
     * @param uri 需要走缓存逻辑的网络请求URI
     * @return 若本地有缓存，返回缓存资源；否则返回null
     */
    private fun insteadOfCache(uri: Uri): WebResourceResponse? {
        MLog.d(TAG, "insteadOfCache uri: $uri")

        val uriPath = uri.path
        if (TextUtils.isEmpty(uriPath)) {
            return null
        }

        val localCachePath = HybridCacheUtils.convertUriToFilePath(uri)
        // 如果缓存存在，则取缓存
        if (HybridCacheUtils.checkPathExist(localCachePath!!)) {
            try {
                MLog.d(TAG, "use local file cache")
                val `is` = FileInputStream(File(localCachePath))
                return WebResourceResponse(HybridCacheUtils.getResourceType(uriPath!!), "UTF-8", `is`)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            MLog.d(TAG, "request from net")
            // 不存在，网络请求并缓存在本地
            HybridCacheUtils.saveResource(uri, HybridCacheUtils.convertUriToFilePath(uri), null)
        }
        return null
    }

    companion object {
        const val TAG = "WebViewClientPresenter"
    }
}
