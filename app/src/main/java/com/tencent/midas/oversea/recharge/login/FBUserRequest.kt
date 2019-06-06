package com.tencent.midas.oversea.recharge.login

import android.os.Bundle

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod

object FBUserRequest {
    private const val ME_ENDPOINT = "/me"

    fun makeUserRequest(callback: GraphRequest.Callback) {
        makeUserRequest(AccessToken.getCurrentAccessToken(), callback)
    }

    fun makeUserRequest(accessToken: AccessToken, callback: GraphRequest.Callback) {
        val bundle = Bundle()
        bundle.putString("fields", "picture,name,id,email,permissions")

        val request = GraphRequest(
                accessToken,
                ME_ENDPOINT,
                bundle,
                HttpMethod.GET,
                callback
        )
        request.executeAsync()
    }
}
