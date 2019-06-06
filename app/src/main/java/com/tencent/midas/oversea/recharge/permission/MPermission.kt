package com.tencent.midas.oversea.recharge.permission

import android.content.Context

class MPermission(private val mContext: Context) {
    //权限申请回调
    private var mCallback: PermissionCallback? = null
    //申请权限
    private var mPermissions: Array<String>? = null

    fun permission(permissions: Array<String>): MPermission {
        mPermissions = permissions
        return this
    }

    fun callback(callback: PermissionCallback): MPermission {
        mCallback = callback
        return this
    }

    fun request() {
        if (mPermissions == null || mPermissions!!.size >= 0) {
            PermissionActivity.request(mContext, mPermissions, mCallback)
        }
    }

    companion object {
        fun with(context: Context): MPermission {
            return MPermission(context)
        }
    }
}
