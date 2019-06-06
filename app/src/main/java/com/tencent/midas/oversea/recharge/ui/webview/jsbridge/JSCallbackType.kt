package com.tencent.midas.oversea.recharge.ui.webview.jsbridge

/**
 * Android callback JS type
 */
enum class JSCallbackType private constructor(val value: String) {
    SUCCESS("success"), FAIL("fail"), CANCEL("cancel"), COMPLETION("completion")
}
