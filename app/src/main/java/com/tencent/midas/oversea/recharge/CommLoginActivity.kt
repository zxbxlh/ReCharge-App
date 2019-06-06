package com.tencent.midas.oversea.recharge

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.data.LoginResult
import com.tencent.midas.oversea.recharge.login.CommLoginManager
import com.tencent.midas.oversea.recharge.ui.activity.MBaseActivity
import com.tencent.midas.oversea.recharge.ui.fragment.FindPasswordFragment
import com.tencent.midas.oversea.recharge.ui.fragment.ILoginObserver
import com.tencent.midas.oversea.recharge.ui.fragment.LoginFragment
import com.tencent.midas.oversea.recharge.ui.fragment.RegisterFragment
import com.tencent.midas.oversea.recharge.ui.view.MDialog
import me.yokeyword.fragmentation.SupportFragment

class CommLoginActivity:MBaseActivity(),ILoginObserver{

    companion object{
        const val TAG = "CommLoginActivity"

        fun start(activity: Activity) {
            val intent = Intent(activity, CommLoginActivity::class.java)
            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        //load login fragment default
        if (findFragment(LoginFragment::class.java) == null) {
            loadRootFragment(R.id.main_container, LoginFragment.newInstance())
        }
    }

    override fun onBackPressedSupport() {
        MLog.d(TAG, "onBackPressedSupport")

        finishSelf()

        //back to launcher
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    //switch fragment
    override fun goFragment(src: SupportFragment, toTag: String) {
        MLog.d(TAG, "goFragment: $toTag")
        when(toTag){
            "LoginFragment"->
                replaceFragment(LoginFragment.newInstance(),false)
            "RegisterFragment"->
                replaceFragment(RegisterFragment.newInstance(),false)
            "FindPasswordFragment"->
                replaceFragment(FindPasswordFragment.newInstance(),false)
        }
    }

    //after login
    override fun onLoginFinish(result: LoginResult) {
        MLog.d(TAG,"onLoginFinish: $result")
        when(result.retCode){
            LoginResult.RetCode.OK->
                onLoginSuccess(result)
            else->
                onLoginFailed(result)
        }
    }

    override fun onRegisterFinish(result: LoginResult) {
        MLog.d(TAG,"onRegisterFinish: $result")
        when(result.retCode){
            LoginResult.RetCode.OK->
                onRegisterSuccess(result)
            else->
                onRegisterFailed(result)
        }
    }

    //登录成功，保存用户信息，跳转
    private fun onLoginSuccess(result: LoginResult){
        //ToDo 测试代码
        finishSelf()
        MainActivity.start(this)
    }

    //登录失败，弹框提示
    private fun onLoginFailed(result:LoginResult){
        val dialog = MDialog(this,R.style.BottomDialog)
        with(dialog){
            setGravity(Gravity.BOTTOM)
            setLayout(R.layout.layout_dialog_permission)
            setWindowAnimation(R.style.BottomDialog_Animation)

            var errMsg = result.retMsg
            if(TextUtils.isEmpty(errMsg)){
                errMsg = resources.getString(R.string.default_failure)
            }

            findViewById<TextView>(R.id.dp_tips).text = errMsg
            findViewById<View>(R.id.dp_sure)?.setOnClickListener {
                dialog.dismiss()
            }
            show()
        }
    }

    //注册成功，保存数据，自动登录
    private fun onRegisterSuccess(result: LoginResult){
        //ToDo 保存数据....
        CommLoginManager.login(this)
    }

    //注册失败，弹框提示
    private fun onRegisterFailed(result:LoginResult){
        val dialog = MDialog(this,R.style.BottomDialog)
        with(dialog){
            setGravity(Gravity.BOTTOM)
            setLayout(R.layout.layout_dialog_permission)
            setWindowAnimation(R.style.BottomDialog_Animation)

            var errMsg = result.retMsg
            if(TextUtils.isEmpty(errMsg)){
                errMsg = resources.getString(R.string.default_failure)
            }

            findViewById<TextView>(R.id.dp_tips).text = errMsg
            findViewById<View>(R.id.dp_sure)?.setOnClickListener {
                dialog.dismiss()
            }
            show()
        }
    }
}