package com.tencent.midas.oversea.recharge.permission

class PermissionItem {

    var permission: String? = null
    var status = GRANTED

    companion object {
        val GRANTED = 0
        val REJECT = 1//权限被拒绝，并且勾选了"不再提醒"，即彻底被拒绝
        val RATIONAL = 2//权限被拒绝，但是没有勾选“不再提醒"
    }
}