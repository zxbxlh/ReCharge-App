package com.tencent.midas.oversea.recharge.data

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tencent.midas.oversea.recharge.comm.MLog

/**
 * 单例模式
 */
object DataHandler
{
    const val TAG = "DataHandler"
    //default sp
    const val SP_DEFAULT = "sp.default"
    //后台下发充值中心url
    const val MSG_RECHARGE_URL = "msg.recharge.url"
    //default url
    private val DEFAULT_RECHARGE_URL = "https://www.midasbuy.com/id/buy?appid=1450015065"

    private var mRechargeUrl : String? = ""
    private var mApp:Context? = null


    //获取充值中心url
    val reChargeUrl:String?
        get(){
            if(!TextUtils.isEmpty(mRechargeUrl)){
                return mRechargeUrl;
            }

            MLog.d(TAG,"ReCharge url is empty.");
            return DEFAULT_RECHARGE_URL;
        }

    //初始化
    fun init(app:Context){
        mApp = app.applicationContext

        //获取配置数据
        val sp = mApp!!.getSharedPreferences(SP_DEFAULT,Context.MODE_PRIVATE)
        mRechargeUrl = sp.getString(MSG_RECHARGE_URL, DEFAULT_RECHARGE_URL)
    }

    //处理push data
    fun handleData(data:Map<String,String>?){

        if (data != null && !data.isEmpty()) {
            var rechargeUrl:String? = data[MSG_RECHARGE_URL]

            if (!TextUtils.isEmpty(rechargeUrl)) {
                MLog.d(TAG, "handleData map: $rechargeUrl")
                mRechargeUrl = rechargeUrl
                mApp!!.getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE)
                        .edit()
                        .putString(MSG_RECHARGE_URL, mRechargeUrl)
                        .apply()
            }
        }
    }

    //处理push data
    fun handleData(data: Bundle?) {
        var rechargeUrl :String = ""

        if (data != null && !data.isEmpty) {
            if(data.get(MSG_RECHARGE_URL) != null){
                rechargeUrl = data.get(MSG_RECHARGE_URL) as String
                MLog.d(TAG, "handleData bundle: $rechargeUrl")
            }
        }

        if (!TextUtils.isEmpty(rechargeUrl)) {
            mRechargeUrl = rechargeUrl
            mApp!!.getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE)
                    .edit()
                    .putString(MSG_RECHARGE_URL, mRechargeUrl)
                    .apply()
        }
    }
}