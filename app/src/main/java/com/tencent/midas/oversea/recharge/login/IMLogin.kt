package com.tencent.midas.oversea.recharge.login

import android.app.Activity
import android.content.Intent

interface IMLogin {
    //是否已登录
    fun isLogin(): Boolean

    //登录
    fun login(activity: Activity, callback: QueryUserCallback)

    //登出
    fun logout()

    //查询用户信息
    fun queryUserProfile(callback: QueryUserCallback?)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
}
