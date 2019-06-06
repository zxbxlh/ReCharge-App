package com.tencent.midas.oversea.recharge.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.data.DataHandler
import com.tencent.midas.oversea.recharge.ui.webview.WebViewPool
import com.tencent.midas.oversea.recharge.ui.webview.WrapperWebView
import me.yokeyword.fragmentation.SupportFragment

/**
 * 充值中心Fragment
 * @author zachzeng
 * 2019.2.27
 */
class RechargeFragment : SupportFragment() {

    private  var mWebView: WrapperWebView? = null
    private var mWebViewContainer: FrameLayout? = null
    private var mSmartRefreshLayout: SmartRefreshLayout? = null
    private var isFirstLoad = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mSmartRefreshLayout = inflater.inflate(R.layout.layout_fragment_recharge, container, false) as SmartRefreshLayout
        mWebViewContainer = mSmartRefreshLayout!!.findViewById<View>(R.id.fr_webView_container) as FrameLayout

        mWebView = WebViewPool.singleton.webView
        mWebView!!.bindNewContext(context!!)
        mWebViewContainer!!.removeAllViews()
        mWebViewContainer!!.addView(mWebView)

        //监听webView状态
        mWebView!!.outWrapperListener = object : WrapperWebView.WrapperWebViewListener {
            override fun onPageStarted() {
                Log.d(TAG, "onPageStarted")
            }

            override fun onPageFinished() {
                Log.d(TAG, "onPageFinished")
                //加载完，停止刷新
                if (mSmartRefreshLayout != null) {
                    mSmartRefreshLayout!!.finishRefresh()
                }
            }

            override fun onError(errorInfo: String) {
                Log.d(TAG, "onError")
            }
        }

        //下拉刷新
        mSmartRefreshLayout!!.setOnRefreshListener { loadRechargeCenter() }

        //首次自动刷新
        isFirstLoad = true
        mSmartRefreshLayout!!.autoRefresh()
        return mSmartRefreshLayout
    }


    /**
     * 加载充值中心
     * 用户是否登录，加载不同界面
     */
    private fun loadRechargeCenter() {
        Log.d(TAG, "loadRechargeCenter")
        mWebView!!.loadUrl(DataHandler.reChargeUrl!!)

        //        if(isFirstLoad) {
        //            mWebView.loadUrl(DataHandler.getRechargeUrl());
        //            isFirstLoad = false;
        //        }else{
        //            String originalUrl = mWebView.getOriginalUrl();
        //            Log.d(TAG,"originalUrl: "+originalUrl);
        //            mWebView.loadUrl(originalUrl);
        //        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mWebViewContainer != null) {
            mWebViewContainer!!.removeAllViews()
        }

        WebViewPool.singleton.resetWebView(mWebView!!)
    }

    //back
    override fun onBackPressedSupport(): Boolean {
        MLog.d(TAG, "onBackPressedSupport()")
        if (mWebView!!.canGoBack()) {
            mWebView!!.goBack()
            //consume back press
            return true
        } else {
            return super.onBackPressedSupport()
        }
    }

    companion object {
        const val TAG = "RechargeFragment"

        fun newInstance(): RechargeFragment {
            val args = Bundle()
            val fragment = RechargeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
