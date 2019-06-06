package com.tencent.midas.oversea.recharge.login

import com.tencent.midas.oversea.recharge.data.UserBean

interface QueryUserCallback {
    fun onCompleted(user: UserBean?)
}
