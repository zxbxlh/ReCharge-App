package com.tencent.midas.oversea.recharge.login

enum class LoginType private constructor(internal var value: String) {
    FACEBOOK("facebook"), TWITTER("twitter"), GOOGLE("google")
}
