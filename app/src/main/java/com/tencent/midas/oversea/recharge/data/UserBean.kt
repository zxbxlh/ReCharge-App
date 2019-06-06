package com.tencent.midas.oversea.recharge.data

import android.net.Uri

/**
 * 数据类
 */
data class UserBean(val name:String,
                    val id:String,
                    val email:String,
                    val logoUri: Uri)