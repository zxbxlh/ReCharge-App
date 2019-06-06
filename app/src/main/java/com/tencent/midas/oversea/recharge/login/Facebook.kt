package com.tencent.midas.oversea.recharge.login

import android.app.Activity
import android.content.Intent
import android.net.Uri

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginResult
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.data.UserBean

import org.json.JSONException
import org.json.JSONObject

import java.util.Arrays

class Facebook : IMLogin {
    private var mLoginCallback: QueryUserCallback? = null
    private var fbCallbackManager: CallbackManager? = null
    private var hasInit = false

    private val isTokenValid: Boolean
        get() {
            val fbAccessToken = AccessToken.getCurrentAccessToken()
            MLog.d(TAG, "fbAccessToken is null: " + (fbAccessToken == null))
            return fbAccessToken != null && !fbAccessToken.isExpired
        }

    fun init() {
        if (!hasInit) {
            fbCallbackManager = CallbackManager.Factory.create()
            com.facebook.login.LoginManager.getInstance().registerCallback(fbCallbackManager!!, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    MLog.d(TAG, "登录成功")
                    queryUserProfileAfterLogin(loginResult.accessToken, mLoginCallback)
                }

                override fun onCancel() {
                    MLog.e(TAG, "登录取消")
                    queryUserProfileAfterLogin(null, mLoginCallback)
                }

                override fun onError(error: FacebookException) {
                    MLog.e(TAG, "登录错误")
                    queryUserProfileAfterLogin(null, mLoginCallback)
                }
            })
        } else {
            hasInit = true
        }
    }


    /**
     * 解析用户信息
     * @param response
     * @return
     */
    private fun parseUserData(response: GraphResponse?): UserBean? {
        var user: UserBean? = null
        try {
            val userObj = response?.jsonObject
            if (userObj != null) {
                user = UserBean(
                        userObj.optString("name"),
                        userObj.optString("id"),
                        userObj.optString("email"),
                        Uri.parse(userObj.getJSONObject("picture")
                                .getJSONObject("data").getString("url"))
                )
            }
        } catch (e: JSONException) {
            MLog.e(TAG, "parseUserData: " + e.message)
        }

        return user
    }


    /**
     * 登录完成后查用户信息
     * @param token
     * @param callback
     */
    private fun queryUserProfileAfterLogin(token: AccessToken?, callback: QueryUserCallback?) {
        if (token != null) {
            FBUserRequest.makeUserRequest(object :GraphRequest.Callback{
                override fun onCompleted(response: GraphResponse?) {
                    callback?.onCompleted(parseUserData(response))
                }
            })
        } else {
            callback?.onCompleted(null)
        }
    }

    override fun queryUserProfile(callback: QueryUserCallback?) {
        if (isTokenValid) {
            FBUserRequest.makeUserRequest(object :GraphRequest.Callback{
                override fun onCompleted(response: GraphResponse?) {
                    callback?.onCompleted(parseUserData(response))
                }
            })
        } else {
            callback?.onCompleted(null)
        }
    }

    override fun isLogin(): Boolean {
        return false
    }


    override fun login(activity: Activity, callback: QueryUserCallback) {
        mLoginCallback = callback
        init()

        if (isTokenValid) {
            queryUserProfile(mLoginCallback)
        } else {
            com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"))
        }
    }

    override fun logout() {
        com.facebook.login.LoginManager.getInstance().logOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        MLog.d(TAG, "onActivityResult")
        if (fbCallbackManager != null) {
            fbCallbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val TAG = "Facebook"
    }
}
