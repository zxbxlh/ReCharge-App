package com.tencent.midas.oversea.recharge.login

import com.tencent.midas.oversea.recharge.data.LoginResult
import com.tencent.midas.oversea.recharge.data.RegisterParams
import com.tencent.midas.oversea.recharge.data.UserBean
import com.tencent.midas.oversea.recharge.ui.fragment.ILoginObserver

/**
 * handle login relevant operation
 */
object CommLoginManager {
    const val TAG = "CommLoginManager"

    //login flag
    var isLogin:Boolean = false

    //login user data
    var user:UserBean? = null

    //init login info
    fun init(){
        //ToDo 测试代码，后续添加登录状态读取校验代码
        isLogin = false
    }

    //auto login
    fun login(callback: ILoginObserver){
        //ToDo 测试代码
        callback.onLoginFinish(LoginResult(LoginResult.RetCode.OK))
    }

    //login
    fun login(name:String,password:String,callback: ILoginObserver){
        //ToDo 测试代码
        callback.onLoginFinish(LoginResult(LoginResult.RetCode.OK))
    }

    //log out
    fun logout(){
        //ToDo 待添加
    }

    //register
    fun register(registerParams:RegisterParams,callback: ILoginObserver){
        //ToDo 测试代码
        callback.onRegisterFinish(LoginResult(LoginResult.RetCode.OK))
    }

    //find password
    fun findPassword(callback: ILoginObserver){

    }
}