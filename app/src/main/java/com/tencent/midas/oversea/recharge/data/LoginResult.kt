package com.tencent.midas.oversea.recharge.data

data class LoginResult(
        val retCode:RetCode,
        val retMsg:String = ""
){

    //login result
    enum class RetCode{
        OK,
        ERROR
    }
}