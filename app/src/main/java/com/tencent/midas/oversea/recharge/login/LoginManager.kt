package com.tencent.midas.oversea.recharge.login

import android.app.Activity
import android.content.Intent
import com.tencent.midas.oversea.recharge.data.UserBean
import java.util.LinkedList

class LoginManager {
    private var login: IMLogin? = null

    val isLogin: Boolean
        get() = sIsLogin

    val user: UserBean?
        get() = sUser

    fun login(activity: Activity, loginType: LoginType, callback: QueryUserCallback) {
        if (!sIsLogin) {
            val wrapperCallback = object :QueryUserCallback{
                override fun onCompleted(user: UserBean?) {
                    //if user is not null
                    user?.let {
                        sIsLogin = true
                        sUser = user
                        callback.onCompleted(user)
                    }
                }
            }

            if (loginType == LoginType.FACEBOOK) {
                //复用实例
                if (login == null || login !is Facebook) {
                    login = Facebook()
                }
            }

            login!!.login(activity, wrapperCallback)
        }
    }

    //登出
    fun logout() {
        sIsLogin = false
        if (login != null) {
            login!!.logout()
        } else {
            //尝试各个平台登出
            Facebook().logout()
        }
    }

    //传递activity结果
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (login != null) {
            login!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val TAG = "LoginManager"
        private var sUser: UserBean? = null
        private var sIsLogin = false    //是否登录


        //查询缓存用户登录信息
        fun refreshUserProfile(callback: QueryUserCallback?) {
            //FIFO，实现串行查询
            val fifoList = LinkedList<IMLogin>()
            fifoList.offer(Facebook())

            val wrapperCallback = object : QueryUserCallback {
                override fun onCompleted(user: UserBean?) {
                    sUser = user

                    if (user == null) {
                        if (!fifoList.isEmpty()) {
                            //查询下一登录态
                            fifoList.poll().queryUserProfile(this)
                        } else {
                            callback?.onCompleted(user)
                        }
                    } else {
                        sIsLogin = true
                        callback?.onCompleted(user)
                    }
                }
            }

            //依次查询
            fifoList.poll().queryUserProfile(wrapperCallback)
        }
    }
}
