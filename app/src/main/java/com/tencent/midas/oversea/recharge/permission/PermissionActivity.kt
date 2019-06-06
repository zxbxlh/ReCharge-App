package com.tencent.midas.oversea.recharge.permission

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle

import java.util.ArrayList

class PermissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (!intent.hasExtra(KEY_PERMISSION)) {
            return
        }

        //api>=23，才申请权限
        val permissions = intent.getStringArrayExtra(KEY_PERMISSION)
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, RC_REQUEST_PERMISSION)
        }
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != RC_REQUEST_PERMISSION) {
            return
        }

        //处理申请结果
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        }

        this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    @TargetApi(23)
    internal fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray, shouldShowRequestPermissionRationale: BooleanArray) {

        var granted = 0
        val permissionItems = ArrayList<PermissionItem>(permissions.size)

        for (i in permissions.indices) {

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                val item = PermissionItem()
                item.permission = permissions[i]

                if (shouldShowRequestPermissionRationale[i]) {
                    //用户拒绝了权限
                    item.status = PermissionItem.RATIONAL
                } else {
                    //用户拒绝了权限，并选择了不再提示
                    item.status = PermissionItem.REJECT
                }

                permissionItems.add(item)
            } else {
                granted++
            }
        }

        if (granted == permissions.size) {
            CALLBACK!!.onPermissionGranted()
        } else {
            CALLBACK!!.onPermissionReject(permissionItems)
        }

        finish()
    }

    companion object {

        private var CALLBACK: PermissionCallback? = null
        private const val RC_REQUEST_PERMISSION = 100
        const val KEY_PERMISSION = "permissions"

        fun request(context: Context, permissions: Array<String>?, callback: PermissionCallback?) {
            CALLBACK = callback
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra(KEY_PERMISSION, permissions)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
