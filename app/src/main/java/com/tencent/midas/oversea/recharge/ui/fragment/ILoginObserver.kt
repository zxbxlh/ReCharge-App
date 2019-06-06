package com.tencent.midas.oversea.recharge.ui.fragment

import com.tencent.midas.oversea.recharge.data.LoginResult
import me.yokeyword.fragmentation.SupportFragment

interface ILoginObserver{
    /**
     * @param src current fragment
     * @param toTag to fragment tag
     */
    fun goFragment(src: SupportFragment,toTag:String)

    /**
     * after login
     */
    fun onLoginFinish(result:LoginResult)

    /**
     * after register
     */
    fun onRegisterFinish(result: LoginResult)
}