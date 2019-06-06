package com.tencent.midas.oversea.recharge.comm

import java.util.HashMap

/**
 * 用于统计时长
 */
object TimeTrace {
    val TAG = "TimeTrace"
    val WEBVIEW_START = "webview_start"

    //缓存统计值的开始时间
    internal var eventMap = HashMap<String, Long>(4)

    fun start(tag: String) {
        eventMap[tag] = System.currentTimeMillis()
    }


    fun stop(tag: String): Long {
        val start = eventMap[tag]
        var duration: Long = -1
        if (start != null) {
            eventMap.remove(tag)
            duration = System.currentTimeMillis() - start
        }

        MLog.d(TAG, "$tag,duration: $duration")
        return duration
    }
}
