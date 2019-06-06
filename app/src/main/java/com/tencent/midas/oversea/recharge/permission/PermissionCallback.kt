package com.tencent.midas.oversea.recharge.permission

import java.util.ArrayList

interface PermissionCallback {
    //权限申请通过回调
    fun onPermissionGranted()
    //权限被拒绝
    fun onPermissionReject(permissionItems: ArrayList<PermissionItem>)
}
