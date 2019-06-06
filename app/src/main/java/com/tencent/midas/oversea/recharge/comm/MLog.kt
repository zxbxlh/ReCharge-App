package com.tencent.midas.oversea.recharge.comm

import android.util.Log

object MLog {
    var logEnable:Boolean = true

    fun d(tag:String,msg:String){
        if(logEnable){
            Log.d(tag,msg)
        }
    }

    fun i(tag:String,msg:String){
        if(logEnable){
            Log.i(tag,msg)
        }
    }

    fun e(tag:String,msg:String){
        if(logEnable){
            Log.e(tag,msg)
        }
    }
}