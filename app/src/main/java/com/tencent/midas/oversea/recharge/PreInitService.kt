package com.tencent.midas.oversea.recharge

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.midas.oversea.recharge.ui.webview.WebViewPool
import com.tencent.smtt.sdk.QbSdk

class PreInitService : Service() {

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this)
        initX5()
        preInitX5WebCore()

        WebViewPool.singleton.init(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //初始化x5
    private fun initX5() {
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                Log.d("PreInitService", "onCoreInitFinished")
            }

            override fun onViewInitFinished(result: Boolean) {
                Log.d("PreInitService", "onViewInitFinished: $result")
            }
        })
    }

    //预加载
    private fun preInitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            // preInit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            QbSdk.preInit(applicationContext, null)
        }
    }
}
