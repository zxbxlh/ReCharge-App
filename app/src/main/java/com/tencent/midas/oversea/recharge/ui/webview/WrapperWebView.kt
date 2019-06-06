package com.tencent.midas.oversea.recharge.ui.webview

import android.annotation.TargetApi
import android.content.Context
import android.content.MutableContextWrapper
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.tencent.midas.oversea.recharge.comm.ImageTool
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.MLog

class WrapperWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var progressBar: ProgressBar

    private lateinit var webView: WebView
    private lateinit var mWebViewContainer: FrameLayout

    private val isWhiteList: Boolean = false   //是否为白名单

    private var mX5WebViewClient: X5WebViewClient? = null
    private var mErrorInfo: TextView? = null
    private var mErrorImage: SimpleDraweeView? = null
    private var mErrorImageUri = ""

    //out wrapper listener
    var outWrapperListener: WrapperWebViewListener? = null

    //wrapper listener,处理error
    var wrapperWebViewListener: WrapperWebViewListener = object : WrapperWebViewListener {
        override fun onPageStarted() {
            outWrapperListener?.onPageStarted()
        }

        override fun onPageFinished() {
            MLog.d(TAG,"onPageFinished()")
            outWrapperListener?.onPageFinished()
        }

        override fun onError(errorInfo: String) {
            handleError(errorInfo)
            outWrapperListener?.onError(errorInfo)
        }
    }

    init {
        initWrapperWebView(context)
        initWebViewSettings(context)
        initWebViewClient()
    }

    private fun initWrapperWebView(context: Context) {
        //attach to parent
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_wrapper_webview, this, true)

        //WebView
        mWebViewContainer = layout.findViewById<View>(R.id.ww_container) as FrameLayout
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        webView = WebView(context.applicationContext)
        webView.layoutParams = layoutParams
        mWebViewContainer.addView(webView, 0)

        progressBar = layout.findViewById<View>(R.id.ww_progress_bar) as ProgressBar
    }

    private fun initWebViewSettings(context: Context) {
        val webSettings = webView.settings
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.javaScriptEnabled = true        //允许js代码
        webSettings.textZoom = 100                //禁用文字缩放
        //webSettings.setSupportMultipleWindows(true);    // 用来支持下载弹窗
        webSettings.javaScriptCanOpenWindowsAutomatically = true

        //禁用放缩
        webSettings.displayZoomControls = false
        webSettings.builtInZoomControls = false

        //setting App Cache
        webSettings.setAppCacheEnabled(false)
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setAppCacheMaxSize(8 * 1024 * 1024);
//        webSettings.setAppCachePath(context.getFilesDir().getAbsolutePath() + "/MWebCache");
//        setting cache mode
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //enable Dom storage Cache
        webSettings.domStorageEnabled = true

        webSettings.allowFileAccess = true           //允许WebView使用File协议
        webSettings.savePassword = false            //不保存密码
        webSettings.loadsImagesAutomatically = true //自动加载图片ls

        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.allowContentAccess = true
        webSettings.saveFormData = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true)
            webSettings.setAllowUniversalAccessFromFileURLs(true)
        }

        //5.0以上开启混合模式加载
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }

        //移除不安全的接口
        try {
            val method = javaClass.getMethod(
                    "removeJavascriptInterface", String::class.java)
            method?.invoke(this, "searchBoxJavaBridge_")
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

    }

    private fun initWebViewClient() {
        val x5WebChromeClient = X5WebChromeClient(this)
        webView.webChromeClient = x5WebChromeClient
        mX5WebViewClient = X5WebViewClient(this)
        webView.webViewClient = mX5WebViewClient
    }

    fun setUserAgent(appId: String, versionCode: Int, language: String) {
        if (isWhiteList) {
            val ws = webView.settings
            val uaSB = StringBuilder()
            uaSB.append(ws.userAgentString)
                    .append(appId)
                    .append(".")
                    .append(versionCode)
                    .append(language)
            ws.userAgentString = uaSB.toString()
        }
    }

    fun showProgress(show: Boolean) {
        if (!show && progressBar!!.isShown) {
            progressBar!!.visibility = View.GONE
        } else if (show && !progressBar!!.isShown) {
            progressBar!!.visibility = View.VISIBLE
        }
    }

    fun setProgress(progress: Int) {
        progressBar.progress = progress
    }

    fun loadUrl(url: String) {
        if (!TextUtils.isEmpty(url)) {
            mWebViewContainer.visibility = View.VISIBLE

            if (mErrorInfo != null && mErrorImage != null) {
                mErrorInfo!!.visibility = View.GONE
                mErrorImage!!.visibility = View.GONE
            }

            webView.loadUrl(url)
        }
    }

    private fun handleError(errorInfo: String) {
        //首次加载Error View
        if (mErrorInfo == null || mErrorImage == null) {
            val errorStub = findViewById<View>(R.id.ww_error) as ViewStub
            errorStub.inflate()
            mErrorInfo = findViewById<View>(R.id.ww_error_info) as TextView
            mErrorImage = findViewById<View>(R.id.ww_error_image) as SimpleDraweeView
        }

        if (!mErrorInfo!!.isShown) {
            webView.loadUrl("about:blank")//避免出现默认的错误界面
            mWebViewContainer.visibility = View.INVISIBLE

            if (TextUtils.isEmpty(mErrorImageUri)) {
                //占位图片uri
                val uriSb = StringBuilder()
                uriSb.append("res://")
                        .append(context.packageName)
                        .append("/")
                        .append(R.drawable.webview_error_image)
                mErrorImageUri = uriSb.toString()
            }

            mErrorInfo!!.visibility = View.VISIBLE
            mErrorImage!!.visibility = View.VISIBLE

            //压缩显示图片
            mErrorImage!!.post {
                //压缩并显示图片
                ImageTool.frescoCompress(Uri.parse(mErrorImageUri), mErrorImage!!, mErrorImage!!.width, mErrorImage!!.height)
            }
        }
    }

    fun canGoBack(): Boolean {
        return webView.canGoBack()
    }

    fun goBack() {
        webView.goBack()
    }

    fun canGoForward(): Boolean {
        return webView.canGoForward()
    }

    fun canGoBackOrForward(i: Int): Boolean {
        return webView.canGoBackOrForward(i)
    }

    fun goForward() {
        webView.goForward()
    }

    fun goBackOrForward(i: Int) {
        webView.goBackOrForward(i)
    }

    fun reload() {
        webView.reload()
    }

    override fun removeAllViews() {
        webView.removeAllViews()
    }

    fun clearCache(b: Boolean) {
        webView.clearCache(b)
    }

    fun clearHistory() {
        webView.clearHistory()
    }

    fun clearSslPreferences() {
        webView.clearSslPreferences()
    }

    fun loadDataWithBaseURL(baseUrl: String, data: String, mimeType: String, encoding: String, historyUrl: String) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
    }

    fun addJavascriptInterface(o: Any, s: String) {
        webView.addJavascriptInterface(o, s)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun evaluateJavascript(s: String, valueCallback: ValueCallback<String>) {
        webView.evaluateJavascript(s, valueCallback)
    }

    /**
     * 绑定新的Context
     * @param context
     */
    fun bindNewContext(context: Context) {
        if (getContext() is MutableContextWrapper) {
            (getContext() as MutableContextWrapper).baseContext = context
        }
    }

    /**
     * 重置WebView
     */
    fun reset() {
        webView?.stopLoading()
        webView?.clearCache(true)
        webView?.clearHistory()
    }

    /**
     * 销毁WebView
     */
    fun destroy() {
        try {
            webView.stopLoading()
            webView.clearCache(true)
            webView.clearHistory()
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
            webView.removeAllViews()
            webView.destroy()
        } catch (e: Exception) {
            MLog.e(TAG, "destroy exception: " + e.message)
        }

    }

    interface WrapperWebViewListener {
        fun onPageStarted()
        fun onPageFinished()
        fun onError(errorInfo: String)
    }

    companion object {
        const val TAG = "WrapperWebView"
    }
}
