package com.tencent.midas.oversea.recharge.comm

import android.content.Context
import android.util.TypedValue
import android.view.WindowManager

object UIUtils {
    val TAG = "UIUtils"

    fun dp2px(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun px2dp(context: Context, px: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.resources.displayMetrics).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = wm.defaultDisplay.width
        MLog.d(TAG, "screenWidth: $width")
        return width
    }

    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = wm.defaultDisplay.height
        MLog.d(TAG, "screenWidth: $width")
        return width
    }
}
