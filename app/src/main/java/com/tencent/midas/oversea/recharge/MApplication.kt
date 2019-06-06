package com.tencent.midas.oversea.recharge

import android.app.Application
import android.os.StrictMode
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.data.DataHandler
import com.tencent.midas.oversea.recharge.login.CommLoginManager
import com.tencent.midas.oversea.recharge.ui.webview.WebViewPool
import com.tencent.smtt.sdk.QbSdk

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //init fresco
        Fresco.initialize(this)
        //init WebViewPool
        WebViewPool.singleton.init(this)
        //init x5
        QbSdk.initX5Environment(applicationContext, null)

        CommLoginManager.init()
        DataHandler.init(this@MApplication)

        //debug开启StrictMode
        if (BuildConfig.DEBUG) {
            MLog.d("MApplication", "start strict mode.")
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls()//检查自定义的耗时调用
                    //.detectDiskReads()//检查UI线程磁盘读写
                    //.detectDiskWrites()
                    .detectNetwork() //检查UI线程网络请求
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build())

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()//检查SQLite没有正确关闭
                    .detectLeakedClosableObjects()//检查资源没有正确关闭
                    .detectActivityLeaks()//检查Activity内存泄露
                    .detectLeakedRegistrationObjects()//检查BroadcastReceiver或ServiceConnection是否正确是否
                    .penaltyDeath()
                    .build())
        }
    }

}
